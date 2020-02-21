package ru.alfabank.alfamir.main.menu_fragment.presentation

import android.util.Log
import com.google.common.base.Strings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.BuildConfig
import ru.alfabank.alfamir.Constants.Initialization.USER_LOGIN
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.main.menu_fragment.domain.usecase.CheckVersion
import ru.alfabank.alfamir.main.menu_fragment.presentation.contract.MenuFragmentContract
import ru.alfabank.alfamir.main.menu_fragment.presentation.dummy_view.MenuViewDummy
import ru.alfabank.alfamir.profile.domain.usecase.GetProfile
import ru.alfabank.alfamir.utility.logging.local.LogWrapper
import javax.inject.Inject

class MenuPresenter
@Inject
internal constructor(private val mGetProfile: GetProfile,
                     private val mLogWrapper: LogWrapper,
                     private val mGetImage: GetImage,
                     private val mCheckVersion: CheckVersion) : MenuFragmentContract.Presenter {

    private var mView: MenuFragmentContract.View? = null
    private val mCompositeDisposable = CompositeDisposable()
    private var mNewFeatureDescription: String? = null
    private var mCommentOnVersionNumber: String? = null
    private var mVersionNumber: String? = null

    override fun takeView(view: MenuFragmentContract.View) {
        mView = view
        showProfile()
        if (Strings.isNullOrEmpty(mNewFeatureDescription) && Strings.isNullOrEmpty(mVersionNumber)) {
            showAppInfo()
        }
    }

    private fun showAppInfo() {
        mCompositeDisposable.add(
                mCheckVersion.execute()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ responseValue ->
                            mNewFeatureDescription = responseValue.newFeatureDescription
                            mVersionNumber = responseValue.versionNumber
                            mCommentOnVersionNumber = responseValue.commentOnVersionNumber
                            val comment = if (mVersionNumber == BuildConfig.VERSION_NAME) mCommentOnVersionNumber else String.format("Доступна версия %s", mVersionNumber)
                            view.setAppVersionComment(comment)
                            view.setAppVersion(BuildConfig.VERSION_NAME)
                        }, { e -> Log.e("AppVersion", e.message) }))
    }

    private fun showProfile() {
        mCompositeDisposable.add(
                mGetProfile.execute(USER_LOGIN)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({profile ->
                            val name = profile.name
                            val position = profile.position
                            mView!!.setUserName(name)
                            mView!!.setUserPosition(position)
                            val picUrl = profile.picUrl
                            CoroutineScope(Dispatchers.Main).launch {
                                mCompositeDisposable.add(
                                        mGetImage.bitmap(picUrl, 48)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe({
                                                    mView!!.setUserPic(it, true)
                                                }, Throwable::printStackTrace)
                                )
                            }
                        }, Throwable::printStackTrace)
        )
    }

    override fun dropView() {
        mView = null
        mCompositeDisposable.dispose()
    }

    override fun getView(): MenuFragmentContract.View {
        return if (mView == null) MenuViewDummy() else mView!!
    }

    override fun onProfileClicked() {
        mCompositeDisposable.add(mGetProfile.execute(USER_LOGIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseValue ->
                    val userLogin = responseValue.login
                    view.openProfileActivityUi(userLogin)
                }, Throwable::printStackTrace)
        )
    }

    override fun onSettingsClicked() {

    }
}
