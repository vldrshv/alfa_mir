package ru.alfabank.alfamir.notification.presentation.dummy_view;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.notification.presentation.contract.NotificationAdapterContract;
import ru.alfabank.alfamir.notification.presentation.contract.NotificationContract;

public class NotificationDummyView implements NotificationContract.View, NotificationAdapterContract.Adapter {

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onImageLoaded(int position, long viewId, Bitmap encodedImage, boolean isAnimated) {

    }

    @Override
    public void openPostUi(String feedId, String postId, String feedUrl, String feedType) {

    }

    @Override
    public void showNotifications() {

    }

    @Override
    public void openPostUi(String postId, String feedUrl, String feedType) {

    }

    @Override
    public void openProfileUi(String profileLogin) {

    }

    @Override
    public void openSurveyUi(String quizId) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
