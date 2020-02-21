package ru.alfabank.alfamir.survey.presentation.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.survey.presentation.contract.SurveyContract;

import javax.inject.Inject;

import static ru.alfabank.alfamir.Constants.SHORT_ANIMATION_DURATION;

public class ContinueFragment extends BaseFragment implements SurveyContract.ContinueFragment {

    @BindView(R.id.continue_fragment_fl_back)
    FrameLayout flBack;
    @BindView(R.id.continue_fragment_ll_start)
    LinearLayout llStart;
    @BindView(R.id.continue_fragment_image_background)
    ImageView imageBackground;
    @BindView(R.id.continue_fragment_tv_date)
    TextView tvDate;
    @BindView(R.id.continue_fragment_tv_title)
    TextView tvTitle;
    @BindView(R.id.continue_fragment_tv_progress)
    TextView tvProgressState;
    @BindView(R.id.continue_fragment_tv_description)
    TextView tvDescription;
    @BindView(R.id.continue_fragment_progress_bar)
    ProgressBar progressBar;

    @Inject
    SurveyContract.Presenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.quiz_continue_fragment, container, false);
        ButterKnife.bind(this, root);
        flBack.setOnClickListener(v -> mPresenter.onBackClicked());
        llStart.setOnClickListener(v -> mPresenter.onContinueButtonClicked());
        return root;
    }

    @Override
    public void showDate(String date) {
        tvDate.setText(date);
    }

    @Override
    public void showTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void showDescription(String description) {
        tvDescription.setText(description);
    }

    @Override
    public void setBarPercentage(int progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void showCompletionStatus(String status) {
        tvProgressState.setText(status);
    }

    @Override
    public void hideDescription() {
        tvDescription.setVisibility(View.GONE);
    }

    @Override
    public void showImage(Bitmap encodedImage, boolean isCached) {

        imageBackground.setImageBitmap(encodedImage);
        imageBackground.setVisibility(View.VISIBLE);

        if (isCached) {
            // should always have animation :<
        }

        imageBackground.setAlpha(0f);
        imageBackground.animate()
                .alpha(1f)
                .setDuration(SHORT_ANIMATION_DURATION)
                .setListener(null);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkIfInitialized()) return;
        // it might be resumed, therefor have no presenter
        if (mPresenter == null) {
            getActivity().finish();
        } else {
            mPresenter.takeContinueFragment(this);
        }
    }

}
