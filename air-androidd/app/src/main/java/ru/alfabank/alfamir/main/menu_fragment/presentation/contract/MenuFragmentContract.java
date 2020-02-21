package ru.alfabank.alfamir.main.menu_fragment.presentation.contract;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface MenuFragmentContract {

    interface View extends BaseView<Presenter> {
        void setUserPic(Bitmap encodedImage, boolean isCached);
        void setUserName(String name);
        void setUserPosition(String position);
        void openProfileActivityUi(String userId);
        void setAppVersion(String version);
        void setAppVersionComment(String comment);
        void openSettingsActivityUi();
    }

    interface Presenter extends BasePresenter<View> {
        void onProfileClicked();
        void onSettingsClicked();
    }

}
