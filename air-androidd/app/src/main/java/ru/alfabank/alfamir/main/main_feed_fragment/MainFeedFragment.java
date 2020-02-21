package ru.alfabank.alfamir.main.main_feed_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.main.main_feed_fragment.contract.MainFeedContract;
import ru.alfabank.alfamir.search.presentation.SearchFragment;
import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils;

import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_ADD;


public class MainFeedFragment extends BaseFragment implements MainFeedContract.View {

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout srlBaseParent;
    @BindView(R.id.recycler_container)
    RecyclerView rvNewsContainer;
    @BindView(R.id.main_feed_toolbar_fl_search)
    FrameLayout flSearch;

    @Inject
    MainFeedContract.Presenter mPresenter;
    @Inject
    MainFeedListAdapter adapter;

    private int totalItemCount;
    private int lastVisibleItem;
    private int visibleThreshold = 4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_feed_fragment, container, false);
        ButterKnife.bind(this, root);

        srlBaseParent.setOnRefreshListener(() -> mPresenter.onListRefresh());
        flSearch.setOnClickListener(v -> mPresenter.onSearchClicked());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkIfInitialized()) return;
        mPresenter.takeView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

    @Override
    public void updateSurvey() {
        adapter.notifyItemChanged(0);
    }

    @Override
    public void showFeed() {
        rvNewsContainer.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvNewsContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        rvNewsContainer.setLayoutManager(lm);
    }

    @Override
    public void showMoreNews(int firstNewPosition, int lastNewPosition) {
        for (int i = firstNewPosition; i < lastNewPosition; i++) {
            adapter.notifyItemChanged(i);
        }
    }

    @Override
    public void showConnectionErrorSnackBar(String errorText, String retryText, int actionType) {
        Snackbar snackbar = Snackbar.make(rvNewsContainer, errorText, Snackbar.LENGTH_SHORT);
        snackbar.setAction(retryText, view -> mPresenter.onErrorSnackBarActionClicked(actionType));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    mPresenter.onErrorSnackBarDismissedItself();
                }
            }
        });
        snackbar.show();
    }

    @Override
    public void openSearchUi() {
        ActivityUtils.addFragmentToActivity(getFragmentManager(), new SearchFragment(), R.id.activity_main_content_frame, FRAGMENT_TRANSITION_ADD, true);
    }

    @Override
    public void showProgressBar() {
        srlBaseParent.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        srlBaseParent.setRefreshing(false);
    }

    @Override
    public void clearFeed() {
        adapter.notifyDataSetChanged();
    }
}
