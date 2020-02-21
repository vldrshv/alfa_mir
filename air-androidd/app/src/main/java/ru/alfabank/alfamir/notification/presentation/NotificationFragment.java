package ru.alfabank.alfamir.notification.presentation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.main.main_activity.MainActivity;
import ru.alfabank.alfamir.main.main_activity.contract.MainActivityContract;
import ru.alfabank.alfamir.notification.presentation.contract.NotificationContract;
import ru.alfabank.alfamir.notification.presentation.view_holder.NotificationDecorator;
import ru.alfabank.alfamir.post.presentation.post.PostActivity;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;
import ru.alfabank.alfamir.survey.presentation.SurveyActivity;

import static ru.alfabank.alfamir.Constants.INTENT_SOURCE;
import static ru.alfabank.alfamir.Constants.INTENT_SOURCE_NOTIFICATION;
import static ru.alfabank.alfamir.Constants.PROFILE_ID;
import static ru.alfabank.alfamir.Constants.Post.FEED_ID;
import static ru.alfabank.alfamir.Constants.Post.FEED_TYPE;
import static ru.alfabank.alfamir.Constants.Post.FEED_URL;
import static ru.alfabank.alfamir.Constants.Post.POST_ID;
import static ru.alfabank.alfamir.Constants.QUIZ_ID;

public class NotificationFragment extends BaseFragment implements NotificationContract.View {

    @Inject
    NotificationContract.Presenter mPresenter;
    @BindView(R.id.recycler_container) RecyclerView mRecycler;
    @BindView(R.id.swipe_container) SwipeRefreshLayout swContainer;
    @BindView(R.id.notification_toolbar_fl_back) FrameLayout flBack;

    private MainActivityContract.View mParentView;
    private LinearLayoutManager mLm;
    @Inject
    public NotificationAdapter mAdapter;
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private int lastVisibleItem;
    private int visibleThreshold = 8;

    private int separatorColor = Color.argb(255, 228, 228, 228);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParentView = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notification_fragment, container, false);
        ButterKnife.bind(this, root);
        flBack.setOnClickListener(v -> getActivity().onBackPressed());
        swContainer.setEnabled(false);
        return root;
    }

    @Override
    public void showNotifications() {
        mAdapter.setHasStableIds(true); // TODO must uncomment
        mRecycler.setAdapter(mAdapter);
        mLm = new LinearLayoutManager(getContext());
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mRecycler.getChildCount();
                totalItemCount = mLm.getItemCount();
                firstVisibleItem = mLm.findFirstVisibleItemPosition();
                lastVisibleItem = mLm.findLastVisibleItemPosition();
                if (totalItemCount!=0 && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    mPresenter.onLoadMore();
                }
            }
        });
        mRecycler.addItemDecoration(new NotificationDecorator(separatorColor, 1));
        mRecycler.setLayoutManager(mLm);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
        mParentView.hideBottomNavBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mParentView.showBottomNavBar();
    }

    @Override
    public void openPostUi(String postId, String feedUrl, String feedType){
        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra(POST_ID, postId);
        intent.putExtra(FEED_URL, feedUrl);
        intent.putExtra(FEED_ID, feedUrl);
        intent.putExtra(FEED_TYPE, feedType);
        getContext().startActivity(intent);
    }

    @Override
    public void openProfileUi(String profileLogin){
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(PROFILE_ID, profileLogin);
        getContext().startActivity(intent);
    }

    @Override
    public void openSurveyUi(String quizId){
        Intent intent = new Intent(getContext(), SurveyActivity.class);
        intent.putExtra(QUIZ_ID, quizId);
        intent.putExtra(INTENT_SOURCE, INTENT_SOURCE_NOTIFICATION);
        getContext().startActivity(intent);
    }

    @Override
    public void showLoading() {
        swContainer.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(()-> {
            swContainer.setRefreshing(false);
        }, 500);

    }

}
