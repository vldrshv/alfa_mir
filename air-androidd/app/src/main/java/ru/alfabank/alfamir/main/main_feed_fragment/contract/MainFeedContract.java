package ru.alfabank.alfamir.main.main_feed_fragment.contract;


import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface MainFeedContract {

    interface View extends BaseView<Presenter> {
        void updateSurvey();
        void showFeed();
        void clearFeed();
        void showProgressBar();
        void hideProgressBar();
        void showMoreNews(int firstNewPosition, int lastNewPosition);
        void showConnectionErrorSnackBar(String errorText, String retryText, int actionType);
        void openSearchUi();
    }

    interface Presenter extends BasePresenter<View> {
        void onListRefresh();
        void onLoadMore();
        void refreshSurveyStatus();
        void onErrorSnackBarActionClicked(int actionType);
        void onErrorSnackBarDismissedItself();
        void onSearchClicked();
    }

}
