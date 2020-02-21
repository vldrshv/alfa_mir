package ru.alfabank.alfamir.people.presentation

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.Constants
import ru.alfabank.alfamir.data.dto.ShortProfile
import ru.alfabank.alfamir.image.data.source.repository.ImageRepository
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.people.presentation.dummy_view.PeopleViewDummy
import ru.alfabank.alfamir.people.presentation.post_contract.PeopleContract
import ru.alfabank.alfamir.profile.data.source.repository.ProfileDataSource
import ru.alfabank.alfamir.profile.data.source.repository.ProfileRepository
import ru.alfabank.alfamir.profile.domain.usecase.GetProfile
import ru.alfabank.alfamir.profile.domain.usecase.SetLike
import ru.alfabank.alfamir.profile.presentation.dto.Profile
import javax.inject.Inject


class PeoplePresenter @Inject
internal constructor(private val mUserLogin: String,
                     private val mGetImage: GetImage,
                     private val mProfileRepository: ProfileRepository,
                     private val mImageRepository: ImageRepository,
                     private val mGetProfile: GetProfile,
                     private val mSetLike: SetLike) : PeopleContract.Presenter {
    private var mProfile: Profile? = null
    private var mView: PeopleContract.View? = null
    private val mCompositeDisposable = CompositeDisposable()

    override fun takeView(view: PeopleContract.View) {
        mView = view
        loadList()
    }

    override fun dropView() {
        mCompositeDisposable.dispose()
        mView = null
    }

    override fun getView(): PeopleContract.View {
        return if (mView == null) {
            PeopleViewDummy()
        } else mView!!
    }

    override fun refreshList() {
        mProfileRepository.refreshShortProfiles()
        loadList()
    }

    override fun like() {
        val targetLikeStatus = 1
        mCompositeDisposable.add(
                mSetLike.execute(SetLike.RequestValues(mUserLogin, targetLikeStatus))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            mProfileRepository.refreshProfile()
                            mProfileRepository.refreshShortProfiles()
                            view.dismiss()
                        }, Throwable::printStackTrace)
        )
    }

    private fun showList(profiles: List<ShortProfile>) {
        view.showProfiles(profiles)
    }

    private fun showEmptyState() {
        val isMyProfile = mUserLogin == Constants.Initialization.USER_LOGIN
        mCompositeDisposable.add(
                mGetProfile.execute(mUserLogin)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ responseValue ->
                            mProfile = responseValue
                            val url = mProfile!!.picUrl
                            CoroutineScope(Dispatchers.Main).launch {
                                mCompositeDisposable.add(mGetImage.bitmap(url, 48)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ view.showEmptyState(isMyProfile, it) }, Throwable::printStackTrace))
                            }
                        }, Throwable::printStackTrace)
        )
    }

    private fun loadList() {
        view.showProgressBar()
        mProfileRepository.getProfiles(mUserLogin, object : ProfileDataSource.LoadProfilesCallback {
            override fun onProfilesLoaded(profiles: List<ShortProfile>) {
                view.hideProgressBar()
                if (profiles.isEmpty()) {
                    showEmptyState()
                } else {
                    showList(profiles)
                }
            }

            override fun onDataNotAvailable() {}
        })
    }

}
