package ru.alfabank.alfamir.main.home_fragment.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.fragment.app.Fragment
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import ru.alfabank.alfamir.Constants.Initialization.IS_NEW_YEAR_COME
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_DP
import ru.alfabank.alfamir.Constants.Initialization.UPDATE_AVAILABLE
import ru.alfabank.alfamir.Constants.Initialization.USER_LOGIN
import ru.alfabank.alfamir.Constants.Profile.USER_NAME
import ru.alfabank.alfamir.Constants.RequestCode.CALL_HD
import ru.alfabank.alfamir.Constants.RequestCode.CALL_HH
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.main.home_fragment.domain.usecase.GetTopNews
import ru.alfabank.alfamir.main.home_fragment.presentation.contract.HomeFragmentContract
import ru.alfabank.alfamir.main.home_fragment.presentation.contract.TopNewsAdapterContract
import ru.alfabank.alfamir.main.home_fragment.presentation.dto.TopNews
import ru.alfabank.alfamir.main.home_fragment.presentation.dummy_view.HomeFragmentViewDummy
import ru.alfabank.alfamir.main.menu_fragment.domain.usecase.CheckVersion
import ru.alfabank.alfamir.profile.domain.usecase.GetProfile
import ru.alfabank.alfamir.utility.logging.local.LogWrapper
import ru.alfabank.alfamir.utility.toBoolean
import javax.inject.Inject

class HomePresenter @Inject
internal constructor(private val mGetProfile: GetProfile,
                     private val mGetTopNews: GetTopNews,
                     private val mGetImage: GetImage,
                     logWrapper: LogWrapper,
                     private val mCheckVersion: CheckVersion,
                     private val mSharedPreferences: SharedPreferences) : HomeFragmentContract.Presenter {

    private var mView: HomeFragmentContract.View? = null
    private var mAdapter: TopNewsAdapterContract.Adapter? = null
    private val mCompositeDisposable = CompositeDisposable()
    private var mTopNews: List<TopNews>? = null
    private val mIsSantaChecked: Boolean = false
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun takeView(view: HomeFragmentContract.View) {
        mView = view
        if (!mIsSantaChecked) checkSecretSanta()
        showHomePage()
    }

    private fun showHomePage() {
        loadProfile()
        loadTopNews()
        showAppInfo()
    }

    private fun checkSecretSanta() {
        if (IS_NEW_YEAR_COME)
            view.setSantaIcon()
    }

    @SuppressLint("LogNotTimber")
    private fun showAppInfo() {
        mCompositeDisposable.add(
                mCheckVersion.execute()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ version ->
                            val update = version.updateAvailable.toBoolean()
                            mView?.update(update)
                            mSharedPreferences.edit().putBoolean(UPDATE_AVAILABLE, update).apply()
                        }, { e -> Log.e("AppVersion", e.message) }))
    }

    private fun loadProfile() {
        mCompositeDisposable.add(mGetProfile.execute(USER_LOGIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ profile ->
                    saveString(USER_NAME, profile.name)
                    USER_LOGIN = profile.login
                    val picUrl = profile.picUrl
                    uiScope.launch {
                        withContext(Dispatchers.IO) {
                            mGetImage.bitmap(picUrl, 47)
                                    .subscribe({ bitmap -> view.setUserPic(bitmap, false) }, Throwable::printStackTrace)
                        }
                    }
                }, Throwable::printStackTrace)
        )
    }

    private fun loadTopNews() {
        mCompositeDisposable.add(mGetTopNews.execute(GetTopNews.RequestValues(3))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseValue ->
                    mTopNews = responseValue.topNews
                    view.hideTempBackground()
                    view.showTopNews()
                }, Throwable::printStackTrace))
    }

    override fun dropView() {
        mView = null
        mCompositeDisposable.dispose()
        job.cancel()
    }

    override fun getView(): HomeFragmentContract.View {
        return if (mView == null) HomeFragmentViewDummy() else mView!!
    }

    override fun onProfileClicked() {
        view.openProfileUi(USER_LOGIN)
    }

    override fun onNotificationClicked() {
        view.openNotificationUi()
    }

    override fun onSearchClicked() {
        view.openSearchUi()
    }

    private fun checkCallPermission(phoneType: Int): Boolean {
        val isCallPermissionGranted = view.checkCallPermission()
        if (!isCallPermissionGranted) {
            view.requestCallPermission(phoneType)
        }
        return isCallPermissionGranted
    }

    private fun extraChargeWarning(): Boolean {
        val isWarningAccepted = mSharedPreferences.getBoolean("mobile_warning_checkbox", false)
        if (!isWarningAccepted) {
            view.showWarningOnExtraCharge()
        }
        return isWarningAccepted
    }

    private fun justCallHH() {
        view.makeCall("88007077227")
    }

    private fun justCallHD() {
        view.makeCall("84956209221")
    }

    override fun onCallHumanHelpClicked() {

        if (checkCallPermission(CALL_HH)) {
            justCallHH()
        }
    }

    override fun saveCallParameter(accept: Boolean) {
        mSharedPreferences.edit().putBoolean("mobile_warning_checkbox", accept).apply()
        justCallHD()
    }

    override fun onCallHumanDeskClicked() {

        if (checkCallPermission(CALL_HD)) {
            if (extraChargeWarning()) {
                justCallHD()
            }
        }
    }

    override fun showHomo() {
        val ctx = (mView as androidx.fragment.app.Fragment).context
        try {
            ctx?.also { context ->
                val intent = context.packageManager?.getLaunchIntentForPackage("ru.alfabank.tokenagent")
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://sap.alfabank.ru"))
            ctx?.also {
                it.startActivity(intent)
            }
        }
    }

    /**
     * TopNewsAdapterContract.Presenter methods
     */

    override fun bindListRowTopNews(position: Int, rowView: TopNewsAdapterContract.TopNewsRowView) {
        val topNews = mTopNews!![position]
        val title = topNews.title
        val picUrl = topNews.picUrl
        rowView.setTitle(title)
        uiScope.launch {
            (mGetImage.bitmap(picUrl, SCREEN_WIDTH_DP)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        rowView.setPicture(it, true)
                    }, Throwable::printStackTrace))
        }
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getListSize(): Int {
        return mTopNews!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun takeListAdapter(adapter: TopNewsAdapterContract.Adapter) {
        mAdapter = adapter
    }

    override fun getAdapter(): TopNewsAdapterContract.Adapter {
        return if (mAdapter == null) HomeFragmentViewDummy() else mAdapter!!
    }

    override fun onDataLoaded() {
        view.showCalendarEventListCards()
    }

    override fun onItemClicked(position: Int) {
        val newsItem = mTopNews!![position]
        val url = newsItem.url
        view.openPost(url)
    }

    private fun saveString(key: String, data: String) {
        mSharedPreferences
                .edit()
                .putString(key, data)
                .apply()
    }
}
