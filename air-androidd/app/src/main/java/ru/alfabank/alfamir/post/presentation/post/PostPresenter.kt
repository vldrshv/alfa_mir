package ru.alfabank.alfamir.post.presentation.post

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.google.common.base.Strings
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.Constants.Companion.NO_SERVER_CONNECTION
import ru.alfabank.alfamir.Constants.Companion.SUBSCRIBE_TYPE_POST
import ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_0
import ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_5
import ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_GREENWICH
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_DP
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_PHYSICAL
import ru.alfabank.alfamir.Constants.Post.POST_SUB_HEADER
import ru.alfabank.alfamir.alfa_tv.data.source.repository.show.ShowRepository
import ru.alfabank.alfamir.data.source.repositories.favorite_pages.FavoritePagesRepositoryNew
import ru.alfabank.alfamir.data.source.repositories.old_trash.SubscribeRepository
import ru.alfabank.alfamir.di.qualifiers.feed.TitlesVisibility
import ru.alfabank.alfamir.di.qualifiers.post.*
import ru.alfabank.alfamir.favorites.data.source.repository.FavoriteRepository
import ru.alfabank.alfamir.feed.data.source.repository.PostRepository
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.post.data.dto.Post
import ru.alfabank.alfamir.post.data.dto.PostInterface
import ru.alfabank.alfamir.post.data.dto.ServerResponse
import ru.alfabank.alfamir.post.presentation.dto.*
import ru.alfabank.alfamir.post.presentation.post.dummy_view.PostAdapterDummy
import ru.alfabank.alfamir.post.presentation.post.dummy_view.PostViewDummy
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostAdapterContract
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostContract
import ru.alfabank.alfamir.utility.enums.FormatElement
import ru.alfabank.alfamir.utility.network.IpProvider
import ru.alfabank.alfamir.utility.static_utilities.DateConverter
import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker
import ru.alfabank.alfamir.utility.static_utilities.LinkHandler
import ru.alfabank.alfamir.utility.toBoolean
import java.util.regex.Pattern
import javax.inject.Inject

class PostPresenter @Inject
internal constructor(private val mPostRepository: PostRepository,
                     private val mFavoritePagesRepositoryNew: FavoritePagesRepositoryNew,
                     private val favoriteRepository: FavoriteRepository,
                     private val mSubscribeRepository: SubscribeRepository,
                     private val mAlfaTvRepository: ShowRepository,
                     private val mIpManager: IpProvider,
                     private val mGetImage: GetImage,
                     @param:PostUrl private val mPostUrl: String,
                     @param:PostId private val mPostId: String,
                     @param:FeedID private val mFeedId: String,
                     @param:FeedType private val mFeedType: String,
                     @param:FeedUrl private val mFeedUrl: String,
                     @param:PostPosition private val mPostPosition: Int,
                     @param:CommentsFirst private val mCommentsFirst: Boolean,
                     @param:TitlesVisibility private val mShowTitles: Boolean,
                     @param:CommentId private val mCommentId: String) : PostContract.Presenter {

    private var mPostView: PostContract.View? = null
    private var mAdapter: PostAdapterContract.Adapter? = null

    private var isPostLikeBeingProcessed: Boolean = false
    private var isCommentLikeBeingProcessed: Boolean = false
    private var mIsDataLoaded: Boolean = false

    private var mReplyTargetCommentId: String? = null

    private var mPostElements: MutableList<PostElement>? = null
    private var mPost: Post? = null
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var isFavorite = false

    private val EMPTY_IP = "0.0.0.0"

    override fun takeView(view: PostContract.View) {
        mPostView = view
        if (!mIsDataLoaded) {
            loadPost()
        } else {
            checkForPausedWebView()
        }
    }

    override fun dropView() {
        mCompositeDisposable.dispose()
        mPostView = null
        job.cancel()
    }

    override fun getView(): PostContract.View {
        return if (mPostView == null) {
            PostViewDummy()
        } else mPostView!!
    }

    override fun getAdapter(): PostAdapterContract.Adapter {
        return if (mAdapter == null) {
            PostAdapterDummy()
        } else mAdapter!!
    }

    @SuppressLint("CheckResult")
    private fun loadPost() {
        mSubscribeRepository.initialize()
        view.showLoadingProgress()
        view.showCurtain()

        if (Strings.isNullOrEmpty(mPostUrl)) {
            loadPostWithFullData()
        } else {
            loadPostWithUrl()
        }
    }

    private fun loadPostWithFullData() {
        mCompositeDisposable.add(mPostRepository.getPostNew(mFeedId, mPostId, mFeedUrl, mFeedType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { post -> onPostLoaded(post) },
                        { throwable -> Log.e("loadPost", throwable.message) }
                )
        )
    }

    private fun loadPostWithUrl() {
        mCompositeDisposable.add(mPostRepository.getPostNew(mPostUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { post -> onPostLoaded(post) },
                        { throwable -> Log.e("loadPost", throwable.message) }
                )
        )
    }
    private fun onPostLoaded(post: Post) {
        mPost = post
        mPostElements = post.postElementList
        showPost()

        if (mPost!!.commentsEnabled == 1 || mPost!!.commentsCount == 0) {
            loadComments()
        } else {
            mIsDataLoaded = true
            view.hideLoadingProgress()
        }
    }

    @SuppressLint("CheckResult")
    private fun loadComments() {
        val postType = mPost!!.type
        val threadId = mPost!!.threadId
        val postUrl = mPost!!.url

        mCompositeDisposable.add(
                mPostRepository.getCommentsNew(postType, threadId, postUrl)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { comments ->
                                    clearComments()
                                    insertComments(comments)
                                    onDataLoaded()
                                },
                                {
                                    view.hideLoadingProgress()
                                    view.showSnackBar(NO_SERVER_CONNECTION)
                                }
                        )
        )
    }

    private fun clearComments() {
        val iterator = mPostElements!!.listIterator()
        while (iterator.hasNext()) {
            val element = iterator.next() // must be called before you can onCallClicked i.remove()
            // Do something
            if (element is CommentElement) {
                iterator.remove()
                val position = iterator.nextIndex()
                view.onItemRemoved(position)
            }
        }
    }

    override fun onReply(position: Int) {
        val comment = mPostElements!![position] as CommentElement
        val authorName = comment.author.name
        val autoInput = "$authorName, "

        mReplyTargetCommentId = comment.id
        view.hideFloatingButton()
        view.showKeyboard(autoInput)
    }

    override fun onFBClicked() {
        mReplyTargetCommentId = null
        view.hideFloatingButton()
        view.showKeyboard(null)
    }

    override fun onKeyboardHidden() {

    }

    override fun onPostRefresh() {
        mPostRepository.refreshItem()
        loadPost()
    }

    override fun onSendComment(comment: String) {
        view.disableTextInput()
        view.hideKeyboard()
        view.showFloatingButton(100)
        sendComment(comment)
    }

    @SuppressLint("CheckResult")
    override fun onDeleteComment(position: Int) {
        val comment = mPostElements!![position] as CommentElement

        val commentId = comment.id
        val postUrl = mPost!!.url
        val commentType = mPost!!.type + "comment"

        mCompositeDisposable.add(mPostRepository.deleteComment(commentId, commentType, postUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.showLoadingProgress()
                    loadComments()
                }, { view.showSnackBar(NO_SERVER_CONNECTION) }))
    }

    @SuppressLint("CheckResult")
    private fun sendComment(comment: String) {
        val postId = mPost!!.id
        val postType = mPost!!.type
        val threadId = mPost!!.threadId
        val postUrl = mPost!!.url

        mCompositeDisposable.add(mPostRepository.sendComment(postId, postType, postUrl, threadId, mReplyTargetCommentId, comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( // down function should be here
                        { getNewComments(postType, threadId, postUrl) },
                        {
                            view.showSnackBar(NO_SERVER_CONNECTION)
                            view.enableTextInput()
                        }
                )
        )
    }
    private fun getNewComments(postType: String, threadId: String, postUrl: String) {
        view.showLoadingProgress()
        val disposable = mPostRepository.getCommentsNew(postType, threadId, postUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { comments ->

                            clearComments()
                            insertComments(comments)
                            onDataLoaded()
                            clearCommentsInputField()

                        },
                        {
                            view.hideLoadingProgress()
                            view.showSnackBar(NO_SERVER_CONNECTION)
                            view.enableTextInput()
                        }
                )
    }
    private fun insertComments( comments: List<PostElement>) {
        if (comments.isNotEmpty()) {
            for (commentElement in comments) {
                mPostElements!!.add(mPostElements!!.size - 1, commentElement)
                view.onItemInserted(mPostElements!!.size - 2)
            }
        }
    }
    private fun onDataLoaded() {
        mIsDataLoaded = true
        view.hideLoadingProgress()
    }
    private fun clearCommentsInputField() {
        view.setTextEditInput("")
        view.enableTextInput()
    }

    // PostAdapterContract.Presenter

    override fun getListSize(): Int {
        return mPostElements!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return mPostElements!![position].viewType
    }

    override fun takeListAdapter(adapter: PostAdapterContract.Adapter) {
        mAdapter = adapter
    }

    override fun bindListRowHeader(position: Int, rowView: PostAdapterContract.HeaderRowView) {
        val headerElement = mPostElements!![position] as HeaderElement

        handleAvatarUrl(headerElement, rowView)

        val authorName = headerElement.authorName
        rowView.setAuthorName(authorName)

        setHeadingTitle(headerElement, rowView)
        setPostTitle(headerElement, rowView)
        setOptionsMenu(headerElement, rowView)

        rowView.setPostDate(headerElement.date)
    }
    private fun handleAvatarUrl(headerElement: HeaderElement, rowView: PostAdapterContract.HeaderRowView) {
        if (isUrlNotSpecified(headerElement.unformattedAvatarUrl)) {
            setAvatarPlaceholder(headerElement.authorInitials, rowView)
            return
        }

        uiScope.launch {
            mCompositeDisposable.add(
                    mGetImage.bitmap(headerElement.unformattedAvatarUrl, 48)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { rowView.setAuthorAvatar(it, true) },
                            Throwable::printStackTrace
                    )
            )
        }
    }
    private fun isUrlNotSpecified(url: String) = Strings.isNullOrEmpty(url)
    private fun setAvatarPlaceholder(authorInitials: String, rowView: PostAdapterContract.HeaderRowView) {
        rowView.setAuthorPlaceholder(authorInitials)
        rowView.setAuthorInitials(authorInitials)
    }
    private fun setHeadingTitle(headerElement: HeaderElement, rowView: PostAdapterContract.HeaderRowView) {
        if (headerElement.headingVisible == 1) {
            rowView.setHeadingTitle(headerElement.headingTitle)
        } else {
            rowView.hideHeadingTitle()
        }
    }
    private fun setPostTitle(headerElement: HeaderElement, rowView: PostAdapterContract.HeaderRowView) {
        if (headerElement.titleVisible == 1) {
            rowView.setPostTitle(headerElement.title)
        } else {
            rowView.hidePostTitle()
        }
    }
    private fun setOptionsMenu(headerElement: HeaderElement, rowView: PostAdapterContract.HeaderRowView) {
        if (headerElement.optionsMenuEnabled == 1) {
            rowView.showOptionsButton()
        } else {
            rowView.hideOptionsButton()
        }
    }

    override fun bindListRowText(position: Int, rowView: PostAdapterContract.TextRowView) {
        val textElement = mPostElements!![position] as TextElement
        var text = textElement.text
//        text = HtmlWrapper.convert(text)
        rowView.setPostBody(text)

        if (textElement.viewType == POST_SUB_HEADER)
            rowView.isSubHeader(true)
    }

    override fun bindListRowHtml(position: Int, rowView: PostAdapterContract.HtmlRowView) {
        val htmlElement = mPostElements!![position] as HtmlElement
        val html = htmlElement.html

        if (html != null)
            rowView.setHtml(html)
    }

    override fun bindListRowSingleHtml(position: Int, rowView: PostAdapterContract.SingleHtmlRowView) {
        val htmlElement = mPostElements!![position] as SingleHtmlElement
        val html = htmlElement.html
        if (html != null)
            rowView.setHtml(html)
    }

    override fun bindListRowPicture(position: Int, rowView: PostAdapterContract.PictureRowView) {
        val pictureElement = mPostElements!![position] as PictureElement

        val url = pictureElement.url
        val height = pictureElement.height
        val width = pictureElement.width
        val coef = SCREEN_WIDTH_PHYSICAL.toFloat() / width
        val properHeight = (height * coef).toInt()

        rowView.setPostImageSizeParameters(properHeight)
        rowView.clearImage()

        uiScope.launch {
            mCompositeDisposable.add(mGetImage.bitmap(url, SCREEN_WIDTH_PHYSICAL)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { rowView.setPostImage(it, true) },
                            { throwable -> Log.e(javaClass.simpleName, throwable.message) }
                    )
            )
        }
    }

    override fun bindListRowQuote(position: Int, rowView: PostAdapterContract.QuoteRowView) {
        val quoteElement = mPostElements!![position] as QuoteElement
        val imageUri = quoteElement.getAuthorImageUri()
        if (imageUri.isNullOrEmpty())
            rowView.setAuthorInitials(quoteElement.getAuthorInitials())
        else
            setImageFromUrl(rowView, quoteElement.getAuthorImageUri(), 48)

        rowView.setAuthorName(quoteElement.getAuthor())
        rowView.setAuthorJobPosition(quoteElement.getAuthorJobPosition())
        rowView.setQuoteBody(quoteElement.getQuoteText())
    }

    override fun bindListRowVideo(position: Int, rowView: PostAdapterContract.VideoRowView) {
        val videoElement = mPostElements!![position] as VideoElement
        val videoId = videoElement.getVideoId()
        val phoneIp = getPhoneIp()
        val d = mPostRepository.getVideo(videoId, phoneIp).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                        val gson = Gson()
                        val serverResponse = gson.fromJson(it, ServerResponse::class.java)

                        rowView.configureVideo(serverResponse.url, serverResponse.token)
                    },
                        { t -> t.printStackTrace() }
                )
    }

    private fun setImageFromUrl(rowView: PostAdapterContract.QuoteRowView, unformattedAvatarUrl: String, targetDp: Int = 48) {
        uiScope.launch {
            mCompositeDisposable.add(mGetImage.bitmap(unformattedAvatarUrl, targetDp)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { rowView.setAuthorAvatar(it, true) },
                            Throwable::printStackTrace
                    )
            )
        }
    }
    override fun onQuoteProfileClicked(position: Int) {
        val quoteElement = mPostElements!![position] as QuoteElement
        val account = quoteElement.getAuthorAccount()
        if (account != null)
            view.openProfileActivityUi(account)
        else
            cannotOpenUriMessage()
    }

    override fun bindListRowFooter(position: Int, rowView: PostAdapterContract.FooterRowView) {
        val footerElement = mPostElements!![position] as FooterElement

        setPostLikes(footerElement, rowView)
        setPostComments(footerElement, rowView)
    }
    private fun setPostLikes(footerElement: FooterElement, rowView: PostAdapterContract.FooterRowView) {
        if (footerElement.likesEnabled == 1) {
            rowView.enableLikes()
            if (footerElement.likes == 0) {
                rowView.hideLikesCount()
            } else {
                rowView.setLikeStatus("${footerElement.likes}", footerElement.currentUserLike)
            }
        } else {
            rowView.disableLikes()
            rowView.hideLikesCount()
        }
    }
    private fun setPostComments(footerElement: FooterElement, rowView: PostAdapterContract.FooterRowView) {
        if (footerElement.commentsEnabled == 1) {
            rowView.enableComments()
            if (footerElement.comments == 0) {
                rowView.hideCommentsCount()
            } else {
                rowView.setCommentCount("${footerElement.comments}")
            }
        } else {
            rowView.disableComments()
            rowView.hideCommentsCount()
        }
    }

    override fun bindListRowComment(position: Int, rowView: PostAdapterContract.CommentRowView) {
        val comment = mPostElements!![position] as CommentElement

        rowView.clearAuthorImage()
        rowView.clearAuthorInitials()

        val imageUrl = LinkHandler.getPhotoLink(comment.author.picLink)
        val formattedDate = DateConverter.formatDate(comment.date, DATE_PATTERN_0, DATE_PATTERN_5, TIME_ZONE_GREENWICH, FormatElement.COMMENT)

        rowView.setAuthorName(comment.author.name)
        rowView.setCommentText(comment.text)
        rowView.setCommentDate(formattedDate)
        rowView.setDeletable(comment.deletable)
        rowView.setLikeStatus("${comment.likes}", comment.userLike)
        rowView.setMargins(comment.isSecondLevel)

        if (isUrlNotSpecified(imageUrl)) {
            setCommentsAuthorPlaceholder(comment, rowView)
        } else {
            setCommentsAuthorImage(imageUrl, rowView)
        }
    }
    private fun setCommentsAuthorPlaceholder(comment: CommentElement, rowView: PostAdapterContract.CommentRowView) {
        val initials = InitialsMaker.formInitials(comment.author.name)
        rowView.setAuthorInitials(initials)
        rowView.setAuthorPicPlaceholder(initials)
    }
    private fun setCommentsAuthorImage(imageUrl: String, rowView: PostAdapterContract.CommentRowView) {
        uiScope.launch {
            mCompositeDisposable.add(mGetImage.bitmap(imageUrl, 48)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { rowView.setAuthorImage(it, true) },
                            { throwable -> Log.e(javaClass.simpleName, throwable.message) }
                    )
            )
        }
    }

    override fun onProfileClicked() {
        val headerElement = mPostElements!![0] as HeaderElement
        val profileId = headerElement.authorId
        view.openProfileActivityUi(profileId)
    }

    override fun onProfileClicked(position: Int) {
        val headerElement = mPostElements!![position] as CommentElement
        val profileId = headerElement.author.id
        view.openProfileActivityUi(profileId)
    }

    @SuppressLint("CheckResult")
    override fun onCommentLiked(position: Int) {
        if (isCommentLikeBeingProcessed)
            return
        else
            isCommentLikeBeingProcessed = true

        val comment = mPostElements!![position] as CommentElement
        val commentId = comment.id
        val postUrl = mPost!!.url
        val commentType = mPost!!.type + "comment"
        val likes = comment.likes
        val likeStatus = comment.userLike
        val newLikeStatus: Int
        val newLikes: Int
        newLikeStatus = if (likeStatus == 0) 1 else 0
        newLikes = if (newLikeStatus == 0) likes - 1 else likes + 1
        setCommentLikeStatus(newLikeStatus, newLikes, position)

        mCompositeDisposable.add(
                mPostRepository.likeComment(commentId, postUrl, commentType, newLikeStatus)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { isCommentLikeBeingProcessed = false },
                                {
                                    setCommentLikeStatus(likeStatus, likes, position)
                                    isCommentLikeBeingProcessed = false
                                    view.showSnackBar(NO_SERVER_CONNECTION)
                                }
                        )
        )
    }

    @SuppressLint("CheckResult")
    override fun onPostLiked(position: Int) {
        if (isPostLikeBeingProcessed)
            return
        else
            isPostLikeBeingProcessed = true

        val id = mPost!!.id
        val type = mPost!!.type
        val url = mPost!!.url
        val currentUserLikeStatus = mPost!!.currentUserLike
        val newUserLikeStatus: Int
        newUserLikeStatus = if (currentUserLikeStatus == 0) 1 else 0
        setPostLikeStatus(newUserLikeStatus, position)

        mCompositeDisposable.add(
                mPostRepository.likePostTest(id, url, type, newUserLikeStatus, mFeedType)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { isPostLikeBeingProcessed = false },
                                {
                                    setPostLikeStatus(currentUserLikeStatus, position)
                                    isPostLikeBeingProcessed = false
                                    view.showSnackBar(NO_SERVER_CONNECTION)
                                }
                        )
        )
    }

    override fun onHeadingClicked() {
        val headerElement = mPostElements!![0] as HeaderElement
        val heading = headerElement.headingTitle
        val postType = mPost!!.type
        val url = mPost!!.url

        view.openNewsActivityUi(heading, postType, url)
    }

    override fun onOptionsClicked() {
        val isDeletable = 0
        val postId = mPost!!.id

        val neverUsed = favoriteRepository.isFavorite(mPostUrl, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { v ->
                            isFavorite = v.isFavorite.toBoolean()

                            val favoriteStatus = if (isFavorite) "Удалить из избранного" else "Добавить в избранное"
                            val subscribeStatus = if (mSubscribeRepository.checkIfSubscribed(postId)) "Отписаться" else "Подписаться"

                            adapter.openPostOptions(isDeletable, favoriteStatus, subscribeStatus)
                        },
                        Throwable::printStackTrace)
    }

    override fun onMenuItemDeleteClicked() {
    }

    override fun onMenuItemFavoriteClicked() {
        val type = mPost!!.type
        val url = mPost!!.url
        val id = mPost!!.id

        if (isFavorite) {
            favoriteRepository.removeFavoritePage(id)
        } else {
            favoriteRepository.addFavoritePage(url, type, id)
        }
    }

    override fun onMenuItemSubscribeClicked() {
        if (mSubscribeRepository.checkIfSubscribed(mPost!!.id)) {
            mSubscribeRepository.unsubscribe(SUBSCRIBE_TYPE_POST, mPost!!.id, mPost!!.url, mPost!!.threadId, mPost!!.type)
        } else {
            mSubscribeRepository.subscribe(SUBSCRIBE_TYPE_POST, mPost!!.id, mPost!!.url, mPost!!.threadId, mPost!!.type)
        }
    }

    override fun onContentLoaded(position: Int) {
        hideCurtain()
        loadPostImages(position)
        loadVideoPosters(position)
    }

    private fun hideCurtain() {
        if (mCommentsFirst) {
            var footerPosition = 0
            for (i in mPostElements!!.indices) {
                val element = mPostElements!![i]
                if (element is FooterElement) {
                    footerPosition = i
                }
            }
            view.scrollToPosition(footerPosition + 1) // could be done via EmptySpace element, but it might be already removed, hence + 1;
        }
        view.hideCurtain()
    }

    private fun loadPostImages(position: Int) {
        val imageList = mPost!!.pageImages
        for (i in imageList.indices) {
            val postImage = imageList[i]
            val id = postImage.id
            val url = postImage.url

            uiScope.launch {
                mCompositeDisposable.add(mGetImage.string(url, SCREEN_WIDTH_DP)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { adapter.onPostImageInject(position, "setImage(\"$id\",\"data:image/jpeg;charset=utf-8;base64,$it\");") },
                                { throwable -> Log.e(javaClass.simpleName, throwable.message) }
                        )
                )
            }
        }
    }

    private fun loadVideoPosters(position: Int) {
        val videos = mPost!!.pageVideos
        for (video in videos) {
            val url = video.posterUrl
            val id = video.id

            uiScope.launch {
                mCompositeDisposable.add(mGetImage.string(url, SCREEN_WIDTH_DP)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { adapter.onPostVideoPosterInject(position, "setPoster(\"$id\",\"data:image/jpeg;charset=utf-8;base64,$it\");") },
                                { throwable -> Log.e(javaClass.simpleName, throwable.message) }
                        )
                )
            }
        }
    }

    override fun onJsPrompt(message: String, position: Int) {

    }

    @SuppressLint("CheckResult")
    override fun onVideoClicked(id: String, password: String, position: Int) {
        val videos = mPost!!.pageVideos
        for (video in videos) {
            if (video.id == id) {
                val archiveId = video.archive_id

                val ip = getPhoneIp()

                mAlfaTvRepository.getVideoUrl(archiveId, ip, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { url -> adapter.onPostVideoUrlInject(position, "changeVideoSource(\"$id\",\"$url\")") },
                                { error -> Log.e("Poster", error.message) }
                        )
            }
        }
    }
    private fun getPhoneIp(): String {
        var ip = "" // "172.28.67.209";
        val cellularIp = mIpManager.mobileIPAddress
        val wifiIp = mIpManager.wifiIPAddress
        if (cellularIp != EMPTY_IP) {
            ip = cellularIp
        } else if (wifiIp != EMPTY_IP) {
            ip = wifiIp
        }

        return ip
    }

    override fun onUriClicked(uri: Uri) {
        val url = uri.toString()
        val uris = mPost!!.postUris
        for (aUri in uris) {
            if (aUri.id == url) {
                handleExpectedUri(aUri)
                return
            }
        }

        handleUnexpectedUrl(uri)
    }

    // private methods

    override fun onViewPause() {
        if (mPostElements == null) return
        for (i in mPostElements!!.indices) {
            val postElement = mPostElements!![i]
            if (postElement is SingleHtmlElement) {
                adapter.setWebViewOnPause(i)
            }
        }
    }

    private fun checkForPausedWebView() {
        if (mPostElements == null) return
        for (i in mPostElements!!.indices) {
            val postElement = mPostElements!![i]
            if (postElement is SingleHtmlElement) {
                adapter.setWebViewOnResume(i)
            }
        }
    }

    private fun handleUnexpectedUrl(uri: Uri) {
        println(uri.scheme)
        when (uri.scheme) {
            "mailto" -> {
                view.onEmailClicked(uri.toString())
            }
            "https" -> {
                view.onExternalLinkClicked(uri.toString())
            }
            "http" -> {
                val uriS = uri.toString()
                if (uriS.contains("profile")) {
                    openAccount(uriS)
                }
            }
            else -> {
                cannotOpenUriMessage()
            }
        }
    }
    private fun openAccount(uri: String) {
        val account = getAccountFromUri(uri)
        if (account != "") view.openProfileActivityUi(account)
    }
    private fun getAccountFromUri(uri: String) : String {
        var res = ""
        val pattern = Pattern.compile("accountname=[A-Za-z]*\\\\[A-Za-z0-9_]*#")
        val matcher = pattern.matcher(uri)
        if (matcher.find()) {
            res = uri.substring(matcher.start(), matcher.end())
            res = res.replace("accountname=", "").replace("#", "")
        }

        return res

    }

    private fun showPost() {
        view.showPost()
    }

    private fun setCommentLikeStatus(newUserLikeStatus: Int, newLikes: Int, position: Int) {
        val comment = mPostElements!![position] as CommentElement
        comment.likes = newLikes
        comment.userLike = newUserLikeStatus
        adapter.setCommentLikeState(position, "$newLikes", newUserLikeStatus)
    }

    private fun setPostLikeStatus(newUserLikeStatus: Int, position: Int) {
        mPost!!.likesCount += if (newUserLikeStatus == 0) -1 else 1
//        if (newUserLikeStatus == 0) {
//            mPost!!.likesCount -= 1 //mPost!!.likesCount - 1
//        } else {
//            mPost!!.likesCount += 1//mPost!!.likesCount + 1
//        }
        mPost!!.currentUserLike = newUserLikeStatus
        adapter.setPostLikeState(position, "${mPost!!.likesCount}", newUserLikeStatus)
    }

    private fun handleExpectedUri(uri: PostInterface.PostUri) {
        val type = uri.type
        val value = uri.value
        when (type) {
            "user_profile" -> {
                if (!Strings.isNullOrEmpty(value)) {
                    val profileId = value.toLowerCase()
                    view.openProfileActivityUi(profileId)
                }
            }
            "external_reference" -> {
                view.onExternalLinkClicked(value)
            }
            "internal_page" -> {
                view.onInternalLinkClicked(value)
            }
            "internal_feed_news" -> {
                val title = ""
                val feedType = "news"
                view.openNewsActivityUi(title, feedType, value)
            }
            "internal_feed_blog" -> {
                val title = ""
                val feedType = "blog"
                view.openNewsActivityUi(title, feedType, value)
            }
            "internal_feed_community" -> {
                val title = ""
                val feedType = "community"
                view.openNewsActivityUi(title, feedType, value)
            }
        }
    }

    private fun cannotOpenUriMessage() {
        view.showSnackBar("Не могу загрузить эту ссылку, видимо, она сломана")
    }
}