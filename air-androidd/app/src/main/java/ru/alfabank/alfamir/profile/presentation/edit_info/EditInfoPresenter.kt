package ru.alfabank.alfamir.profile.presentation.edit_info

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.common.base.Strings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_DP
import ru.alfabank.alfamir.data.source.rxbus.ProfileModule
import ru.alfabank.alfamir.image.data.source.repository.ImageDataSource
import ru.alfabank.alfamir.image.data.source.repository.ImageRepository
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.profile.data.source.repository.ProfileRepository
import ru.alfabank.alfamir.profile.domain.usecase.GetProfile
import ru.alfabank.alfamir.profile.domain.usecase.SaveAboutMe
import ru.alfabank.alfamir.profile.domain.usecase.SaveNewMobileNumber
import ru.alfabank.alfamir.profile.presentation.dto.Profile
import javax.inject.Inject

class EditInfoPresenter @Inject
internal constructor(private val mUserLogin: String,
                     private val mGetImage: GetImage,
                     profileRepository: ProfileRepository,
                     private val mImageRepository: ImageRepository,
                     private val mSaveAboutMe: SaveAboutMe,
                     private val mSaveNewMobileNumber: SaveNewMobileNumber,
                     private val mGetProfile: GetProfile) : EditInfoContract.Presenter {

    private var mView: EditInfoContract.View? = null
    private var mProfile: Profile? = null
    private var mNewProfilePicEncoded: String? = null
    private var mNewMobile: String? = null
    private var mNewAboutMe: String? = null
    private val mCompositeDisposable = CompositeDisposable()
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun takeView(view: EditInfoContract.View) {
        mView = view
        loadProfileInfo()
    }

    override fun dropView() {
        mCompositeDisposable.dispose()
        mView = null
    }

    override fun getView(): EditInfoContract.View {
        return if (mView == null) {
            EditInfoViewDummy()
        } else {
            mView!!
        }
    }

    private fun loadProfileInfo() {
        mCompositeDisposable.add(
                mGetProfile.execute(mUserLogin)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ responseValue ->
                            mProfile = responseValue

                            if (!Strings.isNullOrEmpty(mNewProfilePicEncoded)) {
                                uiScope.launch {
                                    var bitmap: Bitmap? = null
                                    withContext(Dispatchers.Default) {
                                        val byte = Base64.decode(mNewProfilePicEncoded, Base64.DEFAULT)
                                        bitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size)
                                    }
                                    view.showPhoto(bitmap)
                                }
                                mGetProfile.execute(mUserLogin)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe()
                            }
                            val url = mProfile!!.picUrl
                            if (!Strings.isNullOrEmpty(url)) {
                                loadPicture(url)
                            }
                            showProfileInfo()
                        }, Throwable::printStackTrace)
        )
    }

    private fun showProfileInfo() {
        val mobilePhones = mProfile!!.mobilePhone
        val aboutMe = mProfile!!.aboutMe

        if (mobilePhones.isEmpty()) {
            view.hideMobile()
            view.showAddMobile("+ Добавить мобильный")
        } else {
            val mobile = mobilePhones[0]
            view.showMobile(mobile)
            view.hideAddMobile()
        }
        view.showAboutMe(aboutMe)

    }

    private fun loadPicture(url: String) {
        CoroutineScope(Dispatchers.Main).launch {
            mCompositeDisposable.add(mGetImage.bitmap(url, SCREEN_WIDTH_DP)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view.showPhoto(it) }, Throwable::printStackTrace))
        }
    }

    override fun addMobile() {
        view.showEditMobileNumberDialog("")
    }

    override fun editMobile() {
        if (Strings.isNullOrEmpty(mNewMobile)) {
            val phone = mProfile!!.mobilePhone[0]
            view.showEditMobileNumberDialog(phone)
        } else {
            view.showEditMobileNumberDialog(mNewMobile)
        }

    }

    override fun updateAboutMe(text: String) {
        mNewAboutMe = text
    }

    override fun updatePhoto(encodedImage: String) {
        mNewProfilePicEncoded = encodedImage
    }

    override fun addPhoto() {
        view.showActivityTakePictureUi()
    }

    override fun updateMobilePhone(number: String) {
        mNewMobile = number
        view.showMobile(mNewMobile)
        view.hideAddMobile()
    }

    override fun deleteMobilePhone() {
        mNewMobile = ""
        view.hideMobile()
        view.showAddMobile("+ Добавить мобильный")
    }

    override fun saveChanges() {
        if (!mNewProfilePicEncoded.isNullOrEmpty()) {
            savePhoto(mNewProfilePicEncoded!!)
        }

        if (mNewMobile != null) {
            saveMobile(mNewMobile!!)
        }

        if (mNewAboutMe != null) {
            saveAboutMe(mNewAboutMe!!)
        }
    }

    private fun savePhoto(encodedImage: String) {
        mImageRepository.uploadImage(encodedImage, object : ImageDataSource.UploadImageCallback {
            override fun onImageUploaded() {
                mImageRepository.refreshImage()
                ProfileModule.getInstance().updateProfile()
            }

            override fun onServerNotAvailable() {

            }
        })
    }

    private fun saveMobile(number: String) {
        mCompositeDisposable.add(
                mSaveNewMobileNumber.execute(SaveNewMobileNumber.RequestValues(number))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ ProfileModule.getInstance().updateProfile() }, Throwable::printStackTrace)
        )
    }

    private fun saveAboutMe(aboutMeText: String) {
        mCompositeDisposable.add(
                mSaveAboutMe.execute(SaveAboutMe.RequestValues(aboutMeText))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ ProfileModule.getInstance().updateProfile() }, Throwable::printStackTrace)
        )
    }

}
