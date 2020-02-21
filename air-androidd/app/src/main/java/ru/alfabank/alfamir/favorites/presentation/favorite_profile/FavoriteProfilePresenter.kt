package ru.alfabank.alfamir.favorites.presentation.favorite_profile

import android.annotation.SuppressLint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.favorites.domain.usecase.GetFavoriteProfileList
import ru.alfabank.alfamir.favorites.domain.usecase.RemoveFavoritePerson
import ru.alfabank.alfamir.favorites.presentation.dto.FavoriteProfile
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract.FavoriteProfileAdapterContract
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract.FavoriteProfileContract
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.dummy_view.FavoriteProfileViewDummy
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker
import javax.inject.Inject

class FavoriteProfilePresenter
@Inject
internal constructor(private val mGetFavoriteProfileList: GetFavoriteProfileList,
                     private val mRemoveFavoritePerson: RemoveFavoritePerson,
                     private val mGetImage: GetImage) : FavoriteProfileContract.Presenter {

    private var mView: FavoriteProfileContract.View? = null
    private var mAdapter: FavoriteProfileAdapterContract.Adapter? = null
    private val mCompositeDisposable = CompositeDisposable()
    private var mFavoriteProfiles: MutableList<FavoriteProfile> = mutableListOf()
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private fun loadFavoriteProfileList() {
        mCompositeDisposable.add(mGetFavoriteProfileList.execute(GetFavoriteProfileList.RequestValues())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseValue ->
                    mFavoriteProfiles = responseValue.getmFavoriteProfiles()
                    view.showFavoriteList()
                    view.hideLoadingProgress()
                }, { view.hideLoadingProgress() }))
    }

    override fun getListSize(): Int {
        return mFavoriteProfiles.size
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun takeListAdapter(adapter: FavoriteProfileAdapterContract.Adapter) {
        mAdapter = adapter
    }

    override fun getAdapter(): FavoriteProfileAdapterContract.Adapter {
        return if (mAdapter == null) FavoriteProfileViewDummy() else mAdapter!!
    }

    override fun takeView(view: FavoriteProfileContract.View) {
        mView = view
        getView().showLoadingProgress()
        loadFavoriteProfileList()
    }

    override fun dropView() {
        mView = null
        mAdapter = null
        mCompositeDisposable.dispose()
        job.cancel()
    }

    override fun getView(): FavoriteProfileContract.View {
        return if (mView == null) FavoriteProfileViewDummy() else mView!!
    }

    override fun bindListRowProfile(rowView: FavoriteProfileAdapterContract.ProfileRowView, position: Int) {
        val favoriteProfile = mFavoriteProfiles[position]
        val imageUrl = favoriteProfile.imageUrl
        val name = favoriteProfile.name
        val title = favoriteProfile.title
        rowView.setName(name)
        rowView.setTitle(title)
        if (imageUrl.isNullOrEmpty()) {
            val initials = InitialsMaker.formInitials(name)
            rowView.setPlaceHolder(initials)
        } else {

            uiScope.launch {
                mCompositeDisposable.add(mGetImage.bitmap(imageUrl, 80)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ rowView.setImage(it, true) }, Throwable::printStackTrace))
            }
        }
    }

    override fun onItemClicked(position: Int) {
        val favoriteProfile = mFavoriteProfiles[position]
        val login = favoriteProfile.login
        mView!!.openProfileActivityUi(login)
    }

    override fun onSearchClicked() {
        mView!!.openSearchUi()
    }

    @SuppressLint("CheckResult")
    override fun delete(position: Int) {
        val profile = mFavoriteProfiles[position]
        mRemoveFavoritePerson.execute(profile.login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {})
        mFavoriteProfiles.removeAt(position)

    }
}