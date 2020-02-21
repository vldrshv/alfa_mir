package ru.alfabank.alfamir.people.presentation.post_contract;

import android.graphics.Bitmap;

/**
 * Created by U_M0WY5 on 12.04.2018.
 */

public interface PeopleListContract {

    interface ListPresenter {
        int getListSize();
        void bindListRowView(int position, ListRowView rowView);
        void takeListAdapter(ListAdapter adapter);
        void onItemClicked(int position);
        ListAdapter getListView();
    }

    interface ListRowView {
        void setName(String name);
        void setTitle(String title);
        void setProfileImage(Bitmap binaryImage, boolean isAnimated);
        void setProfilePlaceholder();
        void setInitials(String initials);
        void clearInitials();
    }

    interface ListAdapter {
        void onImageDownloaded(int position, Bitmap binaryImage, boolean isNew);
        void openActivityProfileUi(String id);
    }

}
