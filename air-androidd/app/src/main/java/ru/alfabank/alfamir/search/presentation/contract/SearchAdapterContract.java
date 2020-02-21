package ru.alfabank.alfamir.search.presentation.contract;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.base_elements.BaseAdapter;
import ru.alfabank.alfamir.base_elements.BaseListPresenter;
import ru.alfabank.alfamir.search.presentation.view_holder.PageMoreVH;
import ru.alfabank.alfamir.search.presentation.view_holder.PageVH;
import ru.alfabank.alfamir.search.presentation.view_holder.PersonMoreVH;
import ru.alfabank.alfamir.search.presentation.view_holder.PersonVH;

public interface SearchAdapterContract {
    interface Presenter extends
            BaseListPresenter<Adapter>,
            PageVH.ViewHolderClickListener,
            PersonVH.ViewHolderClickListener,
            PageMoreVH.ViewHolderClickListener,
            PersonMoreVH.ViewHolderClickListener {
        void bindListRowPerson(int position, SearchPersonRowView rowView);
        void bindListRowPage(int position, SearchPageRowView rowView);
        long getItemId(int position);
    }

    interface Adapter extends BaseAdapter {
        void onDataSetChanged();
        void onProfileImageLoaded(int position, long viewId, String encodedImage, boolean isAnimated);
        void onPostImageLoaded(int position, long viewId, String encodedImage, boolean isAnimated);
        void openPostUi(String feedId, String postId, String feedUrl, String feedType);
    }

    interface SearchPersonRowView{
        void setName(String name);
        void setPosition(String position);
        void setPicture(Bitmap encodedImage, boolean isAnimated);
        void setPlaceHolder(String initials);
        void clearImage();
    }

    interface SearchPageRowView{
        void setTitle(String title);
        void setDate(String date);
        void setPicture(Bitmap encodedImage, boolean isAnimated);
        void clearImage();
        void hideImage();
        void showImage();
    }

}
