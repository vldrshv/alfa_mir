package ru.alfabank.alfamir.main.home_fragment.presentation;//package ru.alfabank.alfamir.main.home_fragment.presentation;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.PagerSnapHelper;
//import android.support.v7.widget.RecyclerView;
//import android.util.Base64;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import ru.alfabank.alfamir.R;
//import ru.alfabank.alfamir.base_elements.BaseFragment;
//import ru.alfabank.alfamir.calendar_event.presentation.CalendarEventAdapter;
//import ru.alfabank.alfamir.calendar_event.presentation.EventDecoration;
//import ru.alfabank.alfamir.main.home_fragment.presentation.contract.HomeFragmentContract;
//import ru.alfabank.alfamir.main.menu_fragment.presentation.MenuFragment;
//import ru.alfabank.alfamir.notification.presentation.NotificationFragment;
//import ru.alfabank.alfamir.post.presentation.post.PostActivity;
//import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;
//import ru.alfabank.alfamir.profile.presentation.profile.WarningOnExtraChargeDialogFragment;
//import ru.alfabank.alfamir.search.presentation.SearchFragment;
//import ru.alfabank.alfamir.ui.activities.ActivityCreatePost;
//import ru.alfabank.alfamir.utility.image_cropper.ImageCropper;
//import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils;
//
//import javax.inject.Inject;
//
//import static ru.alfabank.alfamir.Constants.*;
//import static ru.alfabank.alfamir.Constants.Feed.NEWS_TYPE_MY_ALFA_LIFE;
//import static ru.alfabank.alfamir.Constants.Initialization.DENSITY;
//import static ru.alfabank.alfamir.Constants.Post.POST_URL;
//
//public class HomeFragment extends BaseFragment implements HomeFragmentContract.View {
//
//    @BindView(R.id.home_fragment_toolbar_menu)
//    FrameLayout flMenu;
//    @BindView(R.id.home_fragment_toolbar_notifications)
//    FrameLayout flNotifications;
//    @BindView(R.id.home_fragment_toolbar_image_notifications)
//    ImageView mImageNotification;
//    @BindView(R.id.home_fragment_toolbar_new_post)
//    FrameLayout flNewPost;
//    @BindView(R.id.home_fragment_toolbar_image_avatar)
//    ImageView imageAvatar;
//    @BindView(R.id.home_fragment_search)
//    LinearLayout llSearch;
//    @BindView(R.id.home_fragment_top_news_fl_temp_background)
//    FrameLayout flTempBackground;
//    @BindView(R.id.human_help_image_call)
//    ImageView imageHumanHelp;
//    @BindView(R.id.human_desk_image_call)
//    ImageView imageHumanDesk;
//    @BindView(R.id.recycler_container)
//    RecyclerView mRecycler;
//    @BindView(R.id.recycler_container_2)
//    RecyclerView mRecycler2;
//
//    @Inject
//    HomeFragmentContract.Presenter mPresenter;
//    @Inject
//    ImageCropper mImageCropper;
//    @Inject
//    TopNewsAdapter mAdapter;
//    @Inject
//    CalendarEventAdapter mCalendarEventAdapter;
//
//    private int separatorColor = Color.argb(255, 250, 250, 250);
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.home_fragment, container, false);
//        ButterKnife.bind(this, root);
//        flMenu.setOnClickListener(v -> ActivityUtils.addFragmentToActivity(getFragmentManager(),
//                new MenuFragment(), R.id.activity_main_content_frame,
//                FRAGMENT_TRANSITION_ADD_TO_LEFT, true));
//        imageAvatar.setOnClickListener(v -> mPresenter.onProfileClicked());
//        flNotifications.setOnClickListener(v -> mPresenter.onNotificationClicked());
//        llSearch.setOnClickListener(v -> mPresenter.onSearchClicked());
//        imageHumanHelp.setOnClickListener(v -> mPresenter.onCallHumanHelpClicked());
//        imageHumanDesk.setOnClickListener(v -> mPresenter.onCallHumanDeskClicked());
//        mRecycler2.addItemDecoration(new EventDecoration(separatorColor, 12));
//
//        flNewPost.setOnClickListener(view -> {
//            Intent intent = new Intent(getContext(),
//                    ActivityCreatePost.class);
//            int feedId = NEWS_TYPE_MY_ALFA_LIFE;
//            intent.putExtra("feedType", feedId);
//            startActivity(intent);
//        });
//        return root;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!checkIfInitialized()) return;
//        mPresenter.takeView(this);
//        mCalendarEventAdapter.onHostViewResume(mPresenter);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mPresenter.dropView();
//        mCalendarEventAdapter.onHostViewDestroy();
//    }
//
//    @Override
//    public void setUserPic(String encodedImage, boolean isCached) {
//        Handler handler = new Handler();
//        new Thread(() -> {
//            byte[] imageBytes = Base64.decode(encodedImage, Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//            Bitmap curvedBitmap = mImageCropper.getRoundedCornerBitmap(bitmap, (int) (360 * DENSITY));
//            handler.post(() -> {
//                imageAvatar.setImageBitmap(curvedBitmap);
//                imageAvatar.setVisibility(View.VISIBLE);
//                if (!isCached) {
//                    imageAvatar.setAlpha(0f);
//                    int mShortAnimationDuration = 200;
//                    imageAvatar.animate()
//                            .alpha(1f)
//                            .setDuration(mShortAnimationDuration)
//                            .setListener(null);
//                }
//            });
//        }).start();
//    }
//
//    @Override
//    public void openProfileUi(String userId) {
//        Intent intent = new Intent(getContext(), ProfileActivity.class);
//        intent.putExtra(PROFILE_ID, userId);
//        startActivity(intent);
//    }
//
//    @Override
//    public void openNotificationUi() {
//        ActivityUtils.addFragmentToActivity(getFragmentManager(),
//                new NotificationFragment(), R.id.activity_main_content_frame,
//                FRAGMENT_TRANSITION_ADD, true);
//    }
//
//    @Override
//    public void openSearchUi() {
//        ActivityUtils.addFragmentToActivity(getFragmentManager(),
//                new SearchFragment(), R.id.activity_main_content_frame,
//                FRAGMENT_TRANSITION_ADD, true);
//    }
//
//    @Override
//    public void showTopNews() {
//        mRecycler.setAdapter(mAdapter);
//        LinearLayoutManager mLm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        mRecycler.setLayoutManager(mLm);
//        mRecycler.addItemDecoration(new CirclePagerDecoration());
//        PagerSnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(mRecycler);
//    }
//
//    @Override
//    public void showCalendarEventListCards() {
//        mCalendarEventAdapter.setFragmentView(this);
//        mRecycler2.setAdapter(mCalendarEventAdapter);
//        LinearLayoutManager mLm2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        mRecycler2.setLayoutManager(mLm2);
//    }
//
//    @Override
//    public void openPost(String postUrl) {
//        Intent intent = new Intent(getContext(), PostActivity.class);
//        intent.putExtra(POST_URL, postUrl);
//        startActivity(intent);
//    }
//
//    @Override
//    public void hideTempBackground() {
//        flTempBackground.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void setSantaIcon() {
//        mImageNotification.setImageDrawable(getResources().getDrawable(R.drawable.icn_santa_hat_white_24x24));
//    }
//
//    /**
//     * For some unknown reason the method calls onPause twice,
//     * which prevents the normal save of the state in the presenter.
//     * Which in turn causes extra phones to be hidden if the users
//     * chooses to onCallClicked while they are shown.
//     */
//    @SuppressLint("MissingPermission")
//    @Override
//    public void makeCall(String phone) {
//        Intent intent = new Intent(Intent.ACTION_CALL);
//        intent.setData(Uri.parse("tel:" + phone));
//        startActivity(intent);
//    }
//
//    @Override
//    public void showWarningOnExtraCharge() {
//        WarningOnExtraChargeDialogFragment dialogFragment = WarningOnExtraChargeDialogFragment.newInstance();
//        FragmentManager fragmentManager = getFragmentManager();
//        dialogFragment.show(fragmentManager, "");
//    }
//
//    @Override
//    public boolean checkCallPermission() {
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void requestCallPermission(int phoneType) {
//        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, phoneType);
//    }
//
//    public void saveCallParameter(boolean accept) {
//        mPresenter.saveCallParameter(accept);
//    }
//
//    @Override
//    public void callHH() {
//        mPresenter.onCallHumanHelpClicked();
//    }
//
//    @Override
//    public void callHD() {
//        mPresenter.onCallHumanDeskClicked();
//    }
//}
