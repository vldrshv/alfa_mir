package ru.alfabank.alfamir.survey.presentation.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Strings;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.survey.presentation.contract.SurveyContract;

import static ru.alfabank.alfamir.Constants.SHORT_ANIMATION_DURATION;
import static ru.alfabank.alfamir.Constants.Survey.TEXT_AREA;

public class TextAreaQuestionFragment extends BaseFragment implements SurveyContract.TextAreaQuestionFragment {


    @BindView(R.id.text_area_question_fragment_ll_question_0)
    LinearLayout llQuestion;

    @BindView(R.id.text_area_question_fragment_fl_text_input_0)
    FrameLayout flTextInput;

    @BindView(R.id.text_area_question_fragment_et_text_input_0)
    EditText etTextInput;


    @BindView(R.id.text_area_question_fragment_tv_title)
    TextView tvTitle;

    @BindView(R.id.text_area_question_fragment_image_survey_image)
    ImageView imageSurveyImage;

    @Inject
    SurveyContract.Presenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.text_area_question_fragment, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkIfInitialized()) return;
        Bundle bundle = getArguments();
        int id = 0; //bundle.getInt(QUESTION_NUMBER);

        // if the fragment was recreated and not created by the activity,
        // get the link to the presenter from the activity; kinda hacky, but have no time :<

        // it might be resumed, therefor have no presenter
        if (mPresenter == null) {
            getActivity().finish();
        } else {
            mPresenter.takeTextAreaFragment(this, id);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void showImage(Bitmap encodedImage, boolean isAnimated) {

        imageSurveyImage.setImageBitmap(encodedImage);

        if (isAnimated) return;
        imageSurveyImage.setAlpha(0f);
        imageSurveyImage.animate()
                .alpha(1f)
                .setDuration(SHORT_ANIMATION_DURATION)
                .setListener(null);
    }

    @Override
    public void setImageHeight(int height) {
        imageSurveyImage.requestLayout();
        imageSurveyImage.getLayoutParams().height = height;
    }

    @Override
    public void setUpETListener() {
        etTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userInput = etTextInput.getText().toString();
                mPresenter.onTextChanged(userInput, TEXT_AREA);
            }
        });
        etTextInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etTextInput.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etTextInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                etTextInput.clearFocus();
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);
            }
            return false;
        });
        etTextInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (Strings.isNullOrEmpty(etTextInput.getText().toString())) {
                mPresenter.onEmptyEditTextView(TEXT_AREA);
            }
        });

    }

    @Override
    public String getTextInput() {
        String userInput = etTextInput.getText().toString();
        return userInput;
    }

}
