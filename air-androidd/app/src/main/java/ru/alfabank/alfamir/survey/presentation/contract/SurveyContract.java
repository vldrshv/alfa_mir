package ru.alfabank.alfamir.survey.presentation.contract;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

/**
 * Created by U_M0WY5 on 24.04.2018.
 */

public interface SurveyContract {

    interface View extends BaseView<Presenter> {}

    interface FragmentView extends View {}

    interface ActivityView extends View {
        void attachFragment(int fragmentType, int questionNumber, int animationType);
        void enableNextButton();
        void disableNextButton();
        void setDisabledNextButtonWarning(int fragmentType);
        void hideBottomBar();
        void hideAppBar();
        void showBottomBar();
        void showAppBar();
        void setCounterText(String text);
        void setNextButtonText(String text);
        void setTitle(String title);
        void close();
        void showWarningOnExitUi();
        void showSnackBar(String text);
    }

    interface Presenter extends BasePresenter<ActivityView> {
        void onRadioButtonAnswerClicked(int answerPosition, int fragmentType);
        void onCheckboxAnswerClicked(int answerPosition, int fragmentType);
        void onEditTextClicked(int answerPosition, int fragmentType);

        void onEmptyEditTextView(int fragmentType);

        void onStartButtonClicked();
        void onContinueButtonClicked();
        void onNextButtonClicked();
        void onDisabledNextButtonClicked(int fragmentType);
        void onDoneClicked();
        void onCloseClicked();
        void onBackClicked();
        void onExitWarningAccepted();
        void onBackPressed();

        void takeRadioButtonQuestionFragmentView(RadioButtonQuestionFragment view, int questionPosition);
        void takeCheckboxQuestionFragmentView(CheckboxQuestionFragment view, int questionPosition);
        void takeTextAreaFragment(TextAreaQuestionFragment view, int questionPosition);
        void takeResultFragmentView(ResultFragment view);
        void takeIntroFragmentView(IntroFragment view);
        void takeContinueFragment(ContinueFragment view);

        void onTextChanged(String text, int fragmentType);

        RadioButtonQuestionFragment getRadioButtonQuestionView();
        CheckboxQuestionFragment getCheckBoxQuestionView();
        TextAreaQuestionFragment getTextAreaQuestionView();

        ResultFragment getResultView();
        IntroFragment getIntroView();
        ContinueFragment getContinueView();
    }

    interface TextAreaQuestionFragment extends FragmentView {

        void showTitle(String title);
        void showImage(Bitmap encodedImage, boolean isAnimated);
        void setImageHeight(int height);
        void setUpETListener();
        String getTextInput();

    }

    interface RadioButtonQuestionFragment extends FragmentView {

        void showTitle(String title);
        void showImage(Bitmap encodedImage, boolean isAnimated);
        void setImageHeight(int height);

        void showQuestion(String question, int position);
        void showTextInput(int position);
        void selectQuestion(int position);
        void deselectQuestion(int position);

        void showAnswerComment(boolean isCorrect, String text, int position);

        void showAnswerStatus(boolean isCorrect, int position);

        void disableOnTouchListeners();

        void enableOnTouchListeners();

        void focusTextInput(boolean isFocused, int position);

        String getTextInput(int position);

    }

    interface CheckboxQuestionFragment extends FragmentView {

        void showTitle(String title);
        void showImage(Bitmap encodedImage, boolean isAnimated);
        void setImageHeight(int height);

        void showQuestion(String question, int position);
        void showTextInput(int position);
        void selectQuestion(int position);
        void unselectQuestion(int position);
        void focusTextInput(boolean isFocused, int position);
        void clearFocus();

        String getTextInput(int position);

    }

    interface ResultFragment extends FragmentView {
        void showComment(String text);
        void showResult(String text);
        void hideResult();
        void showImage(Bitmap encodedImage, boolean isCached);
    }

    interface IntroFragment extends FragmentView {
        void showIntroText();
        void showDate(String date);
        void showTitle(String title);
        void showDescription(String description);
        void hideDescription();
        void showImage(Bitmap encodedImage, boolean isCached);
    }

    interface ContinueFragment extends FragmentView {
        void showDate(String date);
        void showTitle(String title);
        void showDescription(String description);
        void setBarPercentage(int progress);
        void showCompletionStatus(String status);
        void hideDescription();
        void showImage(Bitmap encodedImage, boolean isCached);
    }

}
