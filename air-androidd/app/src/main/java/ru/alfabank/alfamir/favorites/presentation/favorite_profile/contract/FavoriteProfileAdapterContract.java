package ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.base_elements.BaseAdapter;
import ru.alfabank.alfamir.base_elements.BaseListPresenter;
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.view_holder.FavoriteProfileVH;

public interface FavoriteProfileAdapterContract {
    interface Presenter extends BaseListPresenter<Adapter>,
            FavoriteProfileVH.ViewHolderClickListener {
        void bindListRowProfile(ProfileRowView rowView, int position);
    }

    interface Adapter extends BaseAdapter {
        void onItemInserted(int position);
        void onDataSetChanged();
        void delete(int position);
    }

    interface ProfileRowView {
        void setName(String name);
        void setTitle(String title);
        void setImage(Bitmap encodedImage, boolean isAnimated);
        void setPlaceHolder(String initials);
    }
}
