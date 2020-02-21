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

public class IntroFragment extends BaseFragment implements SurveyContract.IntroFragment {

    @BindView(R.id.intro_fragment_ll_intro_text)
    LinearLayout llIntrotext;
    @BindView(R.id.intro_fragment_fl_back)
    FrameLayout flBack;
    @BindView(R.id.intro_fragment_fl_start)
    FrameLayout flStart;
    @BindView(R.id.intro_fragment_image_background)
    ImageView imageBackground;
    @BindView(R.id.intro_fragment_tv_date)
    TextView tvDate;
    @BindView(R.id.intro_fragment_tv_title)
    TextView tvTitle;
    @BindView(R.id.intro_fragment_tv_description)
    TextView tvDescription;
    @BindView(R.id.intro_fragment_scrollview)
    ScrollView scrollView;

    @Inject
    SurveyContract.Presenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.intro_fragment, container, false);
        ButterKnife.bind(this, root);
        flBack.setOnClickListener(v -> mPresenter.onBackClicked());
        flStart.setOnClickListener(v -> mPresenter.onStartButtonClicked());
        return root;
    }

    @Override
    public void showIntroText() {
        llIntrotext.setVisibility(View.VISIBLE);
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
        scrollView.setVisibility(View.VISIBLE);
        tvDescription.setText(description);
    }

    @Override
    public void hideDescription() {
        scrollView.setVisibility(View.GONE);
    }

    @Override
    public void showImage(Bitmap encodedImage, boolean isCached) {

        imageBackground.setImageBitmap(encodedImage);
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
            mPresenter.takeIntroFragmentView(this);
        }
    }
}
