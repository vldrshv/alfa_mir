package ru.alfabank.alfamir.main.main_feed_fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import javax.inject.Inject;

import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.feed.presentation.feed.FeedActivity;
import ru.alfabank.alfamir.main.main_feed_fragment.contract.MainFeedListContract;
import ru.alfabank.alfamir.main.main_feed_fragment.view_holder.PostViewHolder;
import ru.alfabank.alfamir.main.main_feed_fragment.view_holder.SurveyViewHolder;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;
import ru.alfabank.alfamir.survey.presentation.SurveyActivity;

import static ru.alfabank.alfamir.Constants.INTENT_SOURCE;
import static ru.alfabank.alfamir.Constants.INTENT_SOURCE_CARD;
import static ru.alfabank.alfamir.Constants.Post.FEED_ID;
import static ru.alfabank.alfamir.Constants.Post.FEED_TITLE;
import static ru.alfabank.alfamir.Constants.Post.FEED_TYPE;
import static ru.alfabank.alfamir.Constants.Post.FEED_URL;
import static ru.alfabank.alfamir.Constants.Post.POST_CREATION_ENABLED;
import static ru.alfabank.alfamir.Constants.QUIZ_ID;
import static ru.alfabank.alfamir.Constants.VIEW_HOLDER_TYPE_NEWS;
import static ru.alfabank.alfamir.Constants.VIEW_HOLDER_TYPE_QUIZ;

public class MainFeedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MainFeedListContract.Adapter,
        SurveyViewHolder.ViewHolderClickListener, PostViewHolder.ViewHolderClickListener {

    private static MainFeedListContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;


    @Override
    public Context getContext() {
        return mRecyclerView.getContext();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Inject
    public MainFeedListAdapter(MainFeedListPresenter presenter){
        mPresenter = presenter;
        mPresenter.takeListAdapter(this);
    }

    @Override
    public int getItemViewType(int position) {
        return mPresenter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch (i) {
            case VIEW_HOLDER_TYPE_QUIZ:{
                return new SurveyViewHolder(LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.feed_survey_viewholder, viewGroup, false), this);
            }
            case VIEW_HOLDER_TYPE_NEWS:{
                return new PostViewHolder(LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.feed_post_viewholder, viewGroup, false), this);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof PostViewHolder){
            PostViewHolder profileHolder = (PostViewHolder) viewHolder;
            mPresenter.bindListRowPostView(position, profileHolder);
        } else if (viewHolder instanceof SurveyViewHolder){
            SurveyViewHolder quizViewHolder = (SurveyViewHolder) viewHolder;
            mPresenter.bindListRowSurveyView(position, quizViewHolder);
        }
    }

    @Override
    public int getItemCount() { return mPresenter.getListSize(); }

    @Override
    public void onAuthorAvatarDownloaded(int position, Bitmap binaryImage, boolean isAnimated) {
        PostViewHolder viewHolder = (PostViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {

            notifyItemChanged(position);
            return;
        }
        viewHolder.setAuthorAvatar(binaryImage, isAnimated);
    }

    @Override
    public void onPostImageDownloaded(int position, Bitmap binaryImage, boolean isAnimated) {
        PostViewHolder viewHolder = (PostViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {
            notifyItemChanged(position);
            return;
        }
        viewHolder.setPostImage(binaryImage, isAnimated);
    }

    @Override
    public void onSurveyImageDownload(int position, Bitmap binaryImage, boolean isAnimated) {
        SurveyViewHolder viewHolder = (SurveyViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {
            notifyItemChanged(position);
            return;
        }
        viewHolder.setImage(binaryImage, isAnimated);
    }

    @Override
    public void onPostOptionsClicked(int position, int isDeletable, String favoriteStatus, String subscribeStatus) {
        PostViewHolder viewHolder = (PostViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) return;
        viewHolder.showOptions(isDeletable, favoriteStatus, subscribeStatus);
    }

    @Override
    public void openSurveyActivityUi(String surveyId) {
        Intent intent = new Intent(mRecyclerView.getContext(), SurveyActivity.class);
        intent.putExtra(QUIZ_ID, surveyId);
        intent.putExtra(INTENT_SOURCE, INTENT_SOURCE_CARD);
        mRecyclerView.getContext().startActivity(intent);
    }

    @Override
    public void openProfileActivityUi(String id) {
        Intent intent = new Intent(mRecyclerView.getContext(), ProfileActivity.class);
        intent.putExtra("id", id);
        mRecyclerView.getContext().startActivity(intent);
    }

    @Override
    public void openNewsActivityUi(String title, String type, String postUrl) {
        Intent intent = new Intent(mRecyclerView.getContext(), FeedActivity.class);
        boolean postCreationEnabled = postUrl.equals("http://alfa/Info/Stories");

        intent.putExtra(FEED_ID, postUrl);
        intent.putExtra(FEED_URL, postUrl);
        intent.putExtra(FEED_TYPE, type);
        intent.putExtra(FEED_TITLE, title);
        intent.putExtra(POST_CREATION_ENABLED, postCreationEnabled);
        mRecyclerView.getContext().startActivity(intent);
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
    public void removePostAtPosition(int position) { notifyItemRemoved(position); }

    @Override
    public void onPostClicked(int position) { mPresenter.onPostClicked(position); }

    @Override
    public void onAuthorClicked(int position) { mPresenter.onAuthorClicked(position); }

    @Override
    public void onHeadlineTitleClicked(int position) { mPresenter.onHeadlineTitleClicked(position); }

    @Override
    public void onCommentsClicked(int position) { mPresenter.onCommentsClicked(position); }

    @Override
    public void onLikeClicked(int position) { mPresenter.onLikeClicked(position); }

    @Override
    public void onOptionsClicked(int position) { mPresenter.onOptionsClicked(position); }

    @Override
    public void onMenuItemDeleteClicked(int position) { mPresenter.onMenuItemDeleteClicked(position); }

    @Override
    public void onMenuItemFavoriteClicked(int position) { mPresenter.onMenuItemFavoriteClicked(position); }

    @Override
    public void onMenuItemSubscribeClicked(int position) { mPresenter.onMenuItemSubscribeClicked(position); }

    @Override
    public void onQuizClicked() { mPresenter.onQuizClicked(); }

    @Override
    public void onQuizHideClicked() { mPresenter.onQuizHideClicked(); }

}
