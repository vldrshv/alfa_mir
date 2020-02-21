package ru.alfabank.alfamir.favorites.presentation.favorite_post.contract;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.base_elements.BaseAdapter;
import ru.alfabank.alfamir.base_elements.BaseListPresenter;
import ru.alfabank.alfamir.favorites.presentation.favorite_post.view_holder.FavoritePostVH;

public interface FavoritePostAdapterContract {

    interface Presenter extends BaseListPresenter<Adapter>, FavoritePostVH.ViewHolderClickListener {
        void bindListRowPage(PageRowView rowView, int position);
    }

    interface Adapter extends BaseAdapter {
        //        void onImageLoaded(int position, long viewId, String encodedImage, boolean isAnimated);
        void onItemInserted(int position);
        void onDataSetChanged();
        void delete(int position);
    }

    interface PageRowView {
        void clearData();
        void setTitle(String title);
        void setDate(String date);
        void setImage(Bitmap encodedImage, boolean isAnimated);
        void hideImage();
        void showImage();
    }
}
