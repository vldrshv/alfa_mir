package ru.alfabank.alfamir.feed.presentation.feed.contract;

import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface FeedContract {

    interface View extends BaseView<Presenter> {
        void showFeed();

        void showProgressBar();

        void hideProgressBar();

        void showSnackBar(String text);

        void openPostUi(String feedId, String postId, String postUrl, String postType, int position, boolean startWithComments);

        void openProfileUi(String id);
    }

    interface Presenter extends BasePresenter<View>, FeedAdapterContract.Presenter {
        void onListRefresh();
    }

}
