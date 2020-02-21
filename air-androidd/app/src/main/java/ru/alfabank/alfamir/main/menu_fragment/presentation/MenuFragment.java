package ru.alfabank.alfamir.main.menu_fragment.presentation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.BuildConfig;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.ShowListFragment;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.feed.presentation.feed.FeedActivity;
import ru.alfabank.alfamir.main.menu_fragment.presentation.contract.MenuFragmentContract;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;
import ru.alfabank.alfamir.search.presentation.SearchFragment;
import ru.alfabank.alfamir.settings.presentation.settings.SettingsActivity;
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper;

import static ru.alfabank.alfamir.Constants.Post;

public class MenuFragment extends BaseFragment implements MenuFragmentContract.View {

    @BindView(R.id.menu_fragment_ll_profile)
    LinearLayout llProfile;
    @BindView(R.id.menu_fragment_ll_subscriptions)
    LinearLayout llSubscriptions;
    @BindView(R.id.menu_fragment_ll_settings)
    LinearLayout llSettings;
    @BindView(R.id.menu_fragment_ll_sport)
    LinearLayout llSport;
    @BindView(R.id.menu_fragment_ll_forum)
    LinearLayout llForum;
    @BindView(R.id.menu_fragment_ll_notice_board)
    LinearLayout llNoticeBoard;
    @BindView(R.id.menu_fragment_ll_alva_tv)
    LinearLayout llAlfaTv;
    @BindView(R.id.menu_fragment_ll_video)
    LinearLayout llVideo;
    @BindView(R.id.menu_fragment_ll_about)
    LinearLayout llAbout;
    @BindView(R.id.menu_fragment_ll_feedback)
    LinearLayout llFeedBack;
    @BindView(R.id.menu_fragment_image_profile)
    ImageView imageProfile;
    @BindView(R.id.menu_fragment_tv_name)
    TextView tvUserName;
    @BindView(R.id.menu_fragment_tv_title)
    TextView tvUserPosition;
    @BindView(R.id.menu_fragment_tv_version)
    TextView tvVersion;
    @BindView(R.id.menu_fragment_tv_version_comment)
    TextView tvVersionComment;
    @BindView(R.id.menu_toolbar_back)
    FrameLayout flBack;
    @BindView(R.id.menu_toolbar_search)
    FrameLayout flSearch;

    @Inject
    ImageCropper mImageCropper;
    @Inject
    MenuPresenter mMenuPresenter;

    private int tapCounter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.menu_fragment, container, false);
        ButterKnife.bind(this, root);
        setListeners();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkIfInitialized()) return;
        mMenuPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        mMenuPresenter.dropView();
        super.onDestroy();
    }

    private void setListeners() {
        flBack.setOnClickListener(v -> {
            List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragments) {
                if (fragment.isVisible()) {
                    fragment.getChildFragmentManager().popBackStack();
                    return;
                }
            }
        });
        flSearch.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.menu_container, new SearchFragment(), null);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        llProfile.setOnClickListener(v -> {
            mMenuPresenter.onProfileClicked();
        });
        llSport.setOnClickListener(v -> {
            String feedId = "http://alfa/Community/sport";
            String feedUrl = "http://alfa/Community/sport";
            String feedType = "community";
            String feedTitle = "Спортивные сообщества";
            boolean postCreationEnabled = feedId.equals("http://alfa/Info/Stories");
            startFeedActivity(feedId, feedUrl, feedType, feedTitle, postCreationEnabled);
        });
        llForum.setOnClickListener(v -> {
            String feedId = "http://alfa/Community/forum";
            String feedUrl = "http://alfa/Community/forum";
            String feedType = "community";
            String feedTitle = "Форум";
            boolean postCreationEnabled = feedId.equals("http://alfa/Info/Stories");
            startFeedActivity(feedId, feedUrl, feedType, feedTitle, postCreationEnabled);
        });
        llNoticeBoard.setOnClickListener(v -> {
            String feedId = "http://alfa/Community/ads";
            String feedUrl = "http://alfa/Community/ads";
            String feedType = "community";
            String feedTitle = "Доска объявлений";
            boolean postCreationEnabled = feedId.equals("http://alfa/Info/Stories");
            startFeedActivity(feedId, feedUrl, feedType, feedTitle, postCreationEnabled);
        });
        llVideo.setOnClickListener(v -> {
            String feedId = "http://it/tv";
            String feedUrl = "http://it/tv";
            String feedType = "it";
            String feedTitle = "Видео";
            boolean postCreationEnabled = feedId.equals("http://alfa/Info/Stories");
            startFeedActivity(feedId, feedUrl, feedType, feedTitle, postCreationEnabled);
        });
        llFeedBack.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                    "YDyachenko@alfabank.ru, AGorenkin@alfabank.ru", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Air feedback");
            startActivity(Intent.createChooser(emailIntent, "Отправить письмо"));
        });
        llSettings.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });
        llAlfaTv.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.menu_container, new ShowListFragment(), null);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        llAbout.setOnClickListener(v -> onVersionClicked());
    }

    private void startFeedActivity(String feedId, String feedUrl, String feedType, String feedTitle, boolean postCreationEnabled) {
        Intent intent = new Intent(getContext(), FeedActivity.class);
        intent.putExtra(Post.FEED_ID, feedId);
        intent.putExtra(Post.FEED_URL, feedUrl);
        intent.putExtra(Post.FEED_TYPE, feedType);
        intent.putExtra(Post.FEED_TITLE, feedTitle);
        intent.putExtra(Post.POST_CREATION_ENABLED, postCreationEnabled);
        startActivity(intent);
    }

    @Override
    public void setUserPic(Bitmap bitmap, boolean isCached) {

        Bitmap curvedBitmap = mImageCropper.getRoundedCornerBitmap(bitmap, (int) (12 * Constants.Initialization.getDENSITY()));
        imageProfile.setImageBitmap(curvedBitmap);
        imageProfile.setVisibility(View.VISIBLE);
        if (!isCached) {
            imageProfile.setAlpha(0f);
            int mShortAnimationDuration = 200;
            imageProfile.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }
    }

    @Override
    public void setUserName(String name) {
        tvUserName.setText(name);
    }

    @Override
    public void setUserPosition(String position) {
        tvUserPosition.setText(position);
    }

    @Override
    public void openProfileActivityUi(String userId) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(Constants.PROFILE_ID, userId);
        startActivity(intent);
    }

    @Override
    public void setAppVersion(String version) {
        tvVersion.setText(version);
    }

    @Override
    public void setAppVersionComment(String comment) {
        tvVersionComment.setText(comment);
    }

    @Override
    public void openSettingsActivityUi() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void onVersionClicked() {
        tapCounter++;
        if (tapCounter > 6) {
            String server = Constants.Companion.getBUILD_TYPE_PROD() ? "production" : "test";
            Snackbar snackbar = Snackbar.make(llAbout, String.format("App version: %s\nbuild: %s\nserver: %s", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, server), Snackbar.LENGTH_INDEFINITE);
            View snackbarView = snackbar.getView();
            TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setMaxLines(3);
            snackbar.setAction("Dismiss", v -> snackbar.dismiss());
            snackbar.show();
            tapCounter = 0;
        }
    }
}
