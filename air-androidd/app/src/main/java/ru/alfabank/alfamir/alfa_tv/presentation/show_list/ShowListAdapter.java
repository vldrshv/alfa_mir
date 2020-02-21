package ru.alfabank.alfamir.alfa_tv.presentation.show_list;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import javax.inject.Inject;

import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.contract.ShowListAdapterContract;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.contract.ShowListContract;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.view_holder.ShowCurrentVH;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.view_holder.ShowSeparatorVH;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.view_holder.ShowVH;

import static ru.alfabank.alfamir.Constants.Show.SHOW;
import static ru.alfabank.alfamir.Constants.Show.SHOW_CURRENT;
import static ru.alfabank.alfamir.Constants.Show.SHOW_SEPARATOR;

public class ShowListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ShowListAdapterContract.Adapter {

    private RecyclerView mRecyclerView;
    private ShowListContract.Presenter mPresenter;

    @Inject
    public ShowListAdapter(ShowListContract.Presenter presenter){
        mPresenter = presenter;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mPresenter.takeListAdapter(this);
    }

    @Override
    public int getItemViewType(int position) {
        return mPresenter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case SHOW:{
                return new ShowVH(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.show_viewholder, parent, false), mPresenter);
            }
            case SHOW_CURRENT:{
                return new ShowCurrentVH(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.show_current_viewholder, parent, false), mPresenter);
            }
            case SHOW_SEPARATOR:{
                return new ShowSeparatorVH(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.show_separator_viewholder, parent, false));
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ShowVH){
            ShowVH rowView = (ShowVH) holder;
            mPresenter.bindListRowShow(position, rowView);
        } else if(holder instanceof ShowSeparatorVH) {
            ShowSeparatorVH rowView = (ShowSeparatorVH) holder;
            mPresenter.bindListRowShowSeparator(position, rowView);
        } else if(holder instanceof ShowCurrentVH){
            ShowCurrentVH rowView = (ShowCurrentVH) holder;
            mPresenter.bindListRowShowCurrent(position, rowView);
        }
    }

    @Override
    public int getItemCount() {
        return mPresenter.getListSize();
    }

    @Override
    public void setProgressBarProgress(int position, int progress) {
        ShowCurrentVH showCurrentVH = (ShowCurrentVH) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (showCurrentVH == null) return;
        showCurrentVH.setProgress(progress);
    }

    @Override
    public void setProgressBarColor(int position, int showStatus) {
        ShowCurrentVH showCurrentVH = (ShowCurrentVH) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (showCurrentVH == null) return;
        showCurrentVH.setProgressBarColor(showStatus);
    }

    @Override
    public void clearProgressTimer(int position) {
        ShowCurrentVH showCurrentVH = (ShowCurrentVH) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (showCurrentVH == null) return;
        showCurrentVH.clearProgressTimer();
    }

    @Override
    public void onItemUpdated(int position) {
        notifyItemChanged(position);
    }
}
