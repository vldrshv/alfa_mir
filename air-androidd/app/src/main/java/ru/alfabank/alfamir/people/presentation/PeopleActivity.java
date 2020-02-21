package ru.alfabank.alfamir.people.presentation;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.data.dto.ShortProfile;
import ru.alfabank.alfamir.people.presentation.post_contract.PeopleContract;
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper;

import javax.inject.Inject;
import java.util.List;

public class PeopleActivity extends BaseActivity implements PeopleContract.View {

    @BindView(R.id.activity_people_ll_back)
    LinearLayout llBack;
    @BindView(R.id.activity_people_swipe)
    SwipeRefreshLayout swipeView;
    @BindView(R.id.activity_people_container)
    RecyclerView recyclerView;
    @BindView(R.id.activity_people_fl_profile)
    FrameLayout flProfile;
    @BindView(R.id.activity_people_image_profile)
    ImageView imageProfile;
    @BindView(R.id.activity_people_image_add_photo)
    ImageView imageAddPhoto;
    @BindView(R.id.activity_people_ll_empty)
    LinearLayout llEmptyContainer;

    @Inject
    PeopleContract.Presenter presenter;

    @Inject
    PeopleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_activity);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);

        llBack.setOnClickListener(view -> onBackPressed());
        flProfile.setOnClickListener(view -> presenter.like());
        swipeView.setOnRefreshListener(() -> presenter.refreshList());
    }

    @Override
    public void showProfiles(List<ShortProfile> profiles) {
        swipeView.setVisibility(View.VISIBLE);
        llEmptyContainer.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
    }

    @Override
    public void dismiss() {
        onBackPressed();
    }

    @Override
    public void showEmptyState(boolean isMyProfile, Bitmap binaryImage) {
        swipeView.setVisibility(View.GONE);
        llEmptyContainer.setVisibility(View.VISIBLE);
        RoundedBitmapDrawable roundedImage = PictureClipper.makeItRound(binaryImage);
        imageProfile.setImageDrawable(roundedImage);
    }

    @Override
    public void showProgressBar() {
        swipeView.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        Handler handler = new Handler();
        handler.postDelayed(() -> swipeView.setRefreshing(false), 10);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkIfInitialized()) return;
        presenter.takeView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}
