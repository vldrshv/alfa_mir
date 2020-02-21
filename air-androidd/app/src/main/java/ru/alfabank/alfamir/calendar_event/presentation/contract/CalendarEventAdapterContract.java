package ru.alfabank.alfamir.calendar_event.presentation.contract;


import android.graphics.Bitmap;
import androidx.fragment.app.Fragment;
import ru.alfabank.alfamir.base_elements.BaseAdapter;
import ru.alfabank.alfamir.base_elements.BaseListPresenter;
import ru.alfabank.alfamir.calendar_event.presentation.view_holder.CalendarEventCardVH;

public interface CalendarEventAdapterContract {

    interface Presenter extends BaseListPresenter<Adapter>, CalendarEventCardVH.VHClickListener {
        void bindListRowCalendarEvent(int position, CalendarEventCardRowView rowView);

        void onHostViewResume(CalendarEventLoadListener callback);

        void onHostViewDestroy();

        void setFragmentView(Fragment fragment);
    }

    interface Adapter extends BaseAdapter {
        void onDataSetChanged();

        void onHostViewResume(CalendarEventLoadListener callback);

        void onHostViewDestroy();
    }

    interface CalendarEventCardRowView {
        void setTitle(String title);

        void setPicture(Bitmap encodedImage, boolean isAnimated);

        void setDate(String date);

        void setTime(String time);
    }

    interface CalendarEventLoadListener {
        void onDataLoaded();
    }

}
