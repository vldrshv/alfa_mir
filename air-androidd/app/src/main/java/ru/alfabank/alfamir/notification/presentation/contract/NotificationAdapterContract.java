package ru.alfabank.alfamir.notification.presentation.contract;

import android.graphics.Bitmap;
import android.text.Spanned;

import ru.alfabank.alfamir.base_elements.BaseAdapter;
import ru.alfabank.alfamir.base_elements.BaseListPresenter;
import ru.alfabank.alfamir.notification.presentation.view_holder.NotificationVH;

public interface NotificationAdapterContract {

    interface Presenter extends
            BaseListPresenter<Adapter>,
            NotificationVH.ViewHolderClickListener {
        void bindListRowNotification(int position, NotificationRowView rowView);
        long getItemId(int position);
    }

    interface Adapter extends BaseAdapter {
        void onDataSetChanged();
        void onImageLoaded(int position, long viewId, Bitmap encodedImage, boolean isAnimated);
        void openPostUi(String feedId, String postId, String feedUrl, String feedType);
    }

    interface NotificationRowView{
        void setText(String text);
        void setText(Spanned text);
        void setDate(String date);
        void setImage(Bitmap encodedImage, boolean isAnimated);
        void showImage();
        void hideImage();
        void showProfilePlaceHolder();
        void clearImage();
        void showSantaHat();
        void hideSantaHat();
    }

}
