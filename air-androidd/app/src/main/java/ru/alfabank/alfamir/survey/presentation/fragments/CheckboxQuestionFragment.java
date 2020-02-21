package ru.alfabank.alfamir.survey.presentation.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.common.base.Strings;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.survey.presentation.contract.SurveyContract;

import javax.inject.Inject;

import static ru.alfabank.alfamir.Constants.SHORT_ANIMATION_DURATION;
import static ru.alfabank.alfamir.Constants.Survey.CHECKBOX_QUESTION;

public class CheckboxQuestionFragment extends BaseFragment implements SurveyContract.CheckboxQuestionFragment {

    @BindView(R.id.checkbox_fragment_ll_question_0)
    LinearLayout llQuestion0;
    @BindView(R.id.checkbox_fragment_ll_question_1)
    LinearLayout llQuestion1;
    @BindView(R.id.checkbox_fragment_ll_question_2)
    LinearLayout llQuestion2;
    @BindView(R.id.checkbox_fragment_ll_question_3)
    LinearLayout llQuestion3;
    @BindView(R.id.checkbox_fragment_ll_question_4)
    LinearLayout llQuestion4;
    @BindView(R.id.checkbox_fragment_ll_question_5)
    LinearLayout llQuestion5;
    @BindView(R.id.checkbox_fragment_ll_question_6)
    LinearLayout llQuestion6;
    @BindView(R.id.checkbox_fragment_ll_question_7)
    LinearLayout llQuestion7;
    @BindView(R.id.checkbox_fragment_ll_question_8)
    LinearLayout llQuestion8;
    @BindView(R.id.checkbox_fragment_ll_question_9)
    LinearLayout llQuestion9;
    @BindView(R.id.checkbox_fragment_ll_question_10)
    LinearLayout llQuestion10;
    @BindView(R.id.checkbox_fragment_ll_question_11)
    LinearLayout llQuestion11;
    @BindView(R.id.checkbox_fragment_ll_question_12)
    LinearLayout llQuestion12;
    @BindView(R.id.checkbox_fragment_ll_question_13)
    LinearLayout llQuestion13;
    @BindView(R.id.checkbox_fragment_ll_question_14)
    LinearLayout llQuestion14;
    @BindView(R.id.checkbox_fragment_ll_question_15)
    LinearLayout llQuestion15;
    @BindView(R.id.checkbox_fragment_ll_question_16)
    LinearLayout llQuestion16;
    @BindView(R.id.checkbox_fragment_ll_question_17)
    LinearLayout llQuestion17;
    @BindView(R.id.checkbox_fragment_ll_question_18)
    LinearLayout llQuestion18;
    @BindView(R.id.checkbox_fragment_ll_question_19)
    LinearLayout llQuestion19;
    @BindView(R.id.checkbox_fragment_ll_question_20)
    LinearLayout llQuestion20;
    @BindView(R.id.checkbox_fragment_ll_question_21)
    LinearLayout llQuestion21;
    @BindView(R.id.checkbox_fragment_ll_question_22)
    LinearLayout llQuestion22;
    @BindView(R.id.checkbox_fragment_ll_question_23)
    LinearLayout llQuestion23;
    @BindView(R.id.checkbox_fragment_ll_question_24)
    LinearLayout llQuestion24;
    @BindView(R.id.checkbox_fragment_ll_question_25)
    LinearLayout llQuestion25;
    @BindView(R.id.checkbox_fragment_ll_question_26)
    LinearLayout llQuestion26;
    @BindView(R.id.checkbox_fragment_ll_question_27)
    LinearLayout llQuestion27;
    @BindView(R.id.checkbox_fragment_ll_question_28)
    LinearLayout llQuestion28;
    @BindView(R.id.checkbox_fragment_ll_question_29)
    LinearLayout llQuestion29;
    @BindView(R.id.checkbox_fragment_ll_question_30)
    LinearLayout llQuestion30;
    @BindView(R.id.checkbox_fragment_ll_question_31)
    LinearLayout llQuestion31;
    @BindView(R.id.checkbox_fragment_ll_question_32)
    LinearLayout llQuestion32;
    @BindView(R.id.checkbox_fragment_ll_question_33)
    LinearLayout llQuestion33;

    @BindView(R.id.checkbox_fragment_image_selector_0)
    ImageView imageSelector0;
    @BindView(R.id.checkbox_fragment_image_selector_1)
    ImageView imageSelector1;
    @BindView(R.id.checkbox_fragment_image_selector_2)
    ImageView imageSelector2;
    @BindView(R.id.checkbox_fragment_image_selector_3)
    ImageView imageSelector3;
    @BindView(R.id.checkbox_fragment_image_selector_4)
    ImageView imageSelector4;
    @BindView(R.id.checkbox_fragment_image_selector_5)
    ImageView imageSelector5;
    @BindView(R.id.checkbox_fragment_image_selector_6)
    ImageView imageSelector6;
    @BindView(R.id.checkbox_fragment_image_selector_7)
    ImageView imageSelector7;
    @BindView(R.id.checkbox_fragment_image_selector_8)
    ImageView imageSelector8;
    @BindView(R.id.checkbox_fragment_image_selector_9)
    ImageView imageSelector9;
    @BindView(R.id.checkbox_fragment_image_selector_10)
    ImageView imageSelector10;
    @BindView(R.id.checkbox_fragment_image_selector_11)
    ImageView imageSelector11;
    @BindView(R.id.checkbox_fragment_image_selector_12)
    ImageView imageSelector12;
    @BindView(R.id.checkbox_fragment_image_selector_13)
    ImageView imageSelector13;
    @BindView(R.id.checkbox_fragment_image_selector_14)
    ImageView imageSelector14;
    @BindView(R.id.checkbox_fragment_image_selector_15)
    ImageView imageSelector15;
    @BindView(R.id.checkbox_fragment_image_selector_16)
    ImageView imageSelector16;
    @BindView(R.id.checkbox_fragment_image_selector_17)
    ImageView imageSelector17;
    @BindView(R.id.checkbox_fragment_image_selector_18)
    ImageView imageSelector18;
    @BindView(R.id.checkbox_fragment_image_selector_19)
    ImageView imageSelector19;
    @BindView(R.id.checkbox_fragment_image_selector_20)
    ImageView imageSelector20;
    @BindView(R.id.checkbox_fragment_image_selector_21)
    ImageView imageSelector21;
    @BindView(R.id.checkbox_fragment_image_selector_22)
    ImageView imageSelector22;
    @BindView(R.id.checkbox_fragment_image_selector_23)
    ImageView imageSelector23;
    @BindView(R.id.checkbox_fragment_image_selector_24)
    ImageView imageSelector24;
    @BindView(R.id.checkbox_fragment_image_selector_25)
    ImageView imageSelector25;
    @BindView(R.id.checkbox_fragment_image_selector_26)
    ImageView imageSelector26;
    @BindView(R.id.checkbox_fragment_image_selector_27)
    ImageView imageSelector27;
    @BindView(R.id.checkbox_fragment_image_selector_28)
    ImageView imageSelector28;
    @BindView(R.id.checkbox_fragment_image_selector_29)
    ImageView imageSelector29;
    @BindView(R.id.checkbox_fragment_image_selector_30)
    ImageView imageSelector30;
    @BindView(R.id.checkbox_fragment_image_selector_31)
    ImageView imageSelector31;
    @BindView(R.id.checkbox_fragment_image_selector_32)
    ImageView imageSelector32;
    @BindView(R.id.checkbox_fragment_image_selector_33)
    ImageView imageSelector33;

    @BindView(R.id.checkbox_fragment_tv_question_0)
    TextView tvQuestion0;
    @BindView(R.id.checkbox_fragment_tv_question_1)
    TextView tvQuestion1;
    @BindView(R.id.checkbox_fragment_tv_question_2)
    TextView tvQuestion2;
    @BindView(R.id.checkbox_fragment_tv_question_3)
    TextView tvQuestion3;
    @BindView(R.id.checkbox_fragment_tv_question_4)
    TextView tvQuestion4;
    @BindView(R.id.checkbox_fragment_tv_question_5)
    TextView tvQuestion5;
    @BindView(R.id.checkbox_fragment_tv_question_6)
    TextView tvQuestion6;
    @BindView(R.id.checkbox_fragment_tv_question_7)
    TextView tvQuestion7;
    @BindView(R.id.checkbox_fragment_tv_question_8)
    TextView tvQuestion8;
    @BindView(R.id.checkbox_fragment_tv_question_9)
    TextView tvQuestion9;
    @BindView(R.id.checkbox_fragment_tv_question_10)
    TextView tvQuestion10;
    @BindView(R.id.checkbox_fragment_tv_question_11)
    TextView tvQuestion11;
    @BindView(R.id.checkbox_fragment_tv_question_12)
    TextView tvQuestion12;
    @BindView(R.id.checkbox_fragment_tv_question_13)
    TextView tvQuestion13;
    @BindView(R.id.checkbox_fragment_tv_question_14)
    TextView tvQuestion14;
    @BindView(R.id.checkbox_fragment_tv_question_15)
    TextView tvQuestion15;
    @BindView(R.id.checkbox_fragment_tv_question_16)
    TextView tvQuestion16;
    @BindView(R.id.checkbox_fragment_tv_question_17)
    TextView tvQuestion17;
    @BindView(R.id.checkbox_fragment_tv_question_18)
    TextView tvQuestion18;
    @BindView(R.id.checkbox_fragment_tv_question_19)
    TextView tvQuestion19;
    @BindView(R.id.checkbox_fragment_tv_question_20)
    TextView tvQuestion20;
    @BindView(R.id.checkbox_fragment_tv_question_21)
    TextView tvQuestion21;
    @BindView(R.id.checkbox_fragment_tv_question_22)
    TextView tvQuestion22;
    @BindView(R.id.checkbox_fragment_tv_question_23)
    TextView tvQuestion23;
    @BindView(R.id.checkbox_fragment_tv_question_24)
    TextView tvQuestion24;
    @BindView(R.id.checkbox_fragment_tv_question_25)
    TextView tvQuestion25;
    @BindView(R.id.checkbox_fragment_tv_question_26)
    TextView tvQuestion26;
    @BindView(R.id.checkbox_fragment_tv_question_27)
    TextView tvQuestion27;
    @BindView(R.id.checkbox_fragment_tv_question_28)
    TextView tvQuestion28;
    @BindView(R.id.checkbox_fragment_tv_question_29)
    TextView tvQuestion29;
    @BindView(R.id.checkbox_fragment_tv_question_30)
    TextView tvQuestion30;
    @BindView(R.id.checkbox_fragment_tv_question_31)
    TextView tvQuestion31;
    @BindView(R.id.checkbox_fragment_tv_question_32)
    TextView tvQuestion32;
    @BindView(R.id.checkbox_fragment_tv_question_33)
    TextView tvQuestion33;

    @BindView(R.id.checkbox_fragment_fl_text_input_0)
    FrameLayout flTextInput0;
    @BindView(R.id.checkbox_fragment_fl_text_input_1)
    FrameLayout flTextInput1;
    @BindView(R.id.checkbox_fragment_fl_text_input_2)
    FrameLayout flTextInput2;
    @BindView(R.id.checkbox_fragment_fl_text_input_3)
    FrameLayout flTextInput3;
    @BindView(R.id.checkbox_fragment_fl_text_input_4)
    FrameLayout flTextInput4;
    @BindView(R.id.checkbox_fragment_fl_text_input_5)
    FrameLayout flTextInput5;
    @BindView(R.id.checkbox_fragment_fl_text_input_6)
    FrameLayout flTextInput6;
    @BindView(R.id.checkbox_fragment_fl_text_input_7)
    FrameLayout flTextInput7;
    @BindView(R.id.checkbox_fragment_fl_text_input_8)
    FrameLayout flTextInput8;
    @BindView(R.id.checkbox_fragment_fl_text_input_9)
    FrameLayout flTextInput9;
    @BindView(R.id.checkbox_fragment_fl_text_input_10)
    FrameLayout flTextInput10;
    @BindView(R.id.checkbox_fragment_fl_text_input_11)
    FrameLayout flTextInput11;
    @BindView(R.id.checkbox_fragment_fl_text_input_12)
    FrameLayout flTextInput12;
    @BindView(R.id.checkbox_fragment_fl_text_input_13)
    FrameLayout flTextInput13;
    @BindView(R.id.checkbox_fragment_fl_text_input_14)
    FrameLayout flTextInput14;
    @BindView(R.id.checkbox_fragment_fl_text_input_15)
    FrameLayout flTextInput15;
    @BindView(R.id.checkbox_fragment_fl_text_input_16)
    FrameLayout flTextInput16;
    @BindView(R.id.checkbox_fragment_fl_text_input_17)
    FrameLayout flTextInput17;
    @BindView(R.id.checkbox_fragment_fl_text_input_18)
    FrameLayout flTextInput18;
    @BindView(R.id.checkbox_fragment_fl_text_input_19)
    FrameLayout flTextInput19;
    @BindView(R.id.checkbox_fragment_fl_text_input_20)
    FrameLayout flTextInput20;
    @BindView(R.id.checkbox_fragment_fl_text_input_21)
    FrameLayout flTextInput21;
    @BindView(R.id.checkbox_fragment_fl_text_input_22)
    FrameLayout flTextInput22;
    @BindView(R.id.checkbox_fragment_fl_text_input_23)
    FrameLayout flTextInput23;
    @BindView(R.id.checkbox_fragment_fl_text_input_24)
    FrameLayout flTextInput24;
    @BindView(R.id.checkbox_fragment_fl_text_input_25)
    FrameLayout flTextInput25;
    @BindView(R.id.checkbox_fragment_fl_text_input_26)
    FrameLayout flTextInput26;
    @BindView(R.id.checkbox_fragment_fl_text_input_27)
    FrameLayout flTextInput27;
    @BindView(R.id.checkbox_fragment_fl_text_input_28)
    FrameLayout flTextInput28;
    @BindView(R.id.checkbox_fragment_fl_text_input_29)
    FrameLayout flTextInput29;
    @BindView(R.id.checkbox_fragment_fl_text_input_30)
    FrameLayout flTextInput30;
    @BindView(R.id.checkbox_fragment_fl_text_input_31)
    FrameLayout flTextInput31;
    @BindView(R.id.checkbox_fragment_fl_text_input_32)
    FrameLayout flTextInput32;
    @BindView(R.id.checkbox_fragment_fl_text_input_33)
    FrameLayout flTextInput33;

    @BindView(R.id.checkbox_fragment_fl_text_input_click_0)
    FrameLayout flClickListenerInput0;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_1)
    FrameLayout flClickListenerInput1;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_2)
    FrameLayout flClickListenerInput2;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_3)
    FrameLayout flClickListenerInput3;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_4)
    FrameLayout flClickListenerInput4;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_5)
    FrameLayout flClickListenerInput5;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_6)
    FrameLayout flClickListenerInput6;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_7)
    FrameLayout flClickListenerInput7;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_8)
    FrameLayout flClickListenerInput8;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_9)
    FrameLayout flClickListenerInput9;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_10)
    FrameLayout flClickListenerInput10;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_11)
    FrameLayout flClickListenerInput11;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_12)
    FrameLayout flClickListenerInput12;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_13)
    FrameLayout flClickListenerInput13;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_14)
    FrameLayout flClickListenerInput14;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_15)
    FrameLayout flClickListenerInput15;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_16)
    FrameLayout flClickListenerInput16;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_17)
    FrameLayout flClickListenerInput17;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_18)
    FrameLayout flClickListenerInput18;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_19)
    FrameLayout flClickListenerInput19;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_20)
    FrameLayout flClickListenerInput20;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_21)
    FrameLayout flClickListenerInput21;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_22)
    FrameLayout flClickListenerInput22;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_23)
    FrameLayout flClickListenerInput23;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_24)
    FrameLayout flClickListenerInput24;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_25)
    FrameLayout flClickListenerInput25;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_26)
    FrameLayout flClickListenerInput26;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_27)
    FrameLayout flClickListenerInput27;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_28)
    FrameLayout flClickListenerInput28;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_29)
    FrameLayout flClickListenerInput29;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_30)
    FrameLayout flClickListenerInput30;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_31)
    FrameLayout flClickListenerInput31;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_32)
    FrameLayout flClickListenerInput32;
    @BindView(R.id.checkbox_fragment_fl_text_input_click_33)
    FrameLayout flClickListenerInput33;

    @BindView(R.id.checkbox_fragment_et_text_input_0)
    EditText etTextInput0;
    @BindView(R.id.checkbox_fragment_et_text_input_1)
    EditText etTextInput1;
    @BindView(R.id.checkbox_fragment_et_text_input_2)
    EditText etTextInput2;
    @BindView(R.id.checkbox_fragment_et_text_input_3)
    EditText etTextInput3;
    @BindView(R.id.checkbox_fragment_et_text_input_4)
    EditText etTextInput4;
    @BindView(R.id.checkbox_fragment_et_text_input_5)
    EditText etTextInput5;
    @BindView(R.id.checkbox_fragment_et_text_input_6)
    EditText etTextInput6;
    @BindView(R.id.checkbox_fragment_et_text_input_7)
    EditText etTextInput7;
    @BindView(R.id.checkbox_fragment_et_text_input_8)
    EditText etTextInput8;
    @BindView(R.id.checkbox_fragment_et_text_input_9)
    EditText etTextInput9;
    @BindView(R.id.checkbox_fragment_et_text_input_10)
    EditText etTextInput10;
    @BindView(R.id.checkbox_fragment_et_text_input_11)
    EditText etTextInput11;
    @BindView(R.id.checkbox_fragment_et_text_input_12)
    EditText etTextInput12;
    @BindView(R.id.checkbox_fragment_et_text_input_13)
    EditText etTextInput13;
    @BindView(R.id.checkbox_fragment_et_text_input_14)
    EditText etTextInput14;
    @BindView(R.id.checkbox_fragment_et_text_input_15)
    EditText etTextInput15;
    @BindView(R.id.checkbox_fragment_et_text_input_16)
    EditText etTextInput16;
    @BindView(R.id.checkbox_fragment_et_text_input_17)
    EditText etTextInput17;
    @BindView(R.id.checkbox_fragment_et_text_input_18)
    EditText etTextInput18;
    @BindView(R.id.checkbox_fragment_et_text_input_19)
    EditText etTextInput19;
    @BindView(R.id.checkbox_fragment_et_text_input_20)
    EditText etTextInput20;
    @BindView(R.id.checkbox_fragment_et_text_input_21)
    EditText etTextInput21;
    @BindView(R.id.checkbox_fragment_et_text_input_22)
    EditText etTextInput22;
    @BindView(R.id.checkbox_fragment_et_text_input_23)
    EditText etTextInput23;
    @BindView(R.id.checkbox_fragment_et_text_input_24)
    EditText etTextInput24;
    @BindView(R.id.checkbox_fragment_et_text_input_25)
    EditText etTextInput25;
    @BindView(R.id.checkbox_fragment_et_text_input_26)
    EditText etTextInput26;
    @BindView(R.id.checkbox_fragment_et_text_input_27)
    EditText etTextInput27;
    @BindView(R.id.checkbox_fragment_et_text_input_28)
    EditText etTextInput28;
    @BindView(R.id.checkbox_fragment_et_text_input_29)
    EditText etTextInput29;
    @BindView(R.id.checkbox_fragment_et_text_input_30)
    EditText etTextInput30;
    @BindView(R.id.checkbox_fragment_et_text_input_31)
    EditText etTextInput31;
    @BindView(R.id.checkbox_fragment_et_text_input_32)
    EditText etTextInput32;
    @BindView(R.id.checkbox_fragment_et_text_input_33)
    EditText etTextInput33;

    @BindView(R.id.checkbox_fragment_tv_title)
    TextView tvTitle;

    @BindView(R.id.checkbox_fragment_image_survey_image)
    ImageView imageSurveyImage;

    @Inject
    SurveyContract.Presenter mPresenter;

    private int unselectedColor = Color.argb(255, 51, 51, 51);
    private int selectedColor = Color.argb(255, 0, 122, 255);

//    @Inject
//    public CheckboxQuestionFragment() {
//        // Requires empty public constructor
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.checkbox_question_fragment, container, false);
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
            mPresenter.takeCheckboxQuestionFragmentView(this, id);
        }
    }

    @Override
    public String getTextInput(int position) {
        String userInput;
        userInput = etTextInput(position).getText().toString();
        return userInput;
    }

    @Override
    public void showTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void showImage(Bitmap encodedImage, boolean isCached) {

        imageSurveyImage.setImageBitmap(encodedImage);

        if (isCached) return;
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
    public void showQuestion(String question, int position) {
        llQuestion(position).setVisibility(View.VISIBLE);
        tvQuestion(position).setText(question);
        llQuestion(position).setOnClickListener(view -> mPresenter.onCheckboxAnswerClicked(position, CHECKBOX_QUESTION));
    }

    @Override
    public void showTextInput(int position) {
        flTextInput(position).setVisibility(View.VISIBLE);

        flClickListenerInput(position).setOnClickListener(v -> {
            mPresenter.onEditTextClicked(position, CHECKBOX_QUESTION);
        });

        etTextInput(position).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userInput = etTextInput(position).getText().toString();
                mPresenter.onTextChanged(userInput, CHECKBOX_QUESTION);
            }
        });

        etTextInput(position).setImeOptions(EditorInfo.IME_ACTION_DONE);
        etTextInput(position).setRawInputType(InputType.TYPE_CLASS_TEXT);
        etTextInput(position).setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) etTextInput(position).clearFocus();
            return false;
        });

        etTextInput(position).setOnFocusChangeListener((v, hasFocus) -> {
            int pL = etTextInput(position).getPaddingLeft();
            int pT = etTextInput(position).getPaddingTop();
            int pR = etTextInput(position).getPaddingRight();
            int pB = etTextInput(position).getPaddingBottom();

            if (hasFocus) {
                etTextInput(position).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.frame_enter_text_focused));

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                flClickListenerInput(position).setVisibility(View.GONE);

            } else if (!hasFocus) {
                etTextInput(position).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.frame_enter_text_active));

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etTextInput5.getWindowToken(), 0);
                flClickListenerInput(position).setVisibility(View.VISIBLE);

                clearFocus();

                if (Strings.isNullOrEmpty(etTextInput(position).getText().toString())) {
                    mPresenter.onEmptyEditTextView(CHECKBOX_QUESTION);
                }

            }

            etTextInput(position).setPadding(pL, pT, pR, pB);
        });

    }

    @Override
    public void selectQuestion(int position) {
        imageSelector(position).setImageResource(R.drawable.ic_checkbox_checked);
        tvQuestion(position).setTextColor(selectedColor);
    }

    @Override
    public void unselectQuestion(int position) {
        imageSelector(position).setImageResource(R.drawable.ic_checkbox_unchecked);
        tvQuestion(position).setTextColor(unselectedColor);
    }


    @Override
    public void focusTextInput(boolean isFocused, int position) {
        if (isFocused) {

            for (int i = 0; i < 33; i++) {
                etTextInput(i).setFocusable(true);
                etTextInput(i).setFocusableInTouchMode(true);
            }

            etTextInput(position).requestFocus();
        } else {
            etTextInput(position).clearFocus();

            if (!TextUtils.isEmpty(etTextInput(position).getText())) {
                int pL = etTextInput(position).getPaddingLeft();
                int pT = etTextInput(position).getPaddingTop();
                int pR = etTextInput(position).getPaddingRight();
                int pB = etTextInput(position).getPaddingBottom();
                etTextInput(position).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.frame_enter_text_inactive));
                etTextInput(position).setPadding(pL, pT, pR, pB);
            }
        }
    }

    @Override
    public void clearFocus() {
        for (int i = 0; i < 33; i++) {
            etTextInput(i).setFocusable(false);
            etTextInput(i).setFocusableInTouchMode(false);
        }
    }

    private LinearLayout llQuestion(int position) {
        switch (position) {
            case 0:
                return llQuestion0;
            case 1:
                return llQuestion1;
            case 2:
                return llQuestion2;
            case 3:
                return llQuestion3;
            case 4:
                return llQuestion4;
            case 5:
                return llQuestion5;
            case 6:
                return llQuestion6;
            case 7:
                return llQuestion7;
            case 8:
                return llQuestion8;
            case 9:
                return llQuestion9;
            case 10:
                return llQuestion10;
            case 11:
                return llQuestion11;
            case 12:
                return llQuestion12;
            case 13:
                return llQuestion13;
            case 14:
                return llQuestion14;
            case 15:
                return llQuestion15;
            case 16:
                return llQuestion16;
            case 17:
                return llQuestion17;
            case 18:
                return llQuestion18;
            case 19:
                return llQuestion19;
            case 20:
                return llQuestion20;
            case 21:
                return llQuestion21;
            case 22:
                return llQuestion22;
            case 23:
                return llQuestion23;
            case 24:
                return llQuestion24;
            case 25:
                return llQuestion25;
            case 26:
                return llQuestion26;
            case 27:
                return llQuestion27;
            case 28:
                return llQuestion28;
            case 29:
                return llQuestion29;
            case 30:
                return llQuestion30;
            case 31:
                return llQuestion31;
            case 32:
                return llQuestion32;
            case 33:
                return llQuestion33;
        }
        return null;
    }

    private ImageView imageSelector(int position) {
        switch (position) {
            case 0:
                return imageSelector0;
            case 1:
                return imageSelector1;
            case 2:
                return imageSelector2;
            case 3:
                return imageSelector3;
            case 4:
                return imageSelector4;
            case 5:
                return imageSelector5;
            case 6:
                return imageSelector6;
            case 7:
                return imageSelector7;
            case 8:
                return imageSelector8;
            case 9:
                return imageSelector9;
            case 10:
                return imageSelector10;
            case 11:
                return imageSelector11;
            case 12:
                return imageSelector12;
            case 13:
                return imageSelector13;
            case 14:
                return imageSelector14;
            case 15:
                return imageSelector15;
            case 16:
                return imageSelector16;
            case 17:
                return imageSelector17;
            case 18:
                return imageSelector18;
            case 19:
                return imageSelector19;
            case 20:
                return imageSelector20;
            case 21:
                return imageSelector21;
            case 22:
                return imageSelector22;
            case 23:
                return imageSelector23;
            case 24:
                return imageSelector24;
            case 25:
                return imageSelector25;
            case 26:
                return imageSelector26;
            case 27:
                return imageSelector27;
            case 28:
                return imageSelector28;
            case 29:
                return imageSelector29;
            case 30:
                return imageSelector30;
            case 31:
                return imageSelector31;
            case 32:
                return imageSelector32;
            case 33:
                return imageSelector33;
        }
        return null;
    }

    private TextView tvQuestion(int position) {
        switch (position) {
            case 0:
                return tvQuestion0;
            case 1:
                return tvQuestion1;
            case 2:
                return tvQuestion2;
            case 3:
                return tvQuestion3;
            case 4:
                return tvQuestion4;
            case 5:
                return tvQuestion5;
            case 6:
                return tvQuestion6;
            case 7:
                return tvQuestion7;
            case 8:
                return tvQuestion8;
            case 9:
                return tvQuestion9;
            case 10:
                return tvQuestion10;
            case 11:
                return tvQuestion11;
            case 12:
                return tvQuestion12;
            case 13:
                return tvQuestion13;
            case 14:
                return tvQuestion14;
            case 15:
                return tvQuestion15;
            case 16:
                return tvQuestion16;
            case 17:
                return tvQuestion17;
            case 18:
                return tvQuestion18;
            case 19:
                return tvQuestion19;
            case 20:
                return tvQuestion20;
            case 21:
                return tvQuestion21;
            case 22:
                return tvQuestion22;
            case 23:
                return tvQuestion23;
            case 24:
                return tvQuestion24;
            case 25:
                return tvQuestion25;
            case 26:
                return tvQuestion26;
            case 27:
                return tvQuestion27;
            case 28:
                return tvQuestion28;
            case 29:
                return tvQuestion29;
            case 30:
                return tvQuestion30;
            case 31:
                return tvQuestion31;
            case 32:
                return tvQuestion32;
            case 33:
                return tvQuestion33;
        }
        return null;
    }

    private FrameLayout flTextInput(int position) {
        switch (position) {
            case 0:
                return flTextInput0;
            case 1:
                return flTextInput1;
            case 2:
                return flTextInput2;
            case 3:
                return flTextInput3;
            case 4:
                return flTextInput4;
            case 5:
                return flTextInput5;
            case 6:
                return flTextInput6;
            case 7:
                return flTextInput7;
            case 8:
                return flTextInput8;
            case 9:
                return flTextInput9;
            case 10:
                return flTextInput10;
            case 11:
                return flTextInput11;
            case 12:
                return flTextInput12;
            case 13:
                return flTextInput13;
            case 14:
                return flTextInput14;
            case 15:
                return flTextInput15;
            case 16:
                return flTextInput16;
            case 17:
                return flTextInput17;
            case 18:
                return flTextInput18;
            case 19:
                return flTextInput19;
            case 20:
                return flTextInput20;
            case 21:
                return flTextInput21;
            case 22:
                return flTextInput22;
            case 23:
                return flTextInput23;
            case 24:
                return flTextInput24;
            case 25:
                return flTextInput25;
            case 26:
                return flTextInput26;
            case 27:
                return flTextInput27;
            case 28:
                return flTextInput28;
            case 29:
                return flTextInput29;
            case 30:
                return flTextInput30;
            case 31:
                return flTextInput31;
            case 32:
                return flTextInput32;
            case 33:
                return flTextInput33;
        }
        return null;
    }

    private FrameLayout flClickListenerInput(int position) {
        switch (position) {
            case 0:
                return flClickListenerInput0;
            case 1:
                return flClickListenerInput1;
            case 2:
                return flClickListenerInput2;
            case 3:
                return flClickListenerInput3;
            case 4:
                return flClickListenerInput4;
            case 5:
                return flClickListenerInput5;
            case 6:
                return flClickListenerInput6;
            case 7:
                return flClickListenerInput7;
            case 8:
                return flClickListenerInput8;
            case 9:
                return flClickListenerInput9;
            case 10:
                return flClickListenerInput10;
            case 11:
                return flClickListenerInput11;
            case 12:
                return flClickListenerInput12;
            case 13:
                return flClickListenerInput13;
            case 14:
                return flClickListenerInput14;
            case 15:
                return flClickListenerInput15;
            case 16:
                return flClickListenerInput16;
            case 17:
                return flClickListenerInput17;
            case 18:
                return flClickListenerInput18;
            case 19:
                return flClickListenerInput19;
            case 20:
                return flClickListenerInput20;
            case 21:
                return flClickListenerInput21;
            case 22:
                return flClickListenerInput22;
            case 23:
                return flClickListenerInput23;
            case 24:
                return flClickListenerInput24;
            case 25:
                return flClickListenerInput25;
            case 26:
                return flClickListenerInput26;
            case 27:
                return flClickListenerInput27;
            case 28:
                return flClickListenerInput28;
            case 29:
                return flClickListenerInput29;
            case 30:
                return flClickListenerInput30;
            case 31:
                return flClickListenerInput31;
            case 32:
                return flClickListenerInput32;
            case 33:
                return flClickListenerInput33;
        }
        return null;
    }

    private EditText etTextInput(int position) {
        switch (position) {
            case 0:
                return etTextInput0;
            case 1:
                return etTextInput1;
            case 2:
                return etTextInput2;
            case 3:
                return etTextInput3;
            case 4:
                return etTextInput4;
            case 5:
                return etTextInput5;
            case 6:
                return etTextInput6;
            case 7:
                return etTextInput7;
            case 8:
                return etTextInput8;
            case 9:
                return etTextInput9;
            case 10:
                return etTextInput10;
            case 11:
                return etTextInput11;
            case 12:
                return etTextInput12;
            case 13:
                return etTextInput13;
            case 14:
                return etTextInput14;
            case 15:
                return etTextInput15;
            case 16:
                return etTextInput16;
            case 17:
                return etTextInput17;
            case 18:
                return etTextInput18;
            case 19:
                return etTextInput19;
            case 20:
                return etTextInput20;
            case 21:
                return etTextInput21;
            case 22:
                return etTextInput22;
            case 23:
                return etTextInput23;
            case 24:
                return etTextInput24;
            case 25:
                return etTextInput25;
            case 26:
                return etTextInput26;
            case 27:
                return etTextInput27;
            case 28:
                return etTextInput28;
            case 29:
                return etTextInput29;
            case 30:
                return etTextInput30;
            case 31:
                return etTextInput31;
            case 32:
                return etTextInput32;
            case 33:
                return etTextInput33;
        }
        return null;
    }


}
