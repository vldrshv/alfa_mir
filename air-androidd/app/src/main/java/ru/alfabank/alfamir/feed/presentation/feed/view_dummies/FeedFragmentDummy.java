package ru.alfabank.alfamir.feed.presentation.feed.view_dummies;

import ru.alfabank.alfamir.feed.presentation.feed.contract.FeedContract;

public class FeedFragmentDummy implements FeedContract.View {

    @Override
    public void showFeed() {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showSnackBar(String text) {

    }

    @Override
    public void openPostUi(String feedId, String postId, String postUrl, String postType, int position, boolean startWithComments) {

    }

    @Override
    public void openProfileUi(String id) {

    }
}
