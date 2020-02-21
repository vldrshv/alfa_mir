package ru.alfabank.alfamir.profile.presentation.profile

import android.graphics.Bitmap
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import ru.alfabank.alfamir.base_elements.BasePresenter
import ru.alfabank.alfamir.base_elements.BaseView
import ru.alfabank.alfamir.profile.data.dto.SubProfile

/**
 * Created by U_M0WY5 on 14.02.2018.
 */

interface ProfileContract {
    interface View : BaseView<Presenter> {
        fun showName(name: String)

        fun hideName()

        fun showTitle(title: String)

        fun hideTitle()

        fun setDepartment(department: String)

        fun showImageAnimated(roundedImage: RoundedBitmapDrawable)

        fun showImage(roundedImage: RoundedBitmapDrawable)

        fun showAddPhoto()

        fun showMobilePhone_0(mobilePhone: String, isEditable: Boolean)

        fun hideMobilePhone_0()

        fun showMobilePhone_1(mobilePhone: String, isEditable: Boolean)

        fun hideMobilePhone_1()

        fun showMobilePhone_2(mobilePhone: String, isEditable: Boolean)

        fun hideMobilePhone_2()

        fun showMobilePhone_3(mobilePhone: String, isEditable: Boolean)

        fun hideMobilePhone_3()

        fun showMobilePhone_4(mobilePhone: String, isEditable: Boolean)

        fun hideMobilePhone_4()

        fun showWorkPhone_1(workPhone: String)

        fun showWorkPhone_2(workPhone: String)

        fun showWorkPhone_3(workPhone: String)

        fun showWorkPhone_4(workPhone: String)

        fun showMore()

        fun hideAddMobile()

        fun showMoreContactsButton()

        fun showHideExtraContactsButton()

        fun hideMoreContactsButton()

        fun showLocalTime(localTime: String)

        fun hideLocalTime()

        fun showIsFavoured(isFavoured: Boolean, text: String)

        fun showLikes(isLiked: Int, likes: Int)

        fun showActivityTeamUi(profileId: String, teamType: String)

        fun showActivityAddressUi(profileId: String)

        fun showActivityPersonalInfoUi(profileId: String)

        fun showActivityTakePictureUi()

        fun showActivityNewsUi(profileId: String, name: String)

        fun showActivityEditPersonalInfoUi(profileId: String)

        fun showActivityAbsenceUi(profileId: String)

        fun showActivitySkillsUi(profileId: String)

        fun showActivityLikesUi(profileId: String)

        fun showActivityChatUi(profileId: String)

        fun showEmptyVacation(text: String)

        fun showVacation(vacation: String)

        fun showCurrentVacation(vacation: String)

        fun showEditProfile()

        fun showAddMobile()

        fun showEditMobileNumberDialog(phone: String?)

        fun enableChatButton()

        fun disableChatButton()

        fun enableWorkPhone(workPhone: String)

        fun disableWorkPhone(text: String)

        fun enableEmail(email: String)

        fun disableEmail(text: String)

        fun disableAddress()

        fun sendMail(email: String)

        fun makeCall(phone: String)

        fun showWarningOnExtraCharge()

        fun checkCallPermission(): Boolean

        fun requestCallPermission(phoneType: Int)

        fun showManagers(managers: List<SubProfile>)

        fun showAssistants(assistants: List<SubProfile>)

        fun setManagerPhoto(login: String, image: Bitmap)

        fun setAssistantPhoto(login: String, image: Bitmap)
    }

    interface Presenter : BasePresenter<View> {
        fun onTeamClicked(teamType: String)

        fun onAddressClicked()

        fun onPersonalInfoClicked()

        fun onEditPersonalInfoClicked()

        fun onMediaClicked()

        fun onSkillsClicked()

        fun onAbsenceClicked()

        fun onMoreClicked()

        fun onLikesClicked()

        fun onAddPhotoClicked()

        fun onLikeClicked()

        fun onFavouriteClicked()

        fun onCallClicked(phoneType: Int)

        fun onChatClicked()

        fun onMailClicked()

        fun onMoreContactsClicked()

        fun onLessContactsClicked()

        fun updatePhoto(encodedImage: String)

        fun processWarning(isChecked: Boolean)

        fun editMobilePhone(phoneType: Int)

        fun saveMobilePhone(number: String)

        fun deleteMobilePhone()

        fun refreshProfile()

        fun onManagerClicked(id: String)
    }
}
