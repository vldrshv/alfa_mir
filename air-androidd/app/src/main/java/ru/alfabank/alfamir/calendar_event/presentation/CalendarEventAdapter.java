package ru.alfabank.alfamir.calendar_event.presentation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.calendar_event.presentation.contract.CalendarEventAdapterContract;
import ru.alfabank.alfamir.calendar_event.presentation.view_holder.CalendarEventCardVH;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;

import javax.inject.Inject;

public class CalendarEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements CalendarEventAdapterContract.Adapter {

    private CalendarEventAdapterContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private LogWrapper mLog;
    private Fragment fragment;

    @Inject
    CalendarEventAdapter(CalendarEventAdapterContract.Presenter presenter, LogWrapper log) {
        mPresenter = presenter;
        mLog = log;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mPresenter.takeListAdapter(this);
        mPresenter.setFragmentView(fragment);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CalendarEventCardVH(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.calendar_event_card_viewholder, viewGroup, false), mPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        CalendarEventCardVH calendarEventCardVH = (CalendarEventCardVH) viewHolder;
        mPresenter.bindListRowCalendarEvent(i, calendarEventCardVH);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getListSize();
    }

    @Override
    public void onDataSetChanged() {
        notifyDataSetChanged();
    }

    @Override
    public void onHostViewResume(CalendarEventAdapterContract.CalendarEventLoadListener callback) {
        mPresenter.onHostViewResume(callback);
    }

    @Override
    public void onHostViewDestroy() {
        mPresenter.onHostViewDestroy();
    }

    public void setFragmentView(Fragment fragment) {
        this.fragment = fragment;
    }

}
