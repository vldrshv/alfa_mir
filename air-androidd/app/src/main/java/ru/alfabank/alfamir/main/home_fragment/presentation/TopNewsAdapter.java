package ru.alfabank.alfamir.main.home_fragment.presentation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.main.home_fragment.presentation.contract.TopNewsAdapterContract;
import ru.alfabank.alfamir.main.home_fragment.presentation.view_holder.TopNewsVH;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;

import javax.inject.Inject;

public class TopNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements TopNewsAdapterContract.Adapter{

    private TopNewsAdapterContract.Presenter mPresenter;

    @Inject
    TopNewsAdapter(TopNewsAdapterContract.Presenter presenter, LogWrapper log){
        mPresenter = presenter;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mPresenter.takeListAdapter(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TopNewsVH(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.top_news_viewholder, viewGroup, false), mPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TopNewsVH topNewsVH = (TopNewsVH) viewHolder;
        mPresenter.bindListRowTopNews(i, topNewsVH);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getListSize();
    }
}
