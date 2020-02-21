package ru.alfabank.alfamir.profile.presentation.edit_info;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

/**
 * Created by U_M0WY5 on 19.03.2018.
 */

public interface EditInfoContract {

    interface View extends BaseView<Presenter> {
        void showPhoto(Bitmap encodedImage);
        void showMobile(String text);
        void hideMobile();
        void showAddMobile(String text);
        void hideAddMobile();
        void showAboutMe(String text);
        void showEditMobileNumberDialog(String phone);
        void showActivityTakePictureUi();
    }

    interface Presenter extends BasePresenter<View> {
        void addMobile();
        void editMobile();
        void addPhoto();
        void updatePhoto(String encodedImage);
        void updateAboutMe(String text);
        void updateMobilePhone(String number);
        void deleteMobilePhone();
        void saveChanges();
    }

}
