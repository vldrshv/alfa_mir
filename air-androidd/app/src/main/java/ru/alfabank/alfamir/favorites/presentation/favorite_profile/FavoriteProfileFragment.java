package ru.alfabank.alfamir.favorites.presentation.favorite_profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.favorites.SwipeController;
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract.FavoriteProfileContract;
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.view_holder.ProfileDecorator;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;
import ru.alfabank.alfamir.search.presentation.SearchFragment;
import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils;

import javax.inject.Inject;
import java.util.List;

import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_ADD;
import static ru.alfabank.alfamir.Constants.PROFILE_ID;

public class FavoriteProfileFragment extends BaseFragment implements FavoriteProfileContract.View {

    @BindView(R.id.recycler_container)
    RecyclerView recycler;
    @BindView(R.id.favorite_toolbar_fl_search)
    FrameLayout flSearch;
    @BindView(R.id.favorite_post_fragment_toolbar_ll_toggle)
    LinearLayout llToggle;
    @BindView(R.id.favorite_post_fragment_toolbar_tv_profile)
    TextView tvToggleProfile;
    @BindView(R.id.favorite_post_fragment_toolbar_tv_post)
    TextView tvTogglePost;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    FavoriteProfileAdapter mAdapter;
    @Inject
    FavoriteProfileContract.Presenter mPresenter;
    private LinearLayoutManager mLm;

    private int separatorColor = Color.argb(255, 228, 228, 228);
    private int toggleColorActive = Color.argb(255, 133, 133, 133);
    private int toggleColorInactive = Color.argb(255, 255, 255, 255);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.favorite_post_fragment, container, false);
        ButterKnife.bind(this, root);
        tvToggleProfile.setOnClickListener(v -> openProfileFragment());
        tvTogglePost.setOnClickListener(v -> openPostFragment());
        flSearch.setOnClickListener(v -> mPresenter.onSearchClicked());
        llToggle.setBackgroundResource(R.drawable.toggle_blue_two_elements_left);
        tvToggleProfile.setTextColor(toggleColorInactive);
        tvTogglePost.setTextColor(toggleColorActive);
        swipeRefreshLayout.setEnabled(false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    public void showFavoriteList() {
        recycler.setAdapter(mAdapter);
        mLm = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(mLm);
        recycler.addItemDecoration(new ProfileDecorator(separatorColor, 1));
        SwipeController swipeController = new SwipeController(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                mAdapter.delete(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeController);
        touchHelper.attachToRecyclerView(recycler);
    }

    @Override
    public void openProfileActivityUi(String userLogin) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(PROFILE_ID, userLogin);
        startActivity(intent);
    }

    @Override
    public void openSearchUi() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.favorite_container, new SearchFragment(), null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void showLoadingProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    void openPostFragment() {

        List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment.isVisible()) {
                fragment.getChildFragmentManager().popBackStack();
            }
        }
    }

    void openProfileFragment() {
        ActivityUtils.addFragmentToActivity(getFragmentManager(), new FavoriteProfileFragment(), R.id.activity_main_content_frame, FRAGMENT_TRANSITION_ADD, false);
    }
}