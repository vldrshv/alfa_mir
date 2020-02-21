package ru.alfabank.alfamir.alfa_tv.presentation.show

import android.text.Editable
import com.google.common.base.Strings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_3
import ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_7
import ru.alfabank.alfamir.Constants.Show.SHOW
import ru.alfabank.alfamir.Constants.Show.SHOW_CURRENT
import ru.alfabank.alfamir.alfa_tv.data.dto.SavedPassword
import ru.alfabank.alfamir.alfa_tv.data.source.repository.password.PasswordDataSource
import ru.alfabank.alfamir.alfa_tv.domain.usecase.GetShow
import ru.alfabank.alfamir.alfa_tv.domain.usecase.GetShowCurrentState
import ru.alfabank.alfamir.alfa_tv.domain.usecase.GetShowUrl
import ru.alfabank.alfamir.alfa_tv.presentation.dto.Show
import ru.alfabank.alfamir.alfa_tv.presentation.show.contract.ShowContract
import ru.alfabank.alfamir.alfa_tv.presentation.show.dummy_view.ShowViewDummy
import ru.alfabank.alfamir.di.qualifiers.ShowId
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter
import javax.inject.Inject

class ShowPresenter @Inject
internal constructor(@param:ShowId private val mShowId: Int,
                     private val mGetShow: GetShow,
                     private val mGetShowUrl: GetShowUrl,
                     private val mGetImage: GetImage,
                     private val mGetShowCurrentState: GetShowCurrentState,
                     private val mDateConverter: DateFormatter,
                     private val mPasswordDataSource: PasswordDataSource) : ShowContract.Presenter {
    private var mIsOnAir: Int = 0
    private var mShow: Show? = null

    private var mView: ShowContract.View? = null
    private val mCompositeDisposable = CompositeDisposable()
    private var mIsPortrait: Boolean = false

    override fun takeView(view: ShowContract.View) {
        mView = view
    }

    override fun setOrientation(isPortrait: Boolean) {
        mIsPortrait = isPortrait
        if (mIsPortrait) view.hideSHowInfoView()
        loadShow()
    }

    private fun loadShow() {
        mCompositeDisposable.add(mGetShow.execute(GetShow.RequestValues(mShowId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { responseValue ->
                    mShow = responseValue.show
                    mGetShowCurrentState.execute(GetShowCurrentState.RequestValues(mShow!!.id))
                            .subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseValue ->
                    mIsOnAir = responseValue.isIsOnAir

                    mIsOnAir = 1 // TODO for test

                    if (mIsOnAir != mShow!!.isOnAir) mShow!!.isOnAir = mIsOnAir
                    if (mIsPortrait) {
                        showShowPortrait()
                    } else {
                        showShowLandscape()
                    }
                }, {
                    if (mIsPortrait) {
                        view.showSnackBar("Что-то пошло не так. Попробуйте позднее")
                    } else {
                        view.setPortraitOrientation()
                    }

                }))
    }

    private fun showShowLandscape() {
        val isPasswordRequired = mShow!!.isPasswordRequired
        val isPasswordEntered = mShow!!.isPasswordEntered
        if (isPasswordRequired == 0) {
            getShowUrl("")
        } else {
            if (isPasswordEntered) {
                getShowUrl(mShow!!.password)
            } else {
                view.setPortraitOrientation()
            }
        }
    }

    private fun showShowPortrait() {
        showShowInfo()
        showShowStatus()
        view.showShowInfoView()
    }

    private fun getShowUrl(password: String) {
        mCompositeDisposable.add(mGetShowUrl.execute(GetShowUrl.RequestValues(mShowId, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseValue ->
                    val url = responseValue.showUri.uri
                    view.initializePlayer(url)
                }, Throwable::printStackTrace))
    }

    private fun showShowStatus() {
        val isPasswordRequired = mShow!!.isPasswordRequired
        val isPasswordEntered = mShow!!.isPasswordEntered
        val isOnAir = mShow!!.isOnAir
        when (mShow!!.viewType) {
            SHOW -> {
                view.setTimePanelState(false)
                view.showShowMiddlePrompt("Трансляция начнется позже")
            }
            SHOW_CURRENT -> {
                view.setTimePanelState(true)
                if (isOnAir == 0) {
                    view.showShowMiddlePrompt("Трансляция завершилась")
                } else {
                    view.showShowTimeEstimate("Идет сейчас")
                    if (isPasswordRequired == 0) {
                        getShowUrl("")
                    } else {
                        if (isPasswordEntered) {
                            getShowUrl(mShow!!.password)
                        } else {
                            view.showEnterPasswordView()
                        }
                    }
                }
            }
        }
    }

    private fun showShowInfo() {
        showHost()
        showTime()
        showInfo()
    }

    private fun showTime() {
        val lStart = mShow!!.longStartDate
        val lEnd = mShow!!.longEndDate
        val date = mDateConverter.formatDate(lStart, DATE_PATTERN_3)
        view.showShowDate(date)
        val startTime = mDateConverter.formatDate(lStart, DATE_PATTERN_7)
        val endTime = mDateConverter.formatDate(lEnd, DATE_PATTERN_7)
        val time = "$startTime - $endTime"
        view.showShowTime(time)

        val timeUntil = mDateConverter.getTimeLeftUntil(lStart)
        view.showShowTimeEstimate(timeUntil)
    }

    private fun showInfo() {
        val title = mShow!!.title
        val description = mShow!!.description
        val showRoom = mShow!!.room

        if (Strings.isNullOrEmpty(title)) {
            view.hideTitle()
        } else {
            view.showShowTitle(title, 2)
        }
        if (Strings.isNullOrEmpty(description)) {
            view.hideDescription()
        } else {
            view.showShowDescription(description, 2)
        }
        if (Strings.isNullOrEmpty(description) && Strings.isNullOrEmpty(title)) {
            view.hideTitleAndDescription()
        }
        view.showShowRoom(showRoom)
    }

    private fun showHost() {
        val hostName = mShow!!.host.name
        val initials = mShow!!.host.initials
        val hostPicUrl = mShow!!.host.picLink

        if (!Strings.isNullOrEmpty(hostName)) {
            view.setHostName(hostName)
        }
        if (Strings.isNullOrEmpty(hostPicUrl)) {
            view.setHostPicturePlaceHolder()
            view.showHostInitials(initials)
        } else {

            CoroutineScope(Dispatchers.Main).launch {
                mCompositeDisposable.add(mGetImage.bitmap(hostPicUrl, 48)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ view.setHostPicture(it, true) }, Throwable::printStackTrace))
            }
        }
    }

    override fun dropView() {
        mView = null
        mCompositeDisposable.dispose()
    }

    override fun getView(): ShowContract.View {
        return if (mView == null) ShowViewDummy() else mView!!
    }

    override fun onHostClicked() {
        val hostLogin = mShow!!.host.login
        if (hostLogin != null) {
            view.showProfileUi(hostLogin)
        }
    }

    override fun onDoneKeyboardButtonPressed() {

    }

    override fun onEditTextFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            view.setPasswordViewFocused()
        } else {
            view.setPasswordViewNeutral()
        }
    }

    override fun onPortraitOrientationRequest() {
        view.setPortraitOrientation()
    }

    override fun onLandscapeOrientationRequest() {
        view.setLandscapeOrientation()
    }


    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun afterTextChanged(editable: Editable) {
        val text = editable.toString()
        val passWord = mShow!!.password
        val inputLength = text.length
        if (inputLength == 4) {
            if (text == passWord) {
                view.hideEnterPasswordView()
                view.hideKeyboard()
                getShowUrl(text)
                mCompositeDisposable.add(mPasswordDataSource.savePassword(SavedPassword(mShowId, text))
                        .subscribeOn(Schedulers.io())
                        .subscribe({ }, Throwable::printStackTrace))
            } else {
                view.setPasswordViewWrong()
            }
        } else {
            view.setPasswordViewFocused()
        }
    }

    override fun hideKeyboard() {
        view.hideKeyboard()
    }
}
