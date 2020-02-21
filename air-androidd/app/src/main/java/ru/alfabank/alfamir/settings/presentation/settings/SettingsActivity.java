package ru.alfabank.alfamir.settings.presentation.settings;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.widget.LinearLayout;
import android.widget.Switch;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;

public class SettingsActivity extends BaseActivity implements SettingsContract.View {

    @BindView(R.id.activity_notifications_settings_ll_back) LinearLayout llBack;
    @BindView(R.id.activity_notifications_settings_ll_info_favorites) LinearLayout llInfoFavorites;
    @BindView(R.id.activity_notifications_settings_ll_info_subscriptions) LinearLayout llInfoSubscriptions;

    @BindView(R.id.activity_notifications_settings_switch_main) Switch switchMain;
    @BindView(R.id.activity_notifications_settings_switch_birthday) Switch switchBirthday;
    @BindView(R.id.activity_notifications_settings_switch_vacation) Switch switchVacation;
    @BindView(R.id.activity_notifications_settings_switch_posts) Switch switchPosts;
    @BindView(R.id.activity_notifications_settings_switch_comments) Switch switchComments;

    @Inject
    SettingsContract.Presenter presenter;

    float active = 1f;
    float inactive = 0.5f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_settings_activity);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);

        llBack.setOnClickListener(view -> onBackPressed());
        llInfoFavorites.setOnClickListener(view -> presenter.onInfoFavoriteClicked());
        llInfoSubscriptions.setOnClickListener(view -> presenter.onInfoSubscriptionsClicked());

        switchMain.setOnClickListener(view -> presenter.onMainSwitched(switchMain.isChecked()));
        switchBirthday.setOnClickListener(view -> presenter.onBirthdaySwitched(switchBirthday.isChecked()));
        switchVacation.setOnClickListener(view -> presenter.onVacationSwitched(switchVacation.isChecked()));
        switchPosts.setOnClickListener(view -> presenter.onPostsSwitched(switchPosts.isChecked()));
        switchComments.setOnClickListener(view -> presenter.onCommentsSwitched(switchComments.isChecked()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.dropView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!checkIfInitialized()) return;
        presenter.takeView(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    public void setSwitchStateInactive(boolean isActive) {
        float state;
        if(isActive) state = active;
        else state = inactive;

        switchBirthday.setAlpha(state);
        switchVacation.setAlpha(state);
        switchPosts.setAlpha(state);
        switchComments.setAlpha(state);
    }

    @Override
    public void setSwitchStateAll(boolean isChecked) {
        setSwitchStateMain(isChecked);
        setSwitchStateBirthday(isChecked);
        setSwitchStateVacation(isChecked);
        setSwitchStateComments(isChecked);
        setSwitchStatePosts(isChecked);
    }

    @Override
    public void setSwitchStateMain(boolean isChecked) {
        switchMain.setChecked(isChecked);
    }

    @Override
    public void setSwitchStateBirthday(boolean isChecked) {
        switchBirthday.setChecked(isChecked);
    }

    @Override
    public void setSwitchStateVacation(boolean isChecked) {
        switchVacation.setChecked(isChecked);
    }

    @Override
    public void setSwitchStateComments(boolean isChecked) {
        switchComments.setChecked(isChecked);
    }

    @Override
    public void setSwitchStatePosts(boolean isChecked) {
        switchPosts.setChecked(isChecked);
    }

    @Override
    public void showInfoFavoritesDialog() {
        InfoFavoriteDialogFragment dialogFragment = InfoFavoriteDialogFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
//        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragment);
        dialogFragment.show(fragmentManager, "");
    }

    @Override
    public void showInfoSubscriptionsDialog() {
        InfoSubscriptionsDialogFragment dialogFragment = InfoSubscriptionsDialogFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "");
    }

}
