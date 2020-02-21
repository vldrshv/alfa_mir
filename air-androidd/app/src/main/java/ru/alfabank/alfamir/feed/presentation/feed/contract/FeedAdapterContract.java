package ru.alfabank.alfamir.feed.presentation.feed.contract;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.main.main_feed_fragment.contract.MainFeedListContract;
import ru.alfabank.alfamir.main.main_feed_fragment.view_holder.PostViewHolder;
import ru.alfabank.alfamir.feed.presentation.feed.view_holders.HeaderVH;
import ru.alfabank.alfamir.base_elements.BaseAdapter;
import ru.alfabank.alfamir.base_elements.BaseListPresenter;

public interface FeedAdapterContract {

    interface Presenter extends BaseListPresenter<Adapter>, PostViewHolder.ViewHolderClickListener, HeaderVH.ViewHolderClickListener {
        void bindListRowPost(int position, MainFeedListContract.PostRowView rowView);
        void bindListRowHeader(int position, FeedAdapterContract.HeaderRowView rowView);
        long getItemId(int position);
        void onLoadMore();
    }

    interface Adapter extends BaseAdapter {
        void onPostOptionsClicked(int position, int isDeletable, String favoriteStatus, String subscribeStatus);
        void onSubscribeOptionClicked(String subscribeOption, int position);
        void setSubscriptionStateChanged(int isSubscribed);
        void setLikeStatus(int position, String likesCount, int isLiked);
        void onItemRemoved(int position);
        void onDataSetChanged();
        void onItemsInserted(int start, int count);
    }

    interface HeaderRowView {
        void setAuthorImage(Bitmap encodedImage, boolean isAnimated);
        void setBackgroundImage(Bitmap encodedImage, boolean isAnimated);
        void setSubscriptionStateButtonVisibility(boolean isVisible);
        void setSubscriptionState(int isSubscribed);
        void setFeedTitle(String title);
        void setFeedDescription(String description);
        void hideDescription();
        void showOptions(String subscribeOption);
        void hideAuthorImage();
    }

}
