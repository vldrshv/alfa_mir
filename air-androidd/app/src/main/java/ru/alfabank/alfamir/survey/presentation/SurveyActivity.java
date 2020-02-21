package ru.alfabank.alfamir.survey.presentation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.survey.presentation.contract.SurveyContract;
import ru.alfabank.alfamir.survey.presentation.fragments.CheckboxQuestionFragment;
import ru.alfabank.alfamir.survey.presentation.fragments.ContinueFragment;
import ru.alfabank.alfamir.survey.presentation.fragments.IntroFragment;
import ru.alfabank.alfamir.survey.presentation.fragments.RadioButtonQuestionFragment;
import ru.alfabank.alfamir.survey.presentation.fragments.ResultFragment;
import ru.alfabank.alfamir.survey.presentation.fragments.TextAreaQuestionFragment;
import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils;

import static ru.alfabank.alfamir.Constants.FRAGMENT_TYPE_QUIZ_CHECKBOX;
import static ru.alfabank.alfamir.Constants.FRAGMENT_TYPE_QUIZ_CONTINUE;
import static ru.alfabank.alfamir.Constants.FRAGMENT_TYPE_QUIZ_INTRO;
import static ru.alfabank.alfamir.Constants.FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION;
import static ru.alfabank.alfamir.Constants.FRAGMENT_TYPE_QUIZ_RESULT;
import static ru.alfabank.alfamir.Constants.FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION;


public class SurveyActivity extends BaseActivity implements SurveyContract.ActivityView,
        WarningOnExitDialogFragment.WarningDialogListener {


    @BindView (R.id.survey_activity_fl_close) FrameLayout flClose;
    @BindView (R.id.survey_activity_ll_button_next) LinearLayout llNext;
    @BindView (R.id.survey_activity_ll_bottom_navigation) LinearLayout llBottomNavigation;
    @BindView (R.id.survey_activity_tv_counter) TextView tvCounter;
    @BindView (R.id.survey_activity_tv_button_next) TextView tvNext;
    @BindView (R.id.toolbar_tx_title) TextView tvTitle;
    @BindView (R.id.appbar) AppBarLayout appBar;

    @Inject
    SurveyContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.survey_activity);
        ButterKnife.bind(this);

        flClose.setOnClickListener(view -> presenter.onCloseClicked() );
    }

    @Override
    public void attachFragment(int fragmentType, int questionNumber, int animationType){

        switch (fragmentType){
            case FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION: {
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), new RadioButtonQuestionFragment(),
                        R.id.contentFrame, animationType, false);
                break;
            }
            case FRAGMENT_TYPE_QUIZ_CHECKBOX: {
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), new CheckboxQuestionFragment(),
                        R.id.contentFrame, animationType, false);
                break;
            }
            case FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION: {
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), new TextAreaQuestionFragment(),
                        R.id.contentFrame, animationType, false);
                break;
            }
            case FRAGMENT_TYPE_QUIZ_RESULT: {
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), new ResultFragment(),
                        R.id.contentFrame, animationType, false);
                break;
            }
            case FRAGMENT_TYPE_QUIZ_INTRO: {
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), new IntroFragment(),
                        R.id.contentFrame, animationType, false);
                break;
            }
            case FRAGMENT_TYPE_QUIZ_CONTINUE: {
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), new ContinueFragment(),
                        R.id.contentFrame, animationType, false);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!checkIfInitialized()) return;
        presenter.takeView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.dropView();
    }

    @Override
    public void enableNextButton() {
        llNext.setBackgroundResource(R.drawable.background_curved_corners_blue_filled);
        llNext.setOnClickListener(view -> presenter.onNextButtonClicked());
    }

    @Override
    public void disableNextButton() {
        llNext.setBackgroundResource(R.drawable.background_curved_corners_grey_filled);
        llNext.setOnClickListener(null);
    }

    @Override
    public void setDisabledNextButtonWarning(int fragmentType) {
        llNext.setOnClickListener(view -> presenter.onDisabledNextButtonClicked(fragmentType));
    }

    @Override
    public void hideBottomBar() {
        int myVersion = Integer.parseInt(android.os.Build.VERSION.RELEASE.substring(0, 1));
        if (myVersion < 5) {
            llBottomNavigation.setVisibility(View.GONE);
            return;
        }
        int mShortAnimationDuration = 400;
        llBottomNavigation.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        llBottomNavigation.setVisibility(View.GONE);
                    }
                });
//        llBottomNavigation.setVisibility(View.GONE);

    }

    @Override
    public void hideAppBar() {
        int myVersion = Integer.parseInt(android.os.Build.VERSION.RELEASE.substring(0, 1));
        if (myVersion < 5) {
            llBottomNavigation.setVisibility(View.GONE);
            return;
        }
        int mShortAnimationDuration = 400;
        appBar.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        appBar.setVisibility(View.GONE);
                    }
                });
//        appBar.setVisibility(View.GONE);
    }

    @Override
    public void showBottomBar() {
        int myVersion = Integer.parseInt(android.os.Build.VERSION.RELEASE.substring(0, 1));
        if (myVersion < 5) {
            llBottomNavigation.setVisibility(View.VISIBLE);
            return;
        }
        llBottomNavigation.setAlpha(0);
        llBottomNavigation.setVisibility(View.VISIBLE);
        int animationDuration = 400;
        llBottomNavigation.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);
    }

    @Override
    public void showAppBar() {
        int myVersion = Integer.parseInt(android.os.Build.VERSION.RELEASE.substring(0, 1));
        if (myVersion < 5) {
            appBar.setVisibility(View.VISIBLE);
            return;
        }
        appBar.setAlpha(0);
        appBar.setVisibility(View.VISIBLE);
        int animationDuration = 400;
        appBar.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);
    }



    @Override
    public void setCounterText(String text) {
        tvCounter.setText(text);
    }

    @Override
    public void setNextButtonText(String text) {
        tvNext.setText(text);
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void showWarningOnExitUi() {
        WarningOnExitDialogFragment dialogFragment = WarningOnExitDialogFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "");
    }

    @Override
    public void showSnackBar(String text) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.survey_activity_rl_main_parent),
                text, Snackbar.LENGTH_SHORT);
//        snackbar.setAction("Повторить", view -> presenter.onNextButtonClicked());
        snackbar.show();
    }

    @Override
    public void onWarningAccepted() {
        presenter.onExitWarningAccepted();
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }
}
