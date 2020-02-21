package ru.alfabank.alfamir.main.main_feed_fragment

import android.content.Intent
import com.google.common.base.Strings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.App
import ru.alfabank.alfamir.Constants.Companion.MAIN_FEED
import ru.alfabank.alfamir.Constants.Companion.SUBSCRIBE_TYPE_POST
import ru.alfabank.alfamir.Constants.Companion.VIEW_HOLDER_TYPE_NEWS
import ru.alfabank.alfamir.Constants.Companion.VIEW_HOLDER_TYPE_QUIZ
import ru.alfabank.alfamir.Constants.Post.COMMENTS_FIRST
import ru.alfabank.alfamir.Constants.Post.FEED_ID
import ru.alfabank.alfamir.Constants.Post.FEED_TYPE
import ru.alfabank.alfamir.Constants.Post.FEED_URL
import ru.alfabank.alfamir.Constants.Post.POST_ID
import ru.alfabank.alfamir.Constants.Post.POST_POSITION
import ru.alfabank.alfamir.favorites.data.source.repository.FavoriteRepository
import ru.alfabank.alfamir.feed.data.source.repository.PostDataSource
import ru.alfabank.alfamir.feed.data.source.repository.PostRepository
import ru.alfabank.alfamir.image.data.source.repository.ImageRepository
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.main.main_feed_fragment.contract.MainFeedListContract
import ru.alfabank.alfamir.main.main_feed_fragment.view_dummy.MainFeedViewDummy
import ru.alfabank.alfamir.post.data.dto.PostRaw
import ru.alfabank.alfamir.post.presentation.post.PostActivity
import ru.alfabank.alfamir.survey.data.source.repository.SurveyDataSource
import ru.alfabank.alfamir.survey.data.source.repository.SurveyRepository
import ru.alfabank.alfamir.utility.logging.remote.Logger
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract
import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker
import ru.alfabank.alfamir.utility.static_utilities.LinkHandler
import ru.alfabank.alfamir.utility.toBoolean
import javax.inject.Inject

class MainFeedListPresenter @Inject
constructor(private val mPostRepository: PostRepository,
            private val mSurveyRepository: SurveyRepository,
            private val favoriteRepository: FavoriteRepository,
            private val mImageRepository: ImageRepository,
            logger: Logger,
            private val mGetImage: GetImage) : MainFeedListContract.Presenter, LoggerContract.Client.NewsFeedAdapter {

    private val mCompositeDisposable = CompositeDisposable()
    private val mLogger: LoggerContract.Provider
    private var mAdapter: MainFeedListContract.Adapter? = null
    private var mProcessingRequest: Boolean = false
    private var mSurveyOffset: Int = 0
    private var isFavorite = false

    init {
        mLogger = logger
    }

    override fun takeListAdapter(adapter: MainFeedListContract.Adapter) {
        mAdapter = adapter
    }

    override fun getAdapter(): MainFeedListContract.Adapter {
        return if (mAdapter == null) MainFeedViewDummy() else mAdapter!!
    }

    override fun getListSize(): Int {
        val postType = "main"
        return mPostRepository.getCachedPostsListSize(postType) + mSurveyOffset
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            val surveyCover = mSurveyRepository.cachedSurveyCover ?: return VIEW_HOLDER_TYPE_NEWS

            mSurveyOffset = 1
            return VIEW_HOLDER_TYPE_QUIZ
        }
        return VIEW_HOLDER_TYPE_NEWS
    }

    override fun bindListRowSurveyView(position: Int, rowView: MainFeedListContract.SurveyRowView) {
        val surveyCover = mSurveyRepository.cachedSurveyCover

        val surveyTitle = surveyCover.title
        val name = surveyCover.localizedName
        val date = surveyCover.endDate
        val unformattedPostImageUrl = surveyCover.imgUrl

        val isCompleted = surveyCover.completed
        val answeredQuestionsCount = surveyCover.answeredQuestionsCount
        val totalQuestionsCount = surveyCover.requiredAnswerCount

        rowView.setTitle(surveyTitle)
        rowView.setSurveyTypeLocalizedName(name)

        if (isCompleted == 0) {
            if (answeredQuestionsCount == 0) {
                rowView.showEndDate("$name до $date")
                rowView.hideProgressBar()
            } else {
                val percent = (answeredQuestionsCount * 100.0f / totalQuestionsCount).toInt()
                rowView.showProgressBar("Завершено на $percent%", percent)
                rowView.hideEndDate()
            }
        } else {
            val completedText: String
            completedText = if (name == "Викторина")
                "$name пройдена"
            else
                "$name пройден"
            rowView.setCompleted(completedText)
            rowView.hideProgressBar()
        }

        handleSurveyImageUrl(rowView, unformattedPostImageUrl, position)
    }

    override fun bindListRowPostView(position: Int, rowView: MainFeedListContract.PostRowView) {
        val postType = "main"
        val post = mPostRepository.getCachedPostAtPosition(postType, position - mSurveyOffset) as PostRaw

        val authorName = post.author.name
        val authorInitials = InitialsMaker.formInitials(authorName)
        val unformattedAvatarUrl = post.author.picLink
        val date = post.date
        val headingTitle = post.headingTitle
        val title = post.title
        val body = post.shortText
        val unformattedPostImageUrl = post.imageUrl
        val options = post.options
        val likesEnabled = options.likesEnabled
        val commentsEnabled = options.commentsEnabled
        val optionsMenuEnabled = options.optionsMenuEnabled

        val likes = post.likes
        val currentUserLikeStatus = post.currentUserLike
        val comments = post.commentsCount

        rowView.clearPostImage()
        rowView.clearAuthorImage()
        rowView.clearAuthorInitials()

        if (likesEnabled == 1) {
            rowView.enableLikes()
            if (likes == 0) {
                rowView.hideLikesCount()
            } else {
                rowView.showLikesCount()
                rowView.setLikeStatus("" + likes, currentUserLikeStatus)
            }
        } else {
            rowView.disableLikes()
            rowView.hideLikesCount()
        }

        if (commentsEnabled == 1) {
            rowView.enableComments()
            if (comments == 0) {
                rowView.hideCommentsCount()
            } else {
                rowView.showCommentsCount()
                rowView.setComments("" + comments)
            }
        } else {
            rowView.disableComments()
            rowView.hideCommentsCount()
        }

        if (optionsMenuEnabled == 1) {
            rowView.showOptions()
        } else {
            rowView.hideOptions()
        }

        handleAvatarUrl(rowView, unformattedAvatarUrl, position, authorInitials)
        handlePostImageUrl(rowView, unformattedPostImageUrl, position)

        rowView.setAuthorName(authorName)
        rowView.setHeadingTitle(headingTitle)
        rowView.setPostTitle(title)
        rowView.setPostBody(body)
        rowView.setPostDate(date)
    }

    override fun onQuizClicked() {
        val quizCover = mSurveyRepository.cachedSurveyCover ?: return

        val quizId = quizCover.id
        adapter.openSurveyActivityUi("" + quizId)
    }

    override fun onQuizHideClicked() {
        val quizCover = mSurveyRepository.cachedSurveyCover
        val quizId = quizCover.id
        mSurveyRepository.hideSurvey("" + quizId, object : SurveyDataSource.HideSurveyCallback {
            override fun onSurveyHidden() {
                logHideSurvey(quizCover.type, quizCover.id)

                mSurveyRepository.dropCachedSurveyCover()
                mSurveyOffset = 0
                adapter.removePostAtPosition(0)
            }

            override fun onServerNotAvailable() {

            }
        })
    }

    override fun onPostClicked(position: Int) {
        val newsType = MAIN_FEED
        val positionInCachedPostArray = position - mSurveyOffset

        val post = mPostRepository.getCachedPostAtPosition(newsType, positionInCachedPostArray) as PostRaw

        openPost(positionInCachedPostArray, newsType, post.id, post.postUrl, post.type, false)
        logPostOpen(post.id, post.title)
    }

    private fun openPost(position: Int, feedType: String,  postId: String,  postUrl: String,  postType: String,  startWithComments: Boolean){
        val intent = Intent(adapter.context, PostActivity::class.java)
        intent.putExtra(POST_POSITION, position)
        intent.putExtra(FEED_TYPE, postType)
        intent.putExtra(POST_ID, postId)
        intent.putExtra(FEED_URL, postUrl)
        intent.putExtra(FEED_ID, "main")
        intent.putExtra(COMMENTS_FIRST, startWithComments)
        adapter.context.startActivity(intent)
    }

    override fun onAuthorClicked(position: Int) {
        val newsType = "main"
        val positionInCachedPostArray = position - mSurveyOffset
        val post = mPostRepository.getCachedPostAtPosition(newsType, positionInCachedPostArray) as PostRaw
        val authorId = post.author.id
        adapter.openProfileActivityUi(authorId)
    }

    override fun onHeadlineTitleClicked(position: Int) {
        val newsType = "main"
        val positionInCachedPostArray = position - mSurveyOffset
        val post = mPostRepository.getCachedPostAtPosition(newsType, positionInCachedPostArray) as PostRaw

        val title = post.headingTitle
        val type = post.type
        val postUrl = post.postUrl
        adapter.openNewsActivityUi(title, type, postUrl)
    }

    override fun onCommentsClicked(position: Int) {
        val newsType = "main"
        val positionInCachedPostArray = position - mSurveyOffset

        val post = mPostRepository.getCachedPostAtPosition(newsType, positionInCachedPostArray) as PostRaw

        openPost(positionInCachedPostArray, MAIN_FEED, post.id, post.postUrl, post.type, true)
    }

    override fun onLikeClicked(position: Int) {
        if (!mProcessingRequest) {
            mProcessingRequest = true
            likePost(position)
        }
    }

    override fun onOptionsClicked(position: Int) {
        val newsType = "main"
        val positionInCachedPostArray = position - mSurveyOffset
        val post = mPostRepository.getCachedPostAtPosition(newsType, positionInCachedPostArray) as PostRaw

        val neverUsed = favoriteRepository.isFavorite(post.postUrl, post.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ v ->
                    isFavorite = v.isFavorite.toBoolean()
                    val favoriteStatus = if (isFavorite) "Удалить из избранного" else "Добавить в избранное"
                    val isDeletable = post.isDeletable
                    val subscribeStatus = if (post.isSubscribed.toBoolean()) {
                        "Отписаться"
                    } else {
                        "Подписаться"
                    }
                    adapter.onPostOptionsClicked(position, isDeletable, favoriteStatus, subscribeStatus)
                },
                        Throwable::printStackTrace)
    }

    override fun onMenuItemDeleteClicked(position: Int) {
        val postFeedType = "main"
        val positionInCachedPostArray = position - mSurveyOffset

        val post = mPostRepository.getCachedPostAtPosition(postFeedType, positionInCachedPostArray) as PostRaw
        val postId = post.id
        val postType = post.type
        val postUrl = post.postUrl

        mPostRepository.deletePost(postType, postUrl, postId, object : PostDataSource.DeletePostCallback {
            override fun onPostDeleted() {
                mPostRepository.deleteCachedPostAtPosition(postFeedType, positionInCachedPostArray)
                adapter.removePostAtPosition(position)
            }

            override fun onServerNotAvailable() {}
        })
    }

    override fun onMenuItemFavoriteClicked(position: Int) {
        val postFeedType = "main"
        val positionInCachedPostArray = position - mSurveyOffset
        val post = mPostRepository.getCachedPostAtPosition(postFeedType, positionInCachedPostArray) as PostRaw
        val postId = post.id

        if (isFavorite) {
            favoriteRepository.removeFavoritePage(postId)
        } else {
            favoriteRepository.addFavoritePage(post.postUrl, post.type, postId)
        }

    }

    override fun onMenuItemSubscribeClicked(position: Int) {
        val postFeedType = "main"
        val positionInCachedPostArray = position - mSurveyOffset

        val post = mPostRepository.getCachedPostAtPosition(postFeedType, positionInCachedPostArray) as PostRaw
        val postId = post.id

        if (App.subscribeRepository.checkIfSubscribed(postId)) {
            App.subscribeRepository.unsubscribe(SUBSCRIBE_TYPE_POST, post.id, post.postUrl, post.threadId, post.type)
            logPostSubscribe(post.id, post.title, false)
        } else {
            App.subscribeRepository.subscribe(SUBSCRIBE_TYPE_POST, post.id, post.postUrl, post.threadId, post.type)
            logPostSubscribe(post.id, post.title, true)
        }
    }

    private fun likePost(position: Int) {
        val newsType = "main"
        val positionInCachedPostArray = position - mSurveyOffset

        val post = mPostRepository.getCachedPostAtPosition(newsType, positionInCachedPostArray) as PostRaw
        val isLikedByCurrentUser = post.currentUserLike
        val postType = post.type
        val postUrl = post.postUrl
        val postId = post.id

        val targetLikeStatus: Int
        val targetLikeCountChange: Int
        if (isLikedByCurrentUser == 0) {
            targetLikeStatus = 1
            targetLikeCountChange = 1
        } else {
            targetLikeStatus = 0
            targetLikeCountChange = -1
        }

        post.currentUserLike = targetLikeStatus
        post.likes = post.likes + targetLikeCountChange
        adapter.setLikeStatus(position, "" + post.likes, targetLikeStatus)

        mPostRepository.likePost(postId, postUrl, postType, targetLikeStatus, object : PostDataSource.LikePostCallback {
            override fun onPostLiked() {
                mProcessingRequest = false

                val likeStatus: Boolean = targetLikeStatus != 0
                logPostLike(post.id, post.title, likeStatus)
            }

            override fun onServerNotAvailable() {
                post.currentUserLike = isLikedByCurrentUser
                post.likes = post.likes - targetLikeCountChange
                adapter.setLikeStatus(position, "" + post.likes, isLikedByCurrentUser)
                mProcessingRequest = false
            }
        })
    }

    private fun handleSurveyImageUrl(rowView: MainFeedListContract.SurveyRowView, unformattedPostImageUrl: String, position: Int) {
        if (Strings.isNullOrEmpty(unformattedPostImageUrl)) return

        val postImageUrl = LinkHandler.getPhotoLink(unformattedPostImageUrl)
        loadImage(postImageUrl, position, ImageType.SURVEY)
    }

    private fun handlePostImageUrl(rowView: MainFeedListContract.PostRowView, unformattedPostImageUrl: String, position: Int) {
        if (Strings.isNullOrEmpty(unformattedPostImageUrl)) {
            rowView.hidePostImage()
            return
        }

        val layoutHeight = LinkHandler.getPhotoPixelHeightMatchingScreenWidth(unformattedPostImageUrl)
        rowView.setPostImageSizeParameters(layoutHeight)
        val postImageUrl = LinkHandler.getPhotoLink(unformattedPostImageUrl)
        loadImage(postImageUrl, position, ImageType.POST)
    }

    private fun handleAvatarUrl(rowView: MainFeedListContract.PostRowView, unformattedAvatarUrl: String?, position: Int, initials: String) {
        if (Strings.isNullOrEmpty(unformattedAvatarUrl)) {
            rowView.setAuthorPlaceholder(initials)
            return
        }

        val avatarUrl = LinkHandler.getPhotoLink(unformattedAvatarUrl)
        loadImage(avatarUrl, position, ImageType.AVATAR)
    }

    private fun loadImage(url: String, position: Int, type: ImageType) {

        CoroutineScope(Dispatchers.Main).launch {
            mCompositeDisposable.add(mGetImage.bitmap(url, 48)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        when (type) {
                            ImageType.AVATAR -> adapter.onAuthorAvatarDownloaded(position, it, true)
                            ImageType.POST -> adapter.onPostImageDownloaded(position, it, !true)
                            ImageType.SURVEY -> adapter.onSurveyImageDownload(position, it, !true)
                        }
                    }, Throwable::printStackTrace))
        }
    }

    override fun logPostOpen(postId: String, postTitle: String) {
        mLogger.openPost(postId, postTitle)
    }

    override fun logPostLike(postId: String, postTitle: String, isLiked: Boolean) {
        mLogger.like(postId, postTitle, isLiked)
    }

    override fun logPostSubscribe(postId: String, postTitle: String, isSubscribed: Boolean) {
        mLogger.postSubscribe(postId, postTitle, isSubscribed)
    }

    override fun logPostFavorite(postId: String, postTitle: String, isFavoured: Boolean) {
        mLogger.postFavorite(postId, postTitle, isFavoured)
    }

    override fun logHideSurvey(type: String, surveyId: Int) {
        mLogger.onHideSurvey(type, surveyId)
    }

    private enum class ImageType {
        AVATAR, POST, SURVEY
    }
}
