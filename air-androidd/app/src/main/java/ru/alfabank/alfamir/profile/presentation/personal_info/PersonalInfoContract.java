package ru.alfabank.alfamir.profile.presentation.personal_info;

import ru.alfabank.alfamir.base_elements.BaseView;
import ru.alfabank.alfamir.base_elements.BasePresenter;

/**
 * Created by mshvdvsk on 25/02/2018.
 */

public interface PersonalInfoContract {
    interface View extends BaseView<Presenter> {
        void showBirthday(String date);
        void hideBirthday();
        void showAboutMe(String text);
        void showEmptyAboutMe(String text);
        void hideAboutMe();
        void showEditInfoIcon();
        void hideEditInfoIcon();
        void showAddInfoButton(String text);
        void hideAddInfoButton();
        void focusOnEditText();
    }

    interface Presenter extends BasePresenter<View> {
        void uploadAboutMe(String text);
        void updateAboutMe(String text);
        void addAboutMe();
    }
}
