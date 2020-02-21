package ru.alfabank.alfamir.feed.presentation.feed.view_dummies;

import ru.alfabank.alfamir.feed.presentation.feed.contract.FeedAdapterContract;

public class FeedAdapterDummy implements FeedAdapterContract.Adapter {

    @Override
    public void onPostOptionsClicked(int position, int isDeletable, String favoriteStatus, String subscribeStatus) {

    }

    @Override
    public void onSubscribeOptionClicked(String subscribeOption, int position) {

    }

    @Override
    public void setSubscriptionStateChanged(int isSubscribed) {

    }

    @Override
    public void setLikeStatus(int position, String likesCount, int isLiked) {

    }

    @Override
    public void onItemRemoved(int position) {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onItemsInserted(int start, int count) {

    }
}
