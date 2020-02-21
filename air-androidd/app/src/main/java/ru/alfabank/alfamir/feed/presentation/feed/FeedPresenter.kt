package ru.alfabank.alfamir.feed.presentation.feed

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.common.base.Strings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import ru.alfabank.alfamir.Constants.Companion.ACTION_POST_SENDING
import ru.alfabank.alfamir.Constants.Companion.FEED_UPLOAD_AMOUNT
import ru.alfabank.alfamir.Constants.Companion.POST_IMAGE_LOADING_FAILED
import ru.alfabank.alfamir.Constants.Companion.POST_SENDING_SUCCESS
import ru.alfabank.alfamir.Constants.Companion.POST_SENT
import ru.alfabank.alfamir.Constants.Companion.SUBSCRIBE_TYPE_FEED
import ru.alfabank.alfamir.Constants.Companion.SUBSCRIBE_TYPE_POST
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_DP
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.base_elements.BaseFragment
import ru.alfabank.alfamir.data.source.remote.api.WebService
import ru.alfabank.alfamir.data.source.repositories.old_trash.SubscribeRepository
import ru.alfabank.alfamir.di.qualifiers.feed.HeaderVisibility
import ru.alfabank.alfamir.di.qualifiers.post.FeedID
import ru.alfabank.alfamir.di.qualifiers.post.FeedType
import ru.alfabank.alfamir.di.qualifiers.post.FeedUrl
import ru.alfabank.alfamir.favorites.data.source.repository.FavoriteRepository
import ru.alfabank.alfamir.feed.data.dto.HeaderRawOld
import ru.alfabank.alfamir.feed.data.source.repository.PostRepository
import ru.alfabank.alfamir.feed.presentation.feed.contract.FeedAdapterContract
import ru.alfabank.alfamir.feed.presentation.feed.contract.FeedContract
import ru.alfabank.alfamir.feed.presentation.feed.view_dummies.FeedAdapterDummy
import ru.alfabank.alfamir.feed.presentation.feed.view_dummies.FeedFragmentDummy
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.main.main_feed_fragment.contract.MainFeedListContract
import ru.alfabank.alfamir.post.data.dto.PostRaw
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract
import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper
import ru.alfabank.alfamir.utility.static_utilities.LinkHandler
import ru.alfabank.alfamir.utility.toBoolean
import javax.inject.Inject

open class FeedPresenter @Inject
constructor(private val mPostRepository: PostRepository,
            private val mGetImage: GetImage,
            private val favoriteRepository: FavoriteRepository,
            private val mSubscribeRepository: SubscribeRepository,
            private val mLogger: LoggerContract.Provider,
            @param:FeedID private val mFeedId: String,
            @param:FeedUrl private val mFeedUrl: String,
            @param:FeedType private val mFeedType: String,
            @param:HeaderVisibility private val mShowFeedHeader: Boolean) : FeedContract.Presenter {

    private var mView: FeedContract.View? = null
    private var mAdapter: FeedAdapterContract.Adapter? = null
    private var isDataLoaded: Boolean = false
    private var mIsSubscribed: Int = 0
    private var isPostLikeBeingProcessed: Boolean = false
    private var mIsLoading: Boolean = false
    private var isTouchEnabled: Boolean = false
    private var mFirstLoad: Boolean = false
    private val mCompositeDisposable = CompositeDisposable()
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var isFavorite = false
    private lateinit var context: Context

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent?.action != ACTION_POST_SENDING) {
                return
            }

            val message = when (intent.getStringExtra("result")) {
                POST_SENDING_SUCCESS -> {
                    onListRefresh()
                    null
                }
                POST_SENT -> context?.getString(R.string.post_sent)
                POST_IMAGE_LOADING_FAILED -> context?.getString(R.string.post_sending_error)
                else -> null
            }
            view.showSnackBar(message)
        }
    }

    override fun takeView(view: FeedContract.View) {
        mView = view

        if (!isDataLoaded && !mFirstLoad) {
            getView().showProgressBar()
            loadFeed()
        }

        if (view is BaseFragment) {
            view.context?.also { context ->
                LocalBroadcastManager.getInstance(context).registerReceiver(receiver, IntentFilter(ACTION_POST_SENDING))
                this.context = context
            }
        }
    }

    override fun dropView() {
        if (mView is BaseFragment) {
            mView?.also {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
            }
        }
        mView = null
        mCompositeDisposable.dispose()
        job.cancel()
    }

    override fun getView(): FeedContract.View {
        return if (mView == null) FeedFragmentDummy() else mView!!
    }

    override fun onListRefresh() {
        loadFeed()
    }

    private fun loadFeed() {

        mFirstLoad = true
        isTouchEnabled = false
        mCompositeDisposable.add(
                mPostRepository.getFeedNew(mFeedId, mFeedUrl, mFeedType, "", FEED_UPLOAD_AMOUNT, mShowFeedHeader)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            isDataLoaded = true
                            view.hideProgressBar()
                            showFeed()
                            isTouchEnabled = true
                        }, { view.hideProgressBar() }))
    }

    override fun onLoadMore() {
        if (mIsLoading) return
        mIsLoading = true
        view.showProgressBar()
        val timeStamp = mPostRepository.getLastPostPubTimeMinusOneSecond(mFeedId)
        mCompositeDisposable.add(
                mPostRepository.getFeedNew(mFeedId, mFeedUrl, mFeedType, timeStamp, FEED_UPLOAD_AMOUNT, false)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            adapter.onItemsInserted(mPostRepository.getCachedPostsListSize(mFeedId) - it.size, it.size)
                            mIsLoading = false
                            view.hideProgressBar()
                        }, {
                            mIsLoading = false // todo add "the end" state
                            view.hideProgressBar()
                        }))
    }

    private fun showFeed() {
        view.showFeed()
    }

    /**
     * PostAdapterContract methods
     */

    override fun bindListRowPost(position: Int, rowView: MainFeedListContract.PostRowView) {
        val post = mPostRepository.getCachedPostAtPosition(mFeedId, position) as PostRaw
        val author = post.author
        var authorName = ""
        var authorInitials = ""
        var unformattedAvatarUrl = ""
        if (author != null) {
            authorName = post.author.name // TODO exception
            authorInitials = InitialsMaker.formInitials(authorName)
            unformattedAvatarUrl = post.author.picLink
        }
        val date = post.date
        val headingTitle = post.headingTitle
        val title = post.title
        val body = post.shortText
        val unformattedPostImageUrl = post.imageUrl
        val options = post.options
        val likesEnabled = options.likesEnabled
        val commentsEnabled = options.commentsEnabled
        val optionsMenuEnabled = options.optionsMenuEnabled
        val headingVisible = options.headingVisible
        val titleVisible = options.titleVisible
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
        if (headingVisible == 1) {
            rowView.setHeadingTitle(headingTitle)
        } else {
            rowView.hideHeadingTitle()
        }
        if (titleVisible == 1) {
            rowView.setPostTitle(title)
        } else {
            rowView.hidePostTitle()
        }

        if (Strings.isNullOrEmpty(body)) {
            rowView.hidePostBody()
        } else {
            rowView.showPostBody()
            rowView.setPostBody(body)
        }
        rowView.setPostDate(date)
    }

    private fun handleHeaderProfileUrl(rowView: FeedAdapterContract.HeaderRowView, unformattedProfileImageUrl: String, position: Int) {

        uiScope.launch {
            mCompositeDisposable.add(mGetImage.bitmap(unformattedProfileImageUrl, 48)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ rowView.setAuthorImage(it, true) },
                            Throwable::printStackTrace))
        }
    }

    private fun handleHeaderCoverUrl(rowView: FeedAdapterContract.HeaderRowView, unformattedCoverUrl: String, position: Int) {

        uiScope.launch {
            mCompositeDisposable.add(mGetImage.bitmap(unformattedCoverUrl, SCREEN_WIDTH_DP)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ rowView.setBackgroundImage(it, true) }, Throwable::printStackTrace))
        }
    }

    override fun bindListRowHeader(position: Int, rowView: FeedAdapterContract.HeaderRowView) {
        val header = mPostRepository.getCachedPostAtPosition(mFeedId, position) as HeaderRawOld

        val unformattedCoverUrl = header.cover
        val unformattedProfileUrl = header.profilePic
        val tempCover = header.tempCover
        val description = header.description
        val title = header.title
        mIsSubscribed = header.isSubscribed
        val isPersonal = header.isPersonal

        rowView.setFeedTitle(title)
        if (Strings.isNullOrEmpty(description)) {
            rowView.hideDescription()
        } else {
            rowView.setFeedDescription(description)
        }

        if (mIsSubscribed == 0 || mIsSubscribed == 1) {
            rowView.setSubscriptionStateButtonVisibility(true)
            rowView.setSubscriptionState(mIsSubscribed)
        } else {
            rowView.setSubscriptionStateButtonVisibility(false)
        }

        if (isPersonal == 0)
            rowView.hideAuthorImage()
        else
            handleHeaderProfileUrl(rowView, unformattedProfileUrl, position)

        if (Strings.isNullOrEmpty(tempCover)) {

        } else {
            uiScope.launch {
                var bitmap: Bitmap? = null
                withContext(Dispatchers.IO) {
                    val bytes = Base64.decode(tempCover, Base64.DEFAULT)
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                }
                rowView.setBackgroundImage(bitmap, true)
            }
        }
        handleHeaderCoverUrl(rowView, unformattedCoverUrl, position)
    }

    override fun onPostClicked(position: Int) {
        val post = mPostRepository.getCachedPostAtPosition(mFeedId, position) as PostRaw
        val postId = post.id
        view.openPostUi(mFeedId, postId, mFeedUrl, mFeedType, position, false)
    }

    override fun onCommentsClicked(position: Int) {
        val post = mPostRepository.getCachedPostAtPosition(mFeedId, position) as PostRaw
        val postId = post.id
        view.openPostUi(mFeedId, postId, mFeedUrl, mFeedType, position, true)
    }

    override fun onAuthorClicked(position: Int) {
        val post = mPostRepository.getCachedPostAtPosition(mFeedId, position) as PostRaw
        val id = post.author.id
        view.openProfileUi(id)
    }

    override fun onLikeClicked(position: Int) {
        if (isPostLikeBeingProcessed)
            return
        else
            isPostLikeBeingProcessed = true
        val post = mPostRepository.getCachedPostAtPosition(mFeedId, position) as PostRaw
        val postId = post.id
        val feedUrl = post.postUrl
        val feedType = post.type
        val feedId = post.postUrl
        val likes = post.likes
        val newLikes: Int
        val currentUserLikeStatus = post.currentUserLike
        val newUserLikeStatus: Int
        if (currentUserLikeStatus == 0) {
            newUserLikeStatus = 1
            newLikes = likes + 1
        } else {
            newUserLikeStatus = 0
            newLikes = likes - 1
        }
        setPostLikeStatus(newLikes, newUserLikeStatus, position)
        mCompositeDisposable.add(
                mPostRepository.likePostTest(postId, feedUrl, feedType, newUserLikeStatus, feedId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            isPostLikeBeingProcessed = false
                        }, {
                            setPostLikeStatus(likes, currentUserLikeStatus, position)
                            isPostLikeBeingProcessed = false
                        }))
    }

    override fun onOptionsClicked(position: Int) {
        val post = mPostRepository.getCachedPostAtPosition(mFeedId, position) as PostRaw
        val postId = post.id
        val neverUsed = favoriteRepository.isFavorite(post.postUrl, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ v ->
                    isFavorite = v.isFavorite.toBoolean()
                    val favoriteStatus = if (isFavorite) "Удалить из избранного" else "Добавить в избранное"
                    val subscribeStatus = if (mSubscribeRepository.checkIfSubscribed(postId)) "Отписаться" else "Подписаться"
                    val isDeletable = post.isDeletable
                    adapter.onPostOptionsClicked(position, isDeletable, favoriteStatus, subscribeStatus)
                },
                        Throwable::printStackTrace)
    }

    override fun onMenuItemDeleteClicked(position: Int) {
        val post = mPostRepository.getCachedPostAtPosition(mFeedId, position) as PostRaw
        val postId = post.id
        val postType = post.type
        val postUrl = post.postUrl
        mPostRepository.deletePost(postType, postUrl, postId) {
            mPostRepository.deleteCachedPostAtPosition(mFeedId, position)
            adapter.onItemRemoved(position)
        }
    }

    override fun onMenuItemFavoriteClicked(position: Int) {
        val post = mPostRepository.getCachedPostAtPosition(mFeedId, position) as PostRaw
        val postId = post.id
        val feedType = post.type
        val feedUrl = post.postUrl

        if (isFavorite) {
            favoriteRepository.removeFavoritePage(postId)
        } else {
            favoriteRepository.addFavoritePage(feedUrl, feedType, postId)
        }
    }

    override fun onMenuItemSubscribeClicked(position: Int) {
        val post = mPostRepository.getCachedPostAtPosition(mFeedId, position) as PostRaw
        val postId = post.id
        val postUrl = post.postUrl
        val threadId = post.threadId
        val postType = post.type
        val postTitle = post.title
        if (mSubscribeRepository.checkIfSubscribed(post.id)) {
            mSubscribeRepository.unsubscribeNew(SUBSCRIBE_TYPE_POST, postId, postUrl, threadId, postType, object : WebService.WebRequestListener {})
        } else {
            mSubscribeRepository.subscribeNew(SUBSCRIBE_TYPE_POST,
                    postId, postUrl, threadId, postType, object : WebService.WebRequestListener {})
        }
    }

    override fun getListSize(): Int {
        return mPostRepository.getCachedPostsListSize(mFeedId)
    }

    override fun getItemId(position: Int): Long {
        val post = mPostRepository.getCachedPostAtPosition(mFeedId, position) as PostRaw
        return 0
    }

    override fun getItemViewType(position: Int): Int {
        return mPostRepository.getItemViewType(mFeedId, position)
    }

    override fun takeListAdapter(adapter: FeedAdapterContract.Adapter) {
        mAdapter = adapter
    }

    override fun getAdapter(): FeedAdapterContract.Adapter {
        return if (mAdapter == null) FeedAdapterDummy() else mAdapter!!
    }

    private fun handlePostImageUrl(rowView: MainFeedListContract.PostRowView, unformattedPostImageUrl: String, position: Int) {
        if (Strings.isNullOrEmpty(unformattedPostImageUrl)) {
            rowView.setPostImageSizeParameters(0)
            return
        }
        val layoutHeight = LinkHandler.getPhotoPixelHeightMatchingScreenWidth(unformattedPostImageUrl)
        rowView.setPostImageSizeParameters(layoutHeight)
        uiScope.launch {
            mCompositeDisposable.add(mGetImage.bitmap(unformattedPostImageUrl, SCREEN_WIDTH_DP)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ rowView.setPostImage(it, true) }, Throwable::printStackTrace))
        }
    }

    private fun handleAvatarUrl(rowView: MainFeedListContract.PostRowView, unformattedAvatarUrl: String, position: Int, initials: String) {
        if (unformattedAvatarUrl.isBlank()) {
            rowView.setAuthorPlaceholder(initials)
            return
        }
        uiScope.launch {
            mCompositeDisposable.add(mGetImage.bitmap(unformattedAvatarUrl, 48)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ rowView.setAuthorAvatar(it, true) },
                            Throwable::printStackTrace))
        }
    }

    private fun setPostLikeStatus(newLikes: Int, newUserLikeStatus: Int, position: Int) {
        adapter.setLikeStatus(position, "" + newLikes, newUserLikeStatus)
    }

    override fun onSubscribeClicked(position: Int) {
        if (mIsSubscribed == 0) {
            val subscribeType = SUBSCRIBE_TYPE_FEED
            mSubscribeRepository.subscribeNew(subscribeType, "", mFeedUrl, "", mFeedType, object : WebService.WebRequestListener {
                override fun onResponse(jsonWrapper: JsonWrapper, responseType: Int) {
                    mIsSubscribed = 1
                    adapter.setSubscriptionStateChanged(mIsSubscribed)
                    val message = "Вы подписались"
                    view.showSnackBar(message)
                }
            })
        } else {
            val subscribeOption = "Отписаться"
            adapter.onSubscribeOptionClicked(subscribeOption, position)
        }
    }

    override fun onSubscribeOptionClicked(position: Int) {
        val subscribeType = SUBSCRIBE_TYPE_FEED
        mSubscribeRepository.unsubscribeNew(subscribeType, "", mFeedUrl, "", mFeedType, object : WebService.WebRequestListener {
            override fun onResponse(jsonWrapper: JsonWrapper, responseType: Int) {
                mIsSubscribed = 0
                adapter.setSubscriptionStateChanged(mIsSubscribed)
                val message = "Вы отписались"
                view.showSnackBar(message)
            }
        })
    }


}
