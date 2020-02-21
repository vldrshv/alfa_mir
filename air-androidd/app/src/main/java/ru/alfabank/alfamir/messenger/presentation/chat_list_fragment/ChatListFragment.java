package ru.alfabank.alfamir.messenger.presentation.chat_list_fragment;

import android.content.Context;
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
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.main.main_activity.MainActivity;
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.contract.ChatListContract;

public class ChatListFragment extends BaseFragment implements ChatListContract.View {
    @BindView(R.id.fragment_chat_list_swipe_refresh_view)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fragment_chat_list_recycler_view)
    RecyclerView recycler;
    @BindView(R.id.chat_list_toolbar_back)
    FrameLayout flBack;
    @BindView(R.id.fragment_chat_list_image_empty_state)
    ImageView imageEmptyState;
    @Inject
    ChatListAdapter mAdapter;
    @Inject
    ChatListContract.Presenter mPresenter;
    private LinearLayoutManager mLm;
    private OnChatSelectedListener mOnChatSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mOnChatSelectedListener = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.chat_list_fragment, container, false);
        ButterKnife.bind(this, root);
        flBack.setOnClickListener(view -> getActivity().onBackPressed());
        refreshLayout.setOnRefreshListener(() -> mPresenter.onRefresh());
        return root;
    }

    @Override
    public void showChatList() {
        mAdapter.setHasStableIds(true);
        recycler.setAdapter(mAdapter);
        mLm = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(mLm);
    }

    @Override
    public void showChatUi(String userId, String chatId, String type) {
        mOnChatSelectedListener.onChatSelected(userId, chatId, type);
    }

    @Override
    public void setRefreshState(boolean isRefreshing) {
        if (isRefreshing) {
            refreshLayout.setRefreshing(isRefreshing);
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> refreshLayout.setRefreshing(isRefreshing), 400);
        }
    }

    @Override
    public void setEnabledState(boolean isActive) {
//        refreshLayout.setEnabled(isActive);
    }

    @Override
    public void showEmptyMessage() {
        imageEmptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyMessage() {
        imageEmptyState.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
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

    public interface OnChatSelectedListener {
        void onChatSelected(String userId, String chatId, String chatType);
    }

}
