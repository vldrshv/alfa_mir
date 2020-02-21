package ru.alfabank.alfamir.profile.presentation.profile;

import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.alfabank.alfamir.profile.data.dto.SubProfile;

/**
 * Created by U_M0WY5 on 09.04.2018.
 */

class ProfileViewDummy implements ProfileContract.View {

    @Override
    public void showName(String name) {

    }

    @Override
    public void hideName() {

    }

    @Override
    public void showTitle(String title) {

    }

    @Override
    public void hideTitle() {

    }

    @Override
    public void showImageAnimated(RoundedBitmapDrawable roundedImage) {

    }

    @Override
    public void showImage(RoundedBitmapDrawable roundedImage) {

    }

    @Override
    public void showAddPhoto() {

    }

    @Override
    public void showMobilePhone_0(String mobilePhone, boolean isEditable) {

    }

    @Override
    public void hideMobilePhone_0() {

    }

    @Override
    public void showMobilePhone_1(String mobilePhone, boolean isEditable) {

    }


    @Override
    public void hideMobilePhone_1() {

    }

    @Override
    public void showMobilePhone_2(String mobilePhone, boolean isEditable) {

    }

    @Override
    public void hideMobilePhone_2() {

    }

    @Override
    public void showMobilePhone_3(String mobilePhone, boolean isEditable) {

    }

    @Override
    public void hideMobilePhone_3() {

    }

    @Override
    public void showMobilePhone_4(String mobilePhone, boolean isEditable) {

    }

    @Override
    public void hideMobilePhone_4() {

    }

    @Override
    public void hideAddMobile() {

    }

    @Override
    public void showMoreContactsButton() {

    }

    @Override
    public void showHideExtraContactsButton() {

    }

    @Override
    public void hideMoreContactsButton() {

    }

    @Override
    public void showLocalTime(String localTime) {

    }

    @Override
    public void hideLocalTime() {

    }

    @Override
    public void showIsFavoured(boolean isFavoured, String text) {

    }

    @Override
    public void showLikes(int isLiked, int likes) {

    }

    @Override
    public void showActivityTeamUi(String profileId, String teamType) {

    }

    @Override
    public void showActivityAddressUi(String profileId) {

    }

    @Override
    public void showActivityPersonalInfoUi(String profileId) {

    }

    @Override
    public void showActivityTakePictureUi() {

    }

    @Override
    public void showActivityNewsUi(String profileId, String name) {

    }

    @Override
    public void showActivityEditPersonalInfoUi(String profileId) {

    }

    @Override
    public void showActivityAbsenceUi(String profileId) {

    }

    @Override
    public void showActivitySkillsUi(String profileId) {

    }

    @Override
    public void showActivityLikesUi(String profileId) {

    }

    @Override
    public void showActivityChatUi(String profileId) {

    }

    @Override
    public void showEmptyVacation(String text) {

    }

    @Override
    public void showVacation(String vacation) {

    }

    @Override
    public void showCurrentVacation(String vacation) {

    }

    @Override
    public void showEditProfile() {

    }

    @Override
    public void showAddMobile() {

    }

    @Override
    public void showEditMobileNumberDialog(String phone) {

    }

    @Override
    public void enableChatButton() {

    }

    @Override
    public void disableChatButton() {

    }

    @Override
    public void enableWorkPhone(String workPhone) {

    }

    @Override
    public void disableWorkPhone(String text) {

    }

    @Override
    public void showWorkPhone_1(String workPhone) {

    }

    @Override
    public void showWorkPhone_2(String workPhone) {

    }

    @Override
    public void showWorkPhone_3(String workPhone) {

    }

    @Override
    public void showWorkPhone_4(String workPhone) {

    }

    @Override
    public void showMore() {

    }

    @Override
    public void enableEmail(String email) {

    }

    @Override
    public void disableEmail(String text) {

    }

    @Override
    public void disableAddress() {

    }

    @Override
    public void sendMail(String email) {

    }

    @Override
    public void makeCall(String phone) {

    }

    @Override
    public void showWarningOnExtraCharge() {

    }

    @Override
    public boolean checkCallPermission() {
        return false;
    }

    @Override
    public void requestCallPermission(int phoneType) {

    }

    @Override
    public void showManagers(List<SubProfile> managers) {

    }

    @Override
    public void setManagerPhoto(String login, Bitmap image){

    }

    @Override
    public void setDepartment(@NotNull String department) {

    }

    @Override
    public void showAssistants(@NotNull List<SubProfile> assistants) {

    }

    @Override
    public void setAssistantPhoto(@NotNull String login, @NotNull Bitmap image) {

    }
}
