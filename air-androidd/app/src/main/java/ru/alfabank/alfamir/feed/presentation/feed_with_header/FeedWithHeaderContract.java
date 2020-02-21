package ru.alfabank.alfamir.feed.presentation.feed_with_header;

import ru.alfabank.alfamir.feed.presentation.feed.contract.FeedContract;

public interface FeedWithHeaderContract extends FeedContract {

    interface View extends FeedContract.View {
        void setToolbarTitle(String title);
    }

    interface Presenter extends FeedContract.Presenter{
        void takeView(FeedWithHeaderContract.View view);
        void onOptionsClicked();
    }

}
