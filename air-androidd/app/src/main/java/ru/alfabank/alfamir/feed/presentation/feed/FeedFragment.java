package ru.alfabank.alfamir.feed.presentation.feed;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.di.qualifiers.feed.ToolbarVisibility;
import ru.alfabank.alfamir.feed.presentation.feed.contract.FeedContract;
import ru.alfabank.alfamir.post.presentation.post.PostActivity;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;
import ru.alfabank.alfamir.ui.activities.ActivityPublishMedia;

import static ru.alfabank.alfamir.Constants.PROFILE_ID;

public class FeedFragment extends BaseFragment implements FeedContract.View {

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout srContainer;
    @BindView(R.id.recycler_container)
    RecyclerView rvContainer;
    @Inject
    FeedContract.Presenter mPresenter;
    @Inject
    FeedAdapter mAdapter;
    @Inject
    @ToolbarVisibility
    boolean isToolbarVisible;
    private int totalItemCount;
    private int lastVisibleItem;
    private int visibleThreshold = 4;
    private LinearLayoutManager lm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.feed_fragment, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srContainer.setOnRefreshListener(() -> mPresenter.onListRefresh());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkIfInitialized()) return;
        mPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

    @Override
    public void showFeed() {
        rvContainer.setAdapter(mAdapter);
        lm = new LinearLayoutManager(getContext());
        rvContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = lm.getItemCount();
                lastVisibleItem = lm.findLastVisibleItemPosition();
                if (totalItemCount != 0 && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    mPresenter.onLoadMore();
                }
            }
        });
        rvContainer.setLayoutManager(lm);
    }

    @Override
    public void showProgressBar() {
        srContainer.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        srContainer.setRefreshing(false);
    }

    @Override
    public void showSnackBar(String text) {
        if (text == null) return;
        Snackbar snackbar = Snackbar.make(rvContainer, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void openPostUi(String feedId, String postId, String feedUrl, String feedType, int position, boolean startWithComments) {
        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra(Constants.Post.FEED_ID, feedId);
        intent.putExtra(Constants.Post.POST_ID, postId);
        intent.putExtra(Constants.Post.FEED_URL, feedUrl);
        intent.putExtra(Constants.Post.FEED_TYPE, feedType);
        intent.putExtra(Constants.Post.POST_POSITION, position);
        intent.putExtra(Constants.Post.COMMENTS_FIRST, startWithComments);
        getContext().startActivity(intent);
    }

    @Override
    public void openProfileUi(String id) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(PROFILE_ID, id);
        startActivity(intent);
    }

    protected void openCreateMediaActivityUi() {
        Intent intent = new Intent(this.getContext(), ActivityPublishMedia.class);
        startActivity(intent);
    }
}
