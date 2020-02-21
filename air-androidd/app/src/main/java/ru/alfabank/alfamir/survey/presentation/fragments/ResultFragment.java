package ru.alfabank.alfamir.survey.presentation.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.survey.presentation.contract.SurveyContract;

import javax.inject.Inject;

import static ru.alfabank.alfamir.Constants.SHORT_ANIMATION_DURATION;

/**
 * Created by U_M0WY5 on 27.04.2018.
 */

public class ResultFragment extends BaseFragment implements SurveyContract.ResultFragment {

    @BindView(R.id.result_fragment_image_background)
    ImageView imageBackground;
    @BindView(R.id.result_fragment_tv_comment)
    TextView tvComment;
    @BindView(R.id.result_fragment_tv_result)
    TextView tvResult;
    @BindView(R.id.result_fragment_fl_end)
    FrameLayout flEnd;

    @Inject
    SurveyContract.Presenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.quiz_result_fragment, container, false);
        ButterKnife.bind(this, root);
        flEnd.setOnClickListener(view -> mPresenter.onDoneClicked());
        return root;
    }

    @Override
    public void showComment(String text) {
        tvComment.setText(text);
    }

    @Override
    public void showResult(String text) {
        tvResult.setVisibility(View.VISIBLE);
        tvResult.setText(text);
    }

    @Override
    public void hideResult() {
        tvResult.setVisibility(View.GONE);
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
            mPresenter.takeResultFragmentView(this);
        }
    }
}
