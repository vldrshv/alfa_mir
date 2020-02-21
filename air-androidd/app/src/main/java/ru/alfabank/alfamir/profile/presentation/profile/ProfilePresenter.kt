package ru.alfabank.alfamir.profile.presentation.profile

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import com.google.common.base.Strings
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.Constants.Companion.DELETE
import ru.alfabank.alfamir.Constants.Companion.EDIT
import ru.alfabank.alfamir.Constants.Initialization.DENSITY
import ru.alfabank.alfamir.Constants.Initialization.MESSENGER_ENABLED
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_DP
import ru.alfabank.alfamir.Constants.Initialization.USER_LOGIN
import ru.alfabank.alfamir.Constants.Profile.ADD_PHONE_MOBILE
import ru.alfabank.alfamir.Constants.Profile.PHONE_MOBILE_0
import ru.alfabank.alfamir.Constants.Profile.PHONE_MOBILE_1
import ru.alfabank.alfamir.Constants.Profile.PHONE_MOBILE_2
import ru.alfabank.alfamir.Constants.Profile.PHONE_MOBILE_3
import ru.alfabank.alfamir.Constants.Profile.PHONE_MOBILE_4
import ru.alfabank.alfamir.Constants.Profile.PHONE_WORK_0
import ru.alfabank.alfamir.Constants.Profile.PHONE_WORK_1
import ru.alfabank.alfamir.Constants.Profile.PHONE_WORK_2
import ru.alfabank.alfamir.Constants.Profile.PHONE_WORK_3
import ru.alfabank.alfamir.Constants.Profile.PHONE_WORK_4
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.data.source.repositories.favorite_people.FavoritePeopleRepository
import ru.alfabank.alfamir.data.source.rxbus.ProfileModule
import ru.alfabank.alfamir.favorites.domain.usecase.AddFavoritePerson
import ru.alfabank.alfamir.favorites.domain.usecase.RemoveFavoritePerson
import ru.alfabank.alfamir.image.data.source.repository.ImageDataSource
import ru.alfabank.alfamir.image.data.source.repository.ImageRepository
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.profile.data.dto.SubProfile
import ru.alfabank.alfamir.profile.data.source.repository.ProfileRepository
import ru.alfabank.alfamir.profile.domain.usecase.*
import ru.alfabank.alfamir.profile.presentation.dto.Profile
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract
import ru.alfabank.alfamir.utility.static_utilities.BackGroundGenerator
import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper
import ru.alfabank.alfamir.utility.toBoolean
import javax.inject.Inject

internal class ProfilePresenter
@Inject
constructor(private val mUserLogin: String,
            private val mGetProfile: GetProfile,
            private val mGetImage: GetImage,
            private val mSetLike: SetLike,
            private val mRemoveFavoritePerson: RemoveFavoritePerson,
            private val mAddFavoritePerson: AddFavoritePerson,
            private val mSaveAboutMe: SaveAboutMe,
            private val mSaveNewMobileNumber: SaveNewMobileNumber,
            private val profileRepository: ProfileRepository,
            private val imageRepository: ImageRepository,
            private val favouritesRepository: FavoritePeopleRepository,
            private val mSharedPreferences: SharedPreferences,
            private val mImageCropper: ImageCropper,
            private val mGetChatAvailabilityStatus: GetChatAvailabilityStatus,
            private val mLogger: LoggerContract.Provider) : ProfileContract.Presenter, LoggerContract.Client.Profile {

    private var mView: ProfileContract.View? = null
    private var mCurrentMobilePhoneEditing = -1
    private var isMyProfile: Boolean = false
    private var mIsTouchEnabled: Boolean = false
    private lateinit var mProfile: Profile
    private val mCompositeDisposable = CompositeDisposable()

    init {
        if (mUserLogin.toLowerCase() == USER_LOGIN.toLowerCase()) isMyProfile = true
        setUpdateListeners()
    }

    override fun takeView(view: ProfileContract.View) {
        mView = view
        loadProfile()
        logOpen(mUserLogin)
    }

    // todo VLAD add loading
    private fun loadProfile() {
        mCompositeDisposable.add(
                mGetProfile.execute(mUserLogin)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ responseValue ->
                            mProfile = responseValue
                            val url = mProfile.picUrl
                            if (!url.isNullOrEmpty()) {
                                loadPicture(url)
                            }
                            showProfile()
                            checkChatAvailability(mUserLogin)
                            mIsTouchEnabled = true
                        }, Throwable::printStackTrace)
        )
    }

    override fun dropView() {
        mCompositeDisposable.dispose()
        mView = null
    }

    override fun getView(): ProfileContract.View {
        if (mView == null) {
            mView = ProfileViewDummy()
        }
        return mView!!
    }

    override fun onTeamClicked(teamType: String) {
        if (!mIsTouchEnabled) return
        view.showActivityTeamUi(mUserLogin, teamType)
    }

    override fun onAddressClicked() {
        if (!mIsTouchEnabled) return
        view.showActivityAddressUi(mUserLogin)
    }

    override fun onPersonalInfoClicked() {
        if (!mIsTouchEnabled) return
        view.showActivityPersonalInfoUi(mUserLogin)
    }

    override fun onEditPersonalInfoClicked() {
        if (!mIsTouchEnabled) return
        view.showActivityEditPersonalInfoUi(mUserLogin)
    }

    override fun onMediaClicked() {
        if (!mIsTouchEnabled) return
        val name = mProfile.name
        val id = mProfile.login
        view.showActivityNewsUi(id, name)
    }

    override fun onSkillsClicked() {
        if (!mIsTouchEnabled) return
        view.showActivitySkillsUi(mUserLogin)
    }

    override fun onAbsenceClicked() {
        if (!mIsTouchEnabled) return
        view.showActivityAbsenceUi(mUserLogin)
    }

    override fun onMoreClicked() {
        if (!mIsTouchEnabled) return
        view.showMore()
    }

    override fun onLikesClicked() {
        if (!mIsTouchEnabled) return
        view.showActivityLikesUi(mUserLogin)
    }

    override fun onAddPhotoClicked() {
        if (!mIsTouchEnabled) return
        view.showActivityTakePictureUi()
    }

    override fun updatePhoto(encodedImage: String) {
        imageRepository.uploadImage(encodedImage, object : ImageDataSource.UploadImageCallback {
            override fun onImageUploaded() {
                imageRepository.refreshImage()
                loadProfile()
            }

            override fun onServerNotAvailable() {

            }
        })
    }

    override fun processWarning(isChecked: Boolean) {
        if (isChecked) {
            mSharedPreferences.edit().putBoolean("mobile_warning_checkbox", true).apply()
        }
        makeCall(PHONE_WORK_0)
    }

    override fun onLikeClicked() {
        if (!mIsTouchEnabled) return
        val targetLikeStatus: Int = if (mProfile.isLiked == 0) 1 else 0

        mCompositeDisposable.add(
                mSetLike.execute(SetLike.RequestValues(mUserLogin, targetLikeStatus))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ responseValue ->
                            val isLiked = responseValue.isLiked
                            val currentCount = responseValue.currentLikes
                            mProfile?.isLiked = isLiked
                            profileRepository.refreshShortProfiles()
                            view.showLikes(isLiked, currentCount)
                        }, Throwable::printStackTrace)
        )
    }

    override fun onFavouriteClicked() {
        if (!mIsTouchEnabled) return
        if (mProfile.isFavoured == 1) {
            mCompositeDisposable.add(
                    mRemoveFavoritePerson.execute(RemoveFavoritePerson.RequestValues(mUserLogin))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                mProfile.isFavoured = 0
                                view.showIsFavoured(false, "Добавить \n в избранное")
                            }, Throwable::printStackTrace))
        } else {
            mCompositeDisposable.add(
                    mAddFavoritePerson.execute(AddFavoritePerson.RequestValues(mUserLogin))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                mProfile.isFavoured = 1
                                view.showIsFavoured(true, "В избранном")
                            }, Throwable::printStackTrace))
        }
    }

    override fun onCallClicked(phoneType: Int) {
        if (!mIsTouchEnabled) return
        val isCallPermissionGranted = view.checkCallPermission()
        if (!isCallPermissionGranted) {
            view.requestCallPermission(phoneType)
            return
        }
        val isWarningAccepted = mSharedPreferences.getBoolean("mobile_warning_checkbox", false)
        if (!isWarningAccepted) {
            view.showWarningOnExtraCharge()
            return
        }
        makeCall(phoneType)
    }

    override fun onChatClicked() {
        view.showActivityChatUi(mUserLogin)
    }

    override fun onMailClicked() {
        val email = mProfile.email
        view.sendMail(email)
    }

    override fun onMoreContactsClicked() {
        val mobilePhoneNumbers = mProfile.mobilePhone
        showMobilePhoneNumbers(mobilePhoneNumbers)
        view.showHideExtraContactsButton()
    }

    override fun onLessContactsClicked() {
        if (!mIsTouchEnabled) return
        hideExtraMobileNumbers()
        view.showMoreContactsButton()
    }

    override fun editMobilePhone(phoneType: Int) {
        val mobilePhoneNumbers = mProfile.mobilePhone
        var phone: String? = ""

        if (mobilePhoneNumbers.isEmpty() && ADD_PHONE_MOBILE != phoneType) {
            return
        }

        when (phoneType) {
            PHONE_MOBILE_0 -> phone = mobilePhoneNumbers[0]
            PHONE_MOBILE_1 -> phone = mobilePhoneNumbers[1]
            PHONE_MOBILE_2 -> phone = mobilePhoneNumbers[2]
            PHONE_MOBILE_3 -> phone = mobilePhoneNumbers[3]
            PHONE_MOBILE_4 -> phone = mobilePhoneNumbers[4]
            ADD_PHONE_MOBILE -> phone = null
        }
        mCurrentMobilePhoneEditing = phoneType
        view.showEditMobileNumberDialog(phone)
    }

    override fun saveMobilePhone(number: String) {
        updateMobileNumbers(number, EDIT)
    }

    override fun deleteMobilePhone() {
        updateMobileNumbers(null, DELETE)
    }

    override fun refreshProfile() {
        profileRepository.refreshProfile()
        loadProfile()
    }

    private fun updateMobileNumbers(newNumber: String?, actionType: Int) {
        val result = managePhones(mProfile.mobilePhone.toMutableList(), newNumber, actionType)
        mCompositeDisposable.add(
                mSaveNewMobileNumber.execute(SaveNewMobileNumber.RequestValues(result))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            profileRepository.refreshProfile()
                            loadProfile()
                        }, Throwable::printStackTrace)
        )
    }

    private fun checkChatAvailability(respondentLogin: String) {
        if (respondentLogin == USER_LOGIN || !MESSENGER_ENABLED) {
            view.disableChatButton()
        } else {
            mCompositeDisposable.add(
                    mGetChatAvailabilityStatus.execute(GetChatAvailabilityStatus.RequestValues(respondentLogin))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ responseValue ->
                                val status = responseValue.chatAvailabilityStatus
                                if (status == 0) {
                                    view.disableChatButton()
                                } else if (status == 1) {
                                    view.enableChatButton()
                                }
                            }, Throwable::printStackTrace))
        }
    }

    private fun loadPicture(url: String) {

        CoroutineScope(Dispatchers.Main).launch {
            mCompositeDisposable.add(mGetImage.bitmap(url, SCREEN_WIDTH_DP)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view.showImageAnimated(PictureClipper.makeItRound(it)) }, Throwable::printStackTrace))
        }
    }

    private fun showProfile() {
        val name = mProfile.name
        val title = mProfile.position
        val email = mProfile.email
        val workPhoneNumbers = mProfile.workPhone
        val mobilePhoneNumbers = mProfile.mobilePhone
        val localTime = mProfile.localTime
        val vacation = mProfile.vacation

        if (name.isNullOrEmpty()) {
            view.hideName()
        } else {
            view.showName(name)
        }

        if (title.isNullOrEmpty()) {
            view.hideTitle()
        } else {
            view.showTitle(title)
        }

        view.setDepartment(mProfile.department)

        if (email.isNullOrEmpty()) {
            view.disableEmail("Нет почты")
        } else {
            view.enableEmail(email)
        }

        if (workPhoneNumbers.isEmpty()) {
            val text = "Нет телефона"
            view.disableWorkPhone(text)
        } else {
            handleMultipleWorkPhones(workPhoneNumbers)
        }

        hideExtraMobileNumbers()// >> order matters; this goes first to ensure the right state
        if (mobilePhoneNumbers.isEmpty()) {
            view.hideMobilePhone_0()
        } else {
            if (isMyProfile) {
                showMobilePhoneNumbers(mobilePhoneNumbers)
                view.hideMoreContactsButton()
            } else {
                val mainMobilePhone = mobilePhoneNumbers[0]
                view.showMobilePhone_0(mainMobilePhone, isMyProfile)

                if (mobilePhoneNumbers.size == 2) {
                    val additionalMobilePhone = mobilePhoneNumbers[1]
                    view.showMobilePhone_1(additionalMobilePhone, isMyProfile)
                    view.hideMoreContactsButton()
                } else if (mobilePhoneNumbers.size > 2) {
                    view.showMoreContactsButton()
                }
            }
        }

        if (mProfile.workSpaceAddress.isNullOrEmpty() && mProfile.fullAddress.isNullOrEmpty() && mProfile.shortAddress.isNullOrEmpty()) {
            view.disableAddress()
        }

        if (Strings.isNullOrEmpty(localTime)) {
            view.hideLocalTime()
        } else {
            view.showLocalTime(localTime)
        }

        if (vacation.isNullOrEmpty()) {
            view.showEmptyVacation("Отсутствия не запланированы")
        } else {
            if (mProfile.isVacationCurrent) {
                view.showCurrentVacation(vacation)
            } else {
                view.showVacation(vacation)
            }
        }

        view.showLikes(mProfile.isLiked, mProfile.likes)

        if (isMyProfile) {
            view.showEditProfile()
            view.showAddPhoto()
            if (mobilePhoneNumbers.size < 5) {
                view.showAddMobile()
            } else {
                view.hideAddMobile()
            }

        } else {
            if (mProfile.isFavoured.toBoolean()) {
                view.showIsFavoured(true, "В избранном")
            } else {
                view.showIsFavoured(false, "Добавить \n в избранное")
            }
        }

        showManagers()
        showAssistants()
    }

    private fun showManagers() {
        val managers = mutableSetOf<SubProfile>()

        if (mProfile.administrativeManager != null) {
            managers.add(mProfile.administrativeManager)
        }

        if (mProfile.functionalManager != null) {
            managers.add(mProfile.functionalManager)
        }

        if (managers.isNotEmpty()) {

            managers.forEach { subProfile ->
                if (subProfile.picUrl.isNullOrEmpty()) {
                    val drawable = (view as ProfileActivity).resources.getDrawable(R.drawable.rectangle_background)
                    drawable.setColorFilter(Color.parseColor("#" + BackGroundGenerator.getBackgroundAvatarColor(InitialsMaker.formInitials(subProfile.name))), PorterDuff.Mode.SRC_IN)
                    subProfile.background = drawable
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        mCompositeDisposable.add(mGetImage.bitmap(subProfile.picUrl!!.split(";#")[0], 80)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ bitmap ->
                                    view.setManagerPhoto(subProfile.login!!, mImageCropper.getRoundedCornerBitmap(bitmap, (4 * DENSITY).toInt()))
                                }, Throwable::printStackTrace))
                    }
                }
            }
            view.showManagers(managers.toList())
        } else {
            view.showManagers(arrayListOf())
        }
    }

    private fun showAssistants() {
        val assistants = mProfile.assistants

        if (assistants.isNotEmpty()) {

            assistants.forEach { subProfile ->
                if (subProfile.picUrl.isNullOrEmpty()) {
                    val drawable = (view as ProfileActivity).resources.getDrawable(R.drawable.rectangle_background)
                    drawable.setColorFilter(Color.parseColor("#" + BackGroundGenerator.getBackgroundAvatarColor(InitialsMaker.formInitials(subProfile.name))), PorterDuff.Mode.SRC_IN)
                    subProfile.background = drawable
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        mCompositeDisposable.add(mGetImage.bitmap(subProfile.picUrl!!.split(";#")[0], 80)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ bitmap ->
                                    view.setAssistantPhoto(subProfile.login!!, mImageCropper.getRoundedCornerBitmap(bitmap, (4 * DENSITY).toInt()))
                                }, Throwable::printStackTrace))
                    }
                }
            }
            view.showAssistants(assistants.toList())
        } else {
            view.showAssistants(arrayListOf())
        }
    }

    override fun onManagerClicked(id: String) {
        val context = view as ProfileActivity
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra("id", id)
        context.startActivity(intent)
    }

    private fun handleMultipleWorkPhones(phoneNumbers: List<String>) {
        var phone: String
        for (i in phoneNumbers.indices) {
            phone = phoneNumbers[i]
            if (Strings.isNullOrEmpty(phone)) {

            } else {
                showExtraWorkPhone(i, phone)
            }
        }
    }

    private fun showExtraWorkPhone(position: Int, phoneNumber: String) {
        when (position) {
            0 -> view.enableWorkPhone(phoneNumber)
            1 -> view.showWorkPhone_1(phoneNumber)
            2 -> view.showWorkPhone_2(phoneNumber)
            3 -> view.showWorkPhone_3(phoneNumber)
            4 -> view.showWorkPhone_4(phoneNumber)
        }
    }

    private fun hideExtraMobileNumbers() {
        view.hideMobilePhone_1()
        view.hideMobilePhone_2()
        view.hideMobilePhone_3()
        view.hideMobilePhone_4()
    }

    private fun showMobilePhoneNumbers(phoneNumbers: List<String>) {
        var phone: String
        for (i in phoneNumbers.indices) {
            phone = phoneNumbers[i]
            if (Strings.isNullOrEmpty(phone)) {

            } else {
                showMobilePhoneNumber(i, phone)
            }
        }
    }

    private fun showMobilePhoneNumber(position: Int, phoneNumber: String) {
        when (position) {
            0 -> view.showMobilePhone_0(phoneNumber, isMyProfile)
            1 -> view.showMobilePhone_1(phoneNumber, isMyProfile)
            2 -> view.showMobilePhone_2(phoneNumber, isMyProfile)
            3 -> view.showMobilePhone_3(phoneNumber, isMyProfile)
            4 -> view.showMobilePhone_4(phoneNumber, isMyProfile)
        }
    }

    private fun managePhones(phoneNumbers: MutableList<String?>, updatedPhoneNumber: String?, actionType: Int): String {
        when (actionType) {
            EDIT -> {
                when (mCurrentMobilePhoneEditing) {
                    PHONE_MOBILE_0 -> phoneNumbers[0] = updatedPhoneNumber
                    PHONE_MOBILE_1 -> phoneNumbers[1] = updatedPhoneNumber
                    PHONE_MOBILE_2 -> phoneNumbers[2] = updatedPhoneNumber
                    PHONE_MOBILE_3 -> phoneNumbers[3] = updatedPhoneNumber
                    PHONE_MOBILE_4 -> phoneNumbers[4] = updatedPhoneNumber
                    ADD_PHONE_MOBILE -> phoneNumbers.add(updatedPhoneNumber)
                }
            }
            DELETE -> {
                when (mCurrentMobilePhoneEditing) {
                    PHONE_MOBILE_0 -> phoneNumbers.removeAt(0)
                    PHONE_MOBILE_1 -> phoneNumbers.removeAt(1)
                    PHONE_MOBILE_2 -> phoneNumbers.removeAt(2)
                    PHONE_MOBILE_3 -> phoneNumbers.removeAt(3)
                    PHONE_MOBILE_4 -> phoneNumbers.removeAt(4)
                }
            }
        }

        mCurrentMobilePhoneEditing = -1

        if (phoneNumbers.isEmpty()) {
            return ""
        }

        val sb = StringBuilder()
        for (s in phoneNumbers) {
            sb.append("$s;")
        }
        return sb.toString().substring(0, sb.toString().length - 1)
    }

    private fun makeCall(phoneType: Int) {
        val workPhoneNumbers = mProfile.workPhone
        val mobilePhoneNumbers = mProfile.mobilePhone

        var result = String()
        val phone: String

        when (phoneType) {
            PHONE_WORK_0 -> {
                phone = workPhoneNumbers[0]
                result = checkForPrefix(phone)
            }
            PHONE_WORK_1 -> {
                phone = workPhoneNumbers[1]
                result = checkForPrefix(phone)
            }
            PHONE_WORK_2 -> {
                phone = workPhoneNumbers[2]
                result = checkForPrefix(phone)
            }
            PHONE_WORK_3 -> {
                phone = workPhoneNumbers[3]
                result = checkForPrefix(phone)
            }
            PHONE_WORK_4 -> {
                phone = workPhoneNumbers[4]
                result = checkForPrefix(phone)
            }

            PHONE_MOBILE_0 -> result = mobilePhoneNumbers[0]
            PHONE_MOBILE_1 -> result = mobilePhoneNumbers[1]
            PHONE_MOBILE_2 -> result = mobilePhoneNumbers[2]
            PHONE_MOBILE_3 -> result = mobilePhoneNumbers[3]
            PHONE_MOBILE_4 -> result = mobilePhoneNumbers[4]
        }

        view.makeCall(result)
    }

    private fun checkForPrefix(phoneNumber: String): String {
        val prefix = "84956209191,,"
        return if (phoneNumber.contains("+7")) {
            phoneNumber
        } else {
            prefix + phoneNumber
        }
    }

    private fun setUpdateListeners() {
        ProfileModule.getInstance().listenForUpdate()
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(profile: String) {
                        refreshProfile()
                    }

                    override fun onError(e: Throwable) {}

                    override fun onComplete() {}
                })
    }

    // logging

    override fun logOpen(id: String) {
        mLogger.openProfile(id)
    }

    override fun logError(message: String, stackTrace: String) {
        mLogger.error(message, stackTrace)
    }
}
