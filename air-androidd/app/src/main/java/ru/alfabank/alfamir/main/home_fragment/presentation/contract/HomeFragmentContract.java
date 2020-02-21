package ru.alfabank.alfamir.main.home_fragment.presentation.contract;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;
import ru.alfabank.alfamir.calendar_event.presentation.contract.CalendarEventAdapterContract;

public interface HomeFragmentContract {
    interface View extends BaseView<Presenter> {

        void setUserPic(Bitmap encodedImage, boolean isCached);

        void openProfileUi(String userId);

        void openNotificationUi();

        void openSearchUi();

        void showTopNews();

        void showCalendarEventListCards();

        void openPost(String postUrl);

        void makeCall(String phone);

        void hideTempBackground();

        void setSantaIcon();

        boolean checkCallPermission();

        void requestCallPermission(int phoneType);

        void showWarningOnExtraCharge();

        void callHH();

        void callHD();

        void update(boolean show);
    }

    interface Presenter extends BasePresenter<View>, TopNewsAdapterContract.Presenter, CalendarEventAdapterContract.CalendarEventLoadListener {

        void onProfileClicked();

        void onNotificationClicked();

        void onSearchClicked();

        void onCallHumanHelpClicked();

        void onCallHumanDeskClicked();

        void saveCallParameter(boolean accept);

        void showHomo();

    }
}
