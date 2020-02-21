package ru.alfabank.alfamir.feed.presentation.feed;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import javax.inject.Inject;

import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.feed.presentation.feed.contract.FeedAdapterContract;
import ru.alfabank.alfamir.feed.presentation.feed.contract.FeedContract;
import ru.alfabank.alfamir.feed.presentation.feed.view_holders.HeaderVH;
import ru.alfabank.alfamir.main.main_feed_fragment.view_holder.PostViewHolder;

import static ru.alfabank.alfamir.Constants.Feed_element.FEED_HEADER;
import static ru.alfabank.alfamir.Constants.Feed_element.FEED_POST;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FeedAdapterContract.Adapter {

    private RecyclerView mRecyclerView;
    private FeedContract.Presenter mPresenter;

    @Inject
    public FeedAdapter(FeedContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mPresenter.takeListAdapter(this);
    }

    @Override
    public long getItemId(int position) {
        return mPresenter.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mPresenter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FEED_POST:
                return new PostViewHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.feed_post_viewholder, parent, false), mPresenter);
            case FEED_HEADER:
                return new HeaderVH(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.feed_header_viewholder, parent, false), mPresenter);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostViewHolder) {
            PostViewHolder rowView = (PostViewHolder) holder;
            mPresenter.bindListRowPost(position, rowView);
        } else if (holder instanceof HeaderVH) {
            HeaderVH rowView = (HeaderVH) holder;
            mPresenter.bindListRowHeader(position, rowView);
        }
    }

    @Override
    public int getItemCount() {
        return mPresenter.getListSize();
    }

    @Override
    public void onPostOptionsClicked(int position, int isDeletable, String favoriteStatus, String subscribeStatus) {
        PostViewHolder viewHolder = (PostViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) return;
        viewHolder.showOptions(isDeletable, favoriteStatus, subscribeStatus);
    }

    @Override
    public void onSubscribeOptionClicked(String subscribeOption, int position) {
        HeaderVH viewHolder = (HeaderVH) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) return;
        viewHolder.showOptions(subscribeOption);
    }

    @Override
    public void setSubscriptionStateChanged(int isSubscribed) {
        HeaderVH viewHolder = (HeaderVH) mRecyclerView.findViewHolderForAdapterPosition(0);
        if (viewHolder == null) return;
        viewHolder.setSubscriptionState(isSubscribed);
    }

    @Override
    public void setLikeStatus(int position, String likesCount, int isLiked) {
        PostViewHolder viewHolder = (PostViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {
            notifyItemChanged(position);
            return;
        }
        viewHolder.setLikeStatus(likesCount, isLiked);
    }

    @Override
    public void onItemRemoved(int position) {
        notifyItemRemoved(position);
    }

    @Override
    public void onDataSetChanged() {
        notifyDataSetChanged();
    }

    @Override
    public void onItemsInserted(int start, int count) {
        notifyItemRangeInserted(start, count);
    }
}
