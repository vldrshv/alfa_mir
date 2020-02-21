package ru.alfabank.alfamir.alfa_tv.presentation.show_list.contract;

import ru.alfabank.alfamir.alfa_tv.presentation.show_list.view_holder.ShowCurrentVH;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.view_holder.ShowVH;
import ru.alfabank.alfamir.base_elements.BaseAdapter;
import ru.alfabank.alfamir.base_elements.BaseListPresenter;

public interface ShowListAdapterContract {

    interface Presenter extends BaseListPresenter<Adapter>,
            ShowVH.ViewHolderClickListener, ShowCurrentVH.ViewHolderClickListener{
        void bindListRowShow(int position, ShowView rowView);
        void bindListRowShowCurrent(int position, ShowCurrentView rowView);
        void bindListRowShowSeparator(int position, ShowSeparatorView rowView);
    }

    interface Adapter extends BaseAdapter {
        void setProgressBarProgress(int position, int progress);
        void setProgressBarColor(int position, int showStatus);
        void clearProgressTimer(int position);
        void onItemUpdated(int position);
    }

    interface ShowView {
        void setTitle(String title);
        void setTime(String date);
        void showPasswordRequired(boolean isSaved);
        void hidePasswordRequired();
    }

    interface ShowCurrentView {
        void setTitle(String title);
        void setStartTime(String date);
        void setEndTime(String date);
        void setProgress(int progress);
        void setProgressBarColor(int showStatus);
        void setProgressTimer(long millisUntilFinished, long oneTick);
        void clearProgressTimer();
        void showPasswordRequired(boolean isSaved);
        void hidePasswordRequired();
    }

    interface ShowSeparatorView {
        void setTitle(String title);
    }

}
