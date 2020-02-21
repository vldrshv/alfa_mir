package ru.alfabank.alfamir.main.main_feed_fragment.view_dummy;

import android.content.Context;
import android.graphics.Bitmap;
import ru.alfabank.alfamir.main.main_feed_fragment.contract.MainFeedContract;
import ru.alfabank.alfamir.main.main_feed_fragment.contract.MainFeedListContract;

/**
 * Created by U_M0WY5 on 17.05.2018.
 */

public class MainFeedViewDummy implements MainFeedContract.View, MainFeedListContract.Adapter {

    @Override
    public void updateSurvey() {

    }

    @Override
    public void showFeed() {

    }

    @Override
    public void clearFeed() {

    }

    @Override
    public void showMoreNews(int firstNewPosition, int lastNewPosition) {

    }

    @Override
    public void showConnectionErrorSnackBar(String errorText, String retryText, int actionType) {

    }

    @Override
    public void openSearchUi() {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void onAuthorAvatarDownloaded(int position, Bitmap binaryImage, boolean isAnimated) {

    }

    @Override
    public void onPostImageDownloaded(int position, Bitmap binaryImage, boolean isAnimated) {

    }

    @Override
    public void onSurveyImageDownload(int position, Bitmap binaryImage, boolean isAnimated) {

    }

    @Override
    public void onPostOptionsClicked(int position, int isDeletable, String favoriteStatus, String subscribeStatus) {

    }

    @Override
    public void openSurveyActivityUi(String surveyId) {

    }

    @Override
    public void openProfileActivityUi(String id) {

    }

    @Override
    public void openNewsActivityUi(String title, String type, String postUrl) {

    }

    @Override
    public void setLikeStatus(int position, String likesCount, int isLiked) {

    }

    @Override
    public void removePostAtPosition(int position) {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
