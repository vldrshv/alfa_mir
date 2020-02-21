package ru.alfabank.alfamir.survey.presentation.dummy_view;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.survey.presentation.contract.SurveyContract;

public class SurveyViewDummy implements SurveyContract.ActivityView,
        SurveyContract.RadioButtonQuestionFragment, SurveyContract.ResultFragment,
        SurveyContract.IntroFragment, SurveyContract.ContinueFragment, SurveyContract.CheckboxQuestionFragment, SurveyContract.TextAreaQuestionFragment {

    @Override
    public void attachFragment(int fragmentType, int questionNumber, int animationType) {

    }

    @Override
    public void enableNextButton() {

    }

    @Override
    public void disableNextButton() {

    }

    @Override
    public void setDisabledNextButtonWarning(int fragmentType) {

    }

    @Override
    public void hideBottomBar() {

    }

    @Override
    public void hideAppBar() {

    }

    @Override
    public void showBottomBar() {

    }

    @Override
    public void showAppBar() {

    }

    @Override
    public void setCounterText(String text) {

    }

    @Override
    public void setNextButtonText(String text) {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void close() {

    }

    @Override
    public void showWarningOnExitUi() {

    }

    @Override
    public void showSnackBar(String text) {

    }


    @Override
    public void showIntroText() {

    }

    @Override
    public void showDate(String date) {

    }

    @Override
    public void showTitle(String title) {

    }

    @Override
    public void showDescription(String description) {

    }

    @Override
    public void setBarPercentage(int progress) {

    }

    @Override
    public void showCompletionStatus(String status) {

    }

    @Override
    public void hideDescription() {

    }

    @Override
    public void showImage(Bitmap encodedImage, boolean isCached) {

    }

    @Override
    public void showComment(String text) {

    }

    @Override
    public void showResult(String text) {

    }

    @Override
    public void hideResult() {

    }

    @Override
    public void setImageHeight(int height) {

    }

    @Override
    public void showQuestion(String question, int position) {

    }

    @Override
    public void showTextInput(int position) {

    }

    @Override
    public void selectQuestion(int position) {

    }

    @Override
    public void unselectQuestion(int position) {

    }

    @Override
    public void focusTextInput(boolean isFocused, int position) {

    }

    @Override
    public void clearFocus() {

    }

    @Override
    public void deselectQuestion(int position) {

    }

    @Override
    public void setUpETListener() {

    }

    @Override
    public String getTextInput() {
        return null;
    }

    @Override
    public String getTextInput(int position) {
        return "";
    }

    @Override
    public void showAnswerComment(boolean isCorrect, String text, int position) {

    }

    @Override
    public void showAnswerStatus(boolean isCorrect, int position) {

    }

    @Override
    public void disableOnTouchListeners() {

    }

    @Override
    public void enableOnTouchListeners() {

    }

}
