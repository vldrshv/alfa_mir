package ru.alfabank.alfamir.alfa_tv.presentation.show_list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.alfa_tv.presentation.show.ShowActivity;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.contract.ShowListContract;
import ru.alfabank.alfamir.base_elements.BaseFragment;

import javax.inject.Inject;

import static ru.alfabank.alfamir.Constants.Show.SHOW_ID;
import static ru.alfabank.alfamir.Constants.Show.SHOW_POSITION;

public class ShowListFragment extends BaseFragment implements ShowListContract.View{

    @BindView(R.id.recycler_container) RecyclerView recyclerView;
    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeRL;
    @BindView(R.id.feed_with_header_toolbar_fl_back) FrameLayout flBack;
    @Inject ShowListAdapter mAdapter;
    @Inject ShowListContract.Presenter mPresenter;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private LinearLayoutManager mLm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.show_list_fragment, container, false);
        ButterKnife.bind(this, root);
        flBack.setOnClickListener(v -> {
            Fragment parent = getParentFragment();
            if (parent != null) {
                parent.getChildFragmentManager().popBackStack();
                return;
            }
            getActivity().onBackPressed();
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!checkIfInitialized()) return;
        mPresenter.takeView(this);
    }

    @Override
    public void showShows() {
        recyclerView.setAdapter(mAdapter);
        mLm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLm);
    }

    @Override
    public void openShowUi(int showPosition, int id) {
        Intent intent = new Intent(getActivity(), ShowActivity.class);
        intent.putExtra(SHOW_POSITION, showPosition);
        intent.putExtra(SHOW_ID, id);
        getActivity().startActivity(intent);
    }

    @Override
    public void showSnackBar(String text) {
        Snackbar snackbar = Snackbar.make(recyclerView, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void enableRefresh() {
        swipeRL.setOnRefreshListener(() -> mPresenter.onListRefresh());
    }

    @Override
    public void showProgressBar() {
        mMainHandler.post(() -> swipeRL.setRefreshing(true));
    }

    @Override
    public void hideProgressBar() {
        mMainHandler.postDelayed(() -> swipeRL.setRefreshing(false), 500);
    }

    public void disableRefresh(){
        swipeRL.setEnabled(false);
        swipeRL.setRefreshing(false);
    }

}
