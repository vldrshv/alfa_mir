package ru.alfabank.alfamir.favorites.presentation.favorite_post

import android.annotation.SuppressLint
import com.google.common.base.Strings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.favorites.data.source.repository.FavoriteRepository
import ru.alfabank.alfamir.favorites.domain.usecase.GetFavoritePostList
import ru.alfabank.alfamir.favorites.presentation.dto.FavoritePost
import ru.alfabank.alfamir.favorites.presentation.favorite_post.contract.FavoritePostAdapterContract
import ru.alfabank.alfamir.favorites.presentation.favorite_post.contract.FavoritePostContract
import ru.alfabank.alfamir.favorites.presentation.favorite_post.dummy_view.FavoritePostViewDummy
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import javax.inject.Inject

class FavoritePostPresenter
@Inject
internal constructor(
        private val mGetFavoritePostList: GetFavoritePostList,
        private val favoriteRepository: FavoriteRepository,
        val mGetImage: GetImage) : FavoritePostContract.Presenter {

    private var mView: FavoritePostContract.View? = null
    private var mAdapter: FavoritePostAdapterContract.Adapter? = null
    private val mCompositeDisposable = CompositeDisposable()
    private var mFavoritePosts: MutableList<FavoritePost> = mutableListOf()
    private var mIsDataLoaded: Boolean = false
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun takeListAdapter(adapter: FavoritePostAdapterContract.Adapter) {
        mAdapter = adapter
    }

    override fun getAdapter(): FavoritePostAdapterContract.Adapter {
        return if (mAdapter == null)
            FavoritePostViewDummy()
        else
            mAdapter!!
    }

    override fun takeView(view: FavoritePostContract.View) {
        mView = view
        getView().showLoadingProgress()
        loadFavoritePageList()
    }

    override fun dropView() {
        mView = null
        mAdapter = null
        mCompositeDisposable.dispose()
        job.cancel()
    }

    override fun getView(): FavoritePostContract.View {
        return if (mView == null) {
            FavoritePostViewDummy()
        } else mView!!
    }

    private fun loadFavoritePageList() {
        mCompositeDisposable.add(mGetFavoritePostList.execute(GetFavoritePostList.RequestValues(false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseValue ->
                    mIsDataLoaded = true
                    mFavoritePosts = responseValue.getmFavoritePosts()
                    view.showFavoriteList()
                    view.hideLoadingProgress()
                }, { view.hideLoadingProgress() }))
    }

    override fun getListSize(): Int {
        return mFavoritePosts.size
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun bindListRowPage(rowView: FavoritePostAdapterContract.PageRowView, position: Int) {
        val favoritePost = mFavoritePosts[position]
        val title = favoritePost.title
        val pubDate = favoritePost.pubDate
        val imageUrl = favoritePost.imageUrl

        rowView.clearData()
        rowView.setDate(pubDate)
        rowView.setTitle(title)
        if (Strings.isNullOrEmpty(imageUrl)) {
            rowView.hideImage()
        } else {
            rowView.showImage()

            uiScope.launch {
                mCompositeDisposable.add(mGetImage.bitmap(imageUrl, 80)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            rowView.setImage(it, true)
                        }, Throwable::printStackTrace))
            }
        }
    }

    override fun onItemClicked(position: Int) {
        val favoritePost = mFavoritePosts[position]
        val feedId = favoritePost.feedUrl
        val postId = favoritePost.postId
        val feedUrl = favoritePost.feedUrl
        val feedType = favoritePost.feedType
        mView!!.openPost(feedId, postId, feedUrl, feedType)
    }

    override fun onSearchClicked() {
        mView!!.openSearchUi()
    }

    @SuppressLint("CheckResult")
    override fun delete(position: Int) {
        val post = mFavoritePosts[position]
        favoriteRepository.removeFavoritePage(post.postId)
        mFavoritePosts.removeAt(position)
    }
}
