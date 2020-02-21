package ru.alfabank.alfamir.alfa_tv.presentation.show.contract;

import android.graphics.Bitmap;
import android.text.TextWatcher;

import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;
import ru.alfabank.alfamir.ui.custom_views.PasswordEditText;

public interface ShowContract {

    interface View extends BaseView<Presenter> {
        // showData
        void setHostName(String name);
        void setHostPicture(Bitmap encodedImage, boolean isAnimated);
        void setHostPicturePlaceHolder();
        void hideHostInitials();
        void showHostInitials(String initials);
        void showShowDate(String date);
        void showShowTime(String time);
        void showShowTimeEstimate(String time);
        void showShowTitle(String title, int maxLines);
        void showShowDescription(String description, int maxLines);
        void hideTitle();
        void hideDescription();
        void hideTitleAndDescription();
        void showShowDescriptionMore();
        void hideShowDescriptionMore();
        void showShowRoom(String room);
        void setTimePanelState(boolean isActive);

        void clearPassword();
        void setPasswordViewFocused();
        void setPasswordViewWrong();
        void setPasswordViewNeutral();
        void showEnterPasswordView();
        void hideEnterPasswordView();
        void showShowMiddlePrompt(String prompt);

        void hideKeyboard();

        void showShowInfoView();
        void hideSHowInfoView();

        void showLoadingState();
        void hideLoadingState();
        void showSnackBar(String text);

        void initializePlayer(String url);
        void showProfileUi(String profileId);

        void setPortraitOrientation();
        void setLandscapeOrientation();
    }

    interface Presenter extends BasePresenter<View>, PasswordEditText.EditTextListener, TextWatcher {
        void setOrientation(boolean isPortrait);
        void onHostClicked();
        void onDoneKeyboardButtonPressed();
        void onEditTextFocusChanged(boolean hasFocus);
        void onPortraitOrientationRequest();
        void onLandscapeOrientationRequest();
    }

}
