package ru.alfabank.alfamir.alfa_tv.presentation.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.password.PasswordDataSource;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.show.ShowRepository;
import ru.alfabank.alfamir.alfa_tv.domain.usecase.GetShow;
import ru.alfabank.alfamir.alfa_tv.domain.usecase.GetShowCurrentState;
import ru.alfabank.alfamir.alfa_tv.presentation.show.contract.ShowContract;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.image.data.source.repository.ImageRepository;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;
import ru.alfabank.alfamir.ui.custom_views.PasswordEditText;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter;
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract;
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper;
import ru.alfabank.alfamir.utility.video_player.EventLogger;

public class ShowActivity extends BaseActivity implements PlaybackControlView.VisibilityListener, Player.EventListener, PasswordEditText.EditTextListener,
        LoggerContract.Client.TvShow, ShowContract.View {
    @BindView(R.id.show_activity_rl_main_body)
    RelativeLayout rlMain;
    @BindView(R.id.show_activity_exoplayer)
    PlayerView exoPlayer;

    @Nullable
    @BindView(R.id.show_activity_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Nullable
    @BindView(R.id.show_activity_ll_back)
    LinearLayout llBack;
    @Nullable
    @BindView(R.id.show_activity_ll_show_later)
    LinearLayout llShowLater;
    @Nullable
    @BindView(R.id.show_activity_ll_password_screen)
    LinearLayout llPassword;
    @Nullable
    @BindView(R.id.show_activity_image_lock)
    ImageView imageLock;
    @Nullable
    @BindView(R.id.show_activity_et_password)
    PasswordEditText etPassword;
    @Nullable
    @BindView(R.id.show_activity_tv_bottom_prompt)
    TextView tvBottomPrompt;
    @Nullable
    @BindView(R.id.show_activity_tv_middle_prompt)
    TextView tvMiddlePrompt;
    @Nullable
    @BindView(R.id.show_activity_rl_show_time_body)
    RelativeLayout rlShowTimeBody;
    @Nullable
    @BindView(R.id.show_activity_image_calendar)
    ImageView imageCalendar;
    @Nullable
    @BindView(R.id.show_activity_tv_date)
    TextView tvDate;
    @Nullable
    @BindView(R.id.show_activity_image_clock)
    ImageView imageClock;
    @Nullable
    @BindView(R.id.show_activity_tv_time)
    TextView tvTime;
    @Nullable
    @BindView(R.id.show_activity_tv_time_estimate)
    TextView tvTimeEstimate;
    @Nullable
    @BindView(R.id.show_activity_ll_host)
    LinearLayout llHost;
    @Nullable
    @BindView(R.id.show_activity_image_host_pic)
    ImageView imageHostPic;
    @Nullable
    @BindView(R.id.show_activity_tv_host_initials)
    TextView tvHostInitials;
    @Nullable
    @BindView(R.id.show_activity_tv_host_name)
    TextView tvHostName;
    @Nullable
    @BindView(R.id.show_activity_tv_show_title)
    TextView tvShowTitle;
    @Nullable
    @BindView(R.id.show_activity_tv_show_description)
    TextView tvShowDescription;
    @Nullable
    @BindView(R.id.show_activity_tv_show_room)
    TextView tvShowRoom;
    @Nullable
    @BindView(R.id.show_activity_rl_show_title_and_description)
    RelativeLayout rlTitleAndDescription;
    @Nullable
    @BindView(R.id.show_activity_fl_more)
    FrameLayout flMore;
    @Nullable
    @BindView(R.id.show_activity_image_suggestions)
    ImageView imageSuggestion;
    @Nullable
    @BindView(R.id.show_activity_ll_show_info)
    LinearLayout llShowInfo;
    @Nullable
    @BindView(R.id.show_activity_fl_password)
    FrameLayout flPassword;

    private Uri vidUri;
    private Handler mainHandler;
    private EventLogger eventLogger;
    private LinearLayout debugRootView;
    private TextView debugTextView;
    private Button retryButton;

    private FrameLayout llFullScreen;
    private FrameLayout llMute;
    private ImageView imageMute;

    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private DebugTextViewHelper debugViewHelper;
    private boolean inErrorState;
    private TrackGroupArray lastSeenTrackGroupArray;

    private boolean shouldAutoPlay;
    private int resumeWindow;
    private long resumePosition;

    private Object imaAdsLoader; // com.google.android.exoplayer2.ext.ima.ImaAdsLoader
    private Uri loadedAdTagUri;
    private ViewGroup adOverlayViewGroup;

    public static final String DRM_SCHEME_UUID_EXTRA = "drm_scheme_uuid";
    public static final String DRM_LICENSE_URL = "drm_license_url";
    public static final String DRM_KEY_REQUEST_PROPERTIES = "drm_key_request_properties";
    public static final String PREFER_EXTENSION_DECODERS = "prefer_extension_decoders";

    public static final String ACTION_VIEW = "com.google.android.exoplayer.demo.action.VIEW";
    public static final String EXTENSION_EXTRA = "extension";

    public static final String ACTION_VIEW_LIST = "com.google.android.exoplayer.demo.action.VIEW_LIST";
    public static final String URI_LIST_EXTRA = "uri_list";
    public static final String EXTENSION_LIST_EXTRA = "extension_list";
    public static final String AD_TAG_URI_EXTRA = "ad_tag_uri";

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private boolean mIsMuted;
    private int mExoPlayerHeight;

    @Inject
    LoggerContract.Provider logger;
    @Inject
    GetShow mGetShow;
    @Inject
    ShowRepository mShowRepository;
    @Inject
    DateFormatter mDateConverter;
    @Inject
    PasswordDataSource mPasswordDataSource;
    @Inject
    GetShowCurrentState mGetShowCurrentState;
    @Inject
    ImageRepository mImageRepository;
    @Inject
    ShowContract.Presenter mPresenter;

    private int currentPanelColor = Color.argb(255, 52, 123, 246);
    private int currentPanelElementColor = Color.argb(255, 255, 255, 255);

    private int notCurrentPanelColor = Color.argb(255, 239, 239, 244);
    private int notCurrentPanelElementTextColor = Color.argb(255, 142, 147, 147);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mIsMuted = savedInstanceState.getBoolean("isMuted");
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            createPortraitView();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            createLandscapeView();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        super.onResume();
        if (!checkIfInitialized()) return;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            exoPlayer.getLayoutParams().height = mExoPlayerHeight;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mPresenter.takeView(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPresenter.setOrientation(true);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mPresenter.setOrientation(false);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

    @Override
    public void showProfileUi(String profileId) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(Constants.PROFILE_ID, profileId);
        startActivity(intent);
    }

    @Override
    public void setPortraitOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void setLandscapeOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void createLandscapeView() {
        setContentView(R.layout.show_activity);
        ButterKnife.bind(this);

        shouldAutoPlay = true;
        clearResumePosition();
        mediaDataSourceFactory = buildHttpDataSourceFactory(true);
        mainHandler = new Handler();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }

        llMute = findViewById(R.id.exo_mute);
        imageMute = findViewById(R.id.exo_mute_image);
        llMute.setOnClickListener(view -> {
            float currentVolume = player.getVolume();
            if (currentVolume != 0.0) {
                player.setVolume(0f);
                imageMute.setImageDrawable(getResources().getDrawable(R.drawable.ic_playback_mute_off));
                mIsMuted = true;
            } else {
                player.setVolume(1f);
                imageMute.setImageDrawable(getResources().getDrawable(R.drawable.ic_playback_mute));
                mIsMuted = false;
            }
        });
        llFullScreen = findViewById(R.id.exo_fullscreen);
        llFullScreen.setOnClickListener(view -> {
            mPresenter.onPortraitOrientationRequest();
        });
    }

    private void createPortraitView() {
        setContentView(R.layout.show_activity);
        ButterKnife.bind(this);

        exoPlayer.setControllerVisibilityListener(this);

        mExoPlayerHeight = (int) (((double) Constants.Initialization.getSCREEN_WIDTH_PHYSICAL() / 16) * 9);
        shouldAutoPlay = true;
        clearResumePosition();
        mediaDataSourceFactory = buildHttpDataSourceFactory(true);
        mainHandler = new Handler();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }

        debugRootView = findViewById(R.id.controls_root);
        debugTextView = findViewById(R.id.debug_text_view);
        retryButton = findViewById(R.id.retry_button);
        llFullScreen = findViewById(R.id.exo_fullscreen);
        llMute = findViewById(R.id.exo_mute);
        imageMute = findViewById(R.id.exo_mute_image);

        llFullScreen.setOnClickListener(view -> {
            mPresenter.onLandscapeOrientationRequest();

        });
        llBack.setOnClickListener((View v) -> {
            onBackPressed();
        });

        exoPlayer.setControllerVisibilityListener(this);
        exoPlayer.requestFocus();
        llMute.setOnClickListener(view -> {
            float currentVolume = player.getVolume();
            if (currentVolume != 0.0) {
                player.setVolume(0f);
                imageMute.setImageDrawable(getResources().getDrawable(R.drawable.ic_playback_mute_off));
                mIsMuted = true;
            } else {
                player.setVolume(1f);
                imageMute.setImageDrawable(getResources().getDrawable(R.drawable.ic_playback_mute));
                mIsMuted = false;
            }
        });

        llHost.setOnClickListener(view -> mPresenter.onHostClicked());
        etPassword.setHideKeyboardListener(mPresenter);
        etPassword.addTextChangedListener(mPresenter);

        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mPresenter.onDoneKeyboardButtonPressed();
                return true;
            }
            return false;
        });
        etPassword.setOnFocusChangeListener((view, hasFocus) -> mPresenter.onEditTextFocusChanged(hasFocus));
        mSwipeRefreshLayout.setEnabled(false);
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    @Override
    public void setHostName(String name) {
        tvHostName.setText(name);
    }

    @Override
    public void setHostPicture(Bitmap bitmap, boolean isAnimated) {
        RoundedBitmapDrawable roundedImage = PictureClipper.makeItRound(bitmap);
        imageHostPic.setImageDrawable(roundedImage);
        imageHostPic.setVisibility(View.VISIBLE);
        if (isAnimated) {
            imageHostPic.setAlpha(0f);
            int mShortAnimationDuration = 200;
            imageHostPic.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }
    }

    @Override
    public void setHostPicturePlaceHolder() {
        Handler handler = new Handler();
        new Thread(() -> {
            handler.post(() -> {
                imageHostPic.setImageResource(R.drawable.background_profile_empty);
                imageHostPic.setVisibility(View.VISIBLE);
                imageHostPic.setAlpha(0f);
                int mShortAnimationDuration = 200;
                imageHostPic.animate()
                        .alpha(1f)
                        .setDuration(mShortAnimationDuration)
                        .setListener(null);
            });
        }).start();

    }

    @Override
    public void showShowDate(String date) {
        tvDate.setText(date);
    }

    @Override
    public void showShowTime(String time) {
        tvTime.setText(time);
    }

    @Override
    public void showShowTimeEstimate(String time) {
        tvTimeEstimate.setText(time);
    }

    @Override
    public void showShowTitle(String title, int maxLines) {
        tvShowTitle.setText(title);
    }

    @Override
    public void showShowDescription(String description, int maxLines) {
        tvShowDescription.setText(description);
    }

    @Override
    public void hideTitle() {
        tvShowTitle.setVisibility(View.GONE);
    }

    @Override
    public void hideDescription() {
        tvShowDescription.setVisibility(View.GONE);
    }

    @Override
    public void hideTitleAndDescription() {
        rlTitleAndDescription.setVisibility(View.GONE);
    }

    @Override
    public void showShowDescriptionMore() {

    }

    @Override
    public void hideShowDescriptionMore() {

    }

    @Override
    public void showShowRoom(String room) {
        tvShowRoom.setText(room);
    }

    @Override
    public void setTimePanelState(boolean isActive) {
        if (isActive) {
            rlShowTimeBody.setBackgroundColor(currentPanelColor);
            imageCalendar.setColorFilter(currentPanelElementColor);
            imageClock.setColorFilter(currentPanelElementColor);
            tvDate.setTextColor(currentPanelElementColor);
            tvTime.setTextColor(currentPanelElementColor);
            tvTimeEstimate.setTextColor(currentPanelElementColor);
        } else {
            rlShowTimeBody.setBackgroundColor(notCurrentPanelColor);
            imageCalendar.setColorFilter(notCurrentPanelElementTextColor);
            imageClock.setColorFilter(notCurrentPanelElementTextColor);
            tvDate.setTextColor(notCurrentPanelElementTextColor);
            tvTime.setTextColor(notCurrentPanelElementTextColor);
            tvTimeEstimate.setTextColor(notCurrentPanelElementTextColor);
        }
    }

    @Override
    public void clearPassword() {
        etPassword.setText("");
    }

    @Override
    public void setPasswordViewFocused() {
        flPassword.setBackground(getResources().getDrawable(R.drawable.background_enter_password_focused));
        imageLock.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock));
        tvBottomPrompt.setText("Узнайте код у организатора");
        tvBottomPrompt.setTextColor(Color.parseColor("#8e8e93"));
    }

    @Override
    public void setPasswordViewWrong() {
        flPassword.setBackground(getResources().getDrawable(R.drawable.background_enter_password_wrong));
        imageLock.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_wrong));
        tvBottomPrompt.setText("Попробуйте другой код");
        tvBottomPrompt.setTextColor(Color.parseColor("#e53935"));
    }

    @Override
    public void setPasswordViewNeutral() {
        flPassword.setBackground(getResources().getDrawable(R.drawable.background_enter_password));
    }

    @Override
    public void showEnterPasswordView() {
        llPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEnterPasswordView() {
        llPassword.setVisibility(View.GONE);
    }

    @Override
    public void showShowMiddlePrompt(String prompt) {
        llShowLater.setVisibility(View.VISIBLE);
        tvMiddlePrompt.setText(prompt);
    }

    @Override
    public void showShowInfoView() {
        llShowInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSHowInfoView() {
        llShowInfo.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingState() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingState() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
        }, 500);
    }

    @Override
    public void hideHostInitials() {
        tvHostInitials.setVisibility(View.GONE);
    }

    @Override
    public void showHostInitials(String initials) {
        tvHostInitials.setText(initials);
        tvHostInitials.setVisibility(View.VISIBLE);
    }

    @Override
    public void initializePlayer(String showUrl) {
        vidUri = Uri.parse(showUrl);
        Intent intent = getIntent();
        boolean needNewPlayer = player == null;
        if (needNewPlayer) {
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
            lastSeenTrackGroupArray = null;
            eventLogger = new EventLogger(trackSelector);
            UUID drmSchemeUuid = intent.hasExtra(DRM_SCHEME_UUID_EXTRA)
                    ? UUID.fromString(intent.getStringExtra(DRM_SCHEME_UUID_EXTRA)) : null;
            DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
            if (drmSchemeUuid != null) {
                String drmLicenseUrl = intent.getStringExtra(DRM_LICENSE_URL);
                String[] keyRequestPropertiesArray = intent.getStringArrayExtra(DRM_KEY_REQUEST_PROPERTIES);
                int errorStringId = R.string.error_drm_unknown;
                if (Util.SDK_INT < 18) {
                    errorStringId = R.string.error_drm_not_supported;
                } else {
                    try {
                        drmSessionManager = buildDrmSessionManagerV18(drmSchemeUuid, drmLicenseUrl,
                                keyRequestPropertiesArray);
                    } catch (UnsupportedDrmException e) {
                        errorStringId = e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                                ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown;
                    }
                }
                if (drmSessionManager == null) {
                    showToast(errorStringId);
                    return;
                }
            }
            boolean preferExtensionDecoders = intent.getBooleanExtra(PREFER_EXTENSION_DECODERS, false);
            @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
                    ((App) getApplication()).useExtensionRenderers() ? (preferExtensionDecoders ?
                            DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                            : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                            : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this,
                    drmSessionManager, extensionRendererMode);
//            player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
            player = ExoPlayerFactory.newSimpleInstance(this);
            player.addListener(this);
            player.addListener(eventLogger);
            player.addMetadataOutput(eventLogger);
            player.setAudioDebugListener(eventLogger);
            player.setVideoDebugListener(eventLogger);
            exoPlayer.setPlayer(player);
            player.setPlayWhenReady(shouldAutoPlay);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                debugViewHelper = new DebugTextViewHelper(player, debugTextView);
                debugViewHelper.start();
            }
            if (mIsMuted) {
                player.setVolume(0f);
                imageMute.setImageDrawable(getResources().getDrawable(R.drawable.ic_playback_mute_off));
            }

        }
        String action = "com.google.android.exoplayer.demo.action.VIEW";
        Uri[] uris;
        String[] extensions;
        if (ACTION_VIEW.equals(action)) {
            uris = new Uri[]{intent.getData()};
            extensions = new String[]{intent.getStringExtra(EXTENSION_EXTRA)};
        } else if (ACTION_VIEW_LIST.equals(action)) {
            String[] uriStrings = intent.getStringArrayExtra(URI_LIST_EXTRA);
            uris = new Uri[uriStrings.length];
            for (int i = 0; i < uriStrings.length; i++) {
                uris[i] = Uri.parse(uriStrings[i]);
            }
            extensions = intent.getStringArrayExtra(EXTENSION_LIST_EXTRA);
            if (extensions == null) {
                extensions = new String[uriStrings.length];
            }
        } else {
            showToast(getString(R.string.unexpected_intent_action, action));
            return;
        }
        MediaSource[] mediaSources = new MediaSource[uris.length];
        for (int i = 0; i < uris.length; i++) {
            mediaSources[i] = buildMediaSource(vidUri, extensions[i]);
        }
        MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0] : new ConcatenatingMediaSource(mediaSources);
        String adTagUriString = intent.getStringExtra(AD_TAG_URI_EXTRA);
        if (adTagUriString != null) {
            Uri adTagUri = Uri.parse(adTagUriString);
            if (!adTagUri.equals(loadedAdTagUri)) {
                releaseAdsLoader();
                loadedAdTagUri = adTagUri;
            }
            try {
                mediaSource = createAdsMediaSource(mediaSource, Uri.parse(adTagUriString));
            } catch (Exception e) {
                showToast(R.string.ima_not_loaded);
            }
        } else {
            releaseAdsLoader();
        }
        boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            player.seekTo(resumeWindow, resumePosition);
        }
        player.prepare(mediaSource, !haveResumePosition, false);
        inErrorState = false;
        updateButtonVisibilities();
    }

    private void updateButtonVisibilities() {
        if (debugRootView != null && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            debugRootView.removeAllViews();

            retryButton.setVisibility(inErrorState ? View.VISIBLE : View.GONE);
            debugRootView.addView(retryButton);
        }
        if (player == null) {
            return;
        }
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }
        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                Button button = new Button(this);
                int label;
                switch (player.getRendererType(i)) {
                    case C.TRACK_TYPE_AUDIO:
                        label = R.string.audio;
                        break;
                    case C.TRACK_TYPE_VIDEO:
                        label = R.string.video;
                        break;
                    case C.TRACK_TYPE_TEXT:
                        label = R.string.text;
                        break;
                    default:
                        continue;
                }
                button.setText(label);
                button.setTag(i);
//                button.setOnClickListener(this);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    debugRootView.addView(button, debugRootView.getChildCount() - 1);
                }
            }
        }
    }

    private void releaseAdsLoader() {
        if (imaAdsLoader != null) {
            try {
                Class<?> loaderClass = Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsLoader");
                Method releaseMethod = loaderClass.getMethod("release");
                releaseMethod.invoke(imaAdsLoader);
            } catch (Exception e) {
                // Should never happen.
                throw new IllegalStateException(e);
            }
            imaAdsLoader = null;
            loadedAdTagUri = null;
            exoPlayer.getOverlayFrameLayout().removeAllViews();
        }
    }

    private MediaSource createAdsMediaSource(MediaSource mediaSource, Uri adTagUri) throws Exception {
        // Load the extension source using reflection so the demo app doesn't have to depend on it.
        // The ads loader is reused for multiple playbacks, so that ad playback can resume.
        Class<?> loaderClass = Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsLoader");
        if (imaAdsLoader == null) {
            imaAdsLoader = loaderClass.getConstructor(Context.class, Uri.class)
                    .newInstance(this, adTagUri);
            adOverlayViewGroup = new FrameLayout(this);
            // The demo app has a non-null overlay frame layout.
            exoPlayer.getOverlayFrameLayout().addView(adOverlayViewGroup);
        }
        Class<?> sourceClass =
                Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsMediaSource");
        Constructor<?> constructor = sourceClass.getConstructor(MediaSource.class,
                DataSource.Factory.class, loaderClass, ViewGroup.class);
        return (MediaSource) constructor.newInstance(mediaSource, mediaDataSourceFactory, imaAdsLoader,
                adOverlayViewGroup);
    }

    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(UUID uuid,
                                                                              String licenseUrl, String[] keyRequestPropertiesArray) throws UnsupportedDrmException {
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
                buildHttpDataSourceFactory(false));
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }
        return new DefaultDrmSessionManager<>(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback,
                null, mainHandler, eventLogger);
    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri) : Util.inferContentType("." + overrideExtension);
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(false), new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(false), new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(), mainHandler, eventLogger);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((App) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return ((App) getApplication())
                .buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    @Override
    public void onVisibilityChange(int visibility) {

    }

    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {

            if (debugViewHelper != null) {
                debugViewHelper.stop();
            }

            debugViewHelper = null;
            shouldAutoPlay = player.getPlayWhenReady();
            updateResumePosition();
            player.release();
            player = null;
            trackSelector = null;
            eventLogger = null;
        }
    }

    private void updateResumePosition() {
        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = Math.max(0, player.getContentPosition());
    }

    // Player.EventListener implementation

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // Do nothing.
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_ENDED) {
            showControls();
        }
        updateButtonVisibilities();
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
        // Do nothing.
    }

    public void onPositionDiscontinuity() {
        if (inErrorState) {
            updateResumePosition();
        }
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        // Do nothing.
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorString != null) {
            showToast(errorString);
        }
        inErrorState = true;
        if (isBehindLiveWindow(e)) {
            clearResumePosition();
            initializePlayer(vidUri.toString());
        } else {
            updateResumePosition();
            updateButtonVisibilities();
            showControls();
        }
    }

    @Override
    @SuppressWarnings("ReferenceEquality")
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        updateButtonVisibilities();
        if (trackGroups != lastSeenTrackGroupArray) {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                        == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                    showToast(R.string.error_unsupported_video);
                }
                if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                        == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                    showToast(R.string.error_unsupported_audio);
                }
            }
            lastSeenTrackGroupArray = trackGroups;
        }
    }

    private void showControls() {
        if (debugRootView != null) {
            debugRootView.setVisibility(View.VISIBLE);
        }
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (exoPlayer != null) {
            exoPlayer.setVisibility(View.INVISIBLE);
        }
        super.onBackPressed();
    }

    @Override
    public void showSnackBar(String text) {
        Snackbar snackbar = Snackbar.make(rlMain, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
        etPassword.clearFocus();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isMuted", mIsMuted);
    }

    @Override
    public void logOpen(String id, String name) {
        logger.openTvShow(id, name);
    }

}
