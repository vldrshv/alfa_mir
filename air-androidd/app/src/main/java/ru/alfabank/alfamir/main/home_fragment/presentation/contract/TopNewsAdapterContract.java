package ru.alfabank.alfamir.main.home_fragment.presentation.contract;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.base_elements.BaseAdapter;
import ru.alfabank.alfamir.base_elements.BaseListPresenter;
import ru.alfabank.alfamir.main.home_fragment.presentation.view_holder.TopNewsVH;

public interface TopNewsAdapterContract {

    interface Presenter extends BaseListPresenter<Adapter>, TopNewsVH.TopNewsVHClickListener {
        void bindListRowTopNews(int position, TopNewsRowView rowView);

        long getItemId(int position);
    }

    interface Adapter extends BaseAdapter {
    }

    interface TopNewsRowView {
        void setTitle(String title);

        void setPicture(Bitmap encodedImage, boolean isAnimated);
    }

}
