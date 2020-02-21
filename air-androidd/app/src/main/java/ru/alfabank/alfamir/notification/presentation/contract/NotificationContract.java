package ru.alfabank.alfamir.notification.presentation.contract;

import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface NotificationContract {

    interface View extends BaseView<Presenter> {
        void showNotifications();
        void openPostUi(String postId, String feedUrl, String feedType);
        void openProfileUi(String profileLogin);
        void openSurveyUi(String quizId);
        void showLoading();
        void hideLoading();
    }

    interface Presenter extends BasePresenter<View>, NotificationAdapterContract.Presenter {
        void onLoadMore();
    }
}