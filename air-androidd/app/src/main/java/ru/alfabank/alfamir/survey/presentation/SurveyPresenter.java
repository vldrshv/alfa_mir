package ru.alfabank.alfamir.survey.presentation;//package ru.alfabank.alfamir.survey.presentation;
//
//
//import com.google.common.base.Strings;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.schedulers.Schedulers;
//import ru.alfabank.alfamir.GetImage;
//import ru.alfabank.alfamir.image.presentation.dto.Image;
//import ru.alfabank.alfamir.survey.getData.dto.Answer;
//import ru.alfabank.alfamir.survey.getData.dto.Survey;
//import ru.alfabank.alfamir.image.getData.source.repository.ImageDataSource;
//import ru.alfabank.alfamir.image.getData.source.repository.ImageRepository;
//import ru.alfabank.alfamir.survey.getData.source.repository.SurveyDataSource;
//import ru.alfabank.alfamir.survey.getData.source.repository.SurveyRepository;
//import ru.alfabank.alfamir.di.qualifiers.ID;
//import ru.alfabank.alfamir.di.qualifiers.IntentSource;
//import ru.alfabank.alfamir.survey.presentation.contract.SurveyContract;
//import ru.alfabank.alfamir.survey.presentation.dummy_view.SurveyViewDummy;
//import ru.alfabank.alfamir.utility.update_notifier.UpdateNotifier;
//import ru.alfabank.alfamir.utility.update_notifier.notifications.SurveyUpdate;
//import ru.alfabank.alfamir.utility.logging.remote.Logger;
//import ru.alfabank.alfamir.utility.logging.remote.LoggerContract;
//import ru.alfabank.alfamir.LinkHandler;
//
//import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_ADD;
//import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT;
//import static ru.alfabank.alfamir.Constants.FRAGMENT_TYPE_QUIZ_CHECKBOX;
//import static ru.alfabank.alfamir.Constants.FRAGMENT_TYPE_QUIZ_CONTINUE;
//import static ru.alfabank.alfamir.Constants.FRAGMENT_TYPE_QUIZ_INTRO;
//import static ru.alfabank.alfamir.Constants.FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION;
//import static ru.alfabank.alfamir.Constants.FRAGMENT_TYPE_QUIZ_RESULT;
//import static ru.alfabank.alfamir.Constants.FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION;
//import static ru.alfabank.alfamir.Constants.INTENT_SOURCE_CARD;
//import static ru.alfabank.alfamir.Constants.INTENT_SOURCE_DEFAULT;
//import static ru.alfabank.alfamir.Constants.INTENT_SOURCE_NOTIFICATION;
//import static ru.alfabank.alfamir.Constants.INTENT_SOURCE_PUSH;
//import static ru.alfabank.alfamir.Constants.Initialization.SCREEN_HEIGHT_ACTIVE_SIZE;
//import static ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_PHYSICAL;
//import static ru.alfabank.alfamir.Constants.Survey.CHECKBOX_QUESTION;
//import static ru.alfabank.alfamir.Constants.Survey.RADIO_BUTTON_QUESTION;
//import static ru.alfabank.alfamir.Constants.Survey.TEXT_AREA;
//
//public class SurveyPresenter implements SurveyContract.Presenter, LoggerContract.Client.Survey {
//
//    private SurveyRepository mQuizRepository;
//    private ImageRepository mImageRepository;
//    private GetImage mGetImage;
//
//    private SurveyContract.ActivityView mActivityQuizView;
//
//    private SurveyContract.RadioButtonQuestionFragment mFragmentRadioButtonQuestionView;
//    private SurveyContract.CheckboxQuestionFragment mCheckboxQuestionFragment;
//    private SurveyContract.ResultFragment mFragmentResultView;
//    private SurveyContract.IntroFragment mIntroFragment;
//    private SurveyContract.ContinueFragment mContinueFragment;
//    private SurveyContract.TextAreaQuestionFragment mTextAreaQuestionFragment;
//
//    private String mQuizId;
//    private int mIntentSource;
//
//    private int mQuestionsCount = -1;
//    private int mCurrentQuestion;
//    private int mCurrentAnswerPosition;
//    private int mCurrentFragmentType;
//    private int [] mCheckBoxAnswers;
//    private boolean mIsDataLoaded;
//
//    private static final int UNCHECKED = 0;
//    private static final int CHECKED = 1;
//
//    private LoggerContract.Provider mLogger;
//    private UpdateNotifier mUpdateNotifier;
//    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
//
//    @Inject
//    SurveyPresenter(@ID String quizId,
//                    @IntentSource int intentSource,
//                    GetImage fillImage,
//                    SurveyRepository quizRepository,
//                    ImageRepository imageRepository,
//                    UpdateNotifier updateNotifier,
//                    Logger logger){
//        mQuizId = quizId;
//        mIntentSource = intentSource;
//        mQuizRepository = quizRepository;
//        mImageRepository = imageRepository;
//        mUpdateNotifier = updateNotifier;
//        mLogger = logger;
//        mGetImage = fillImage;
//    }
//
//    /**
//     * view Related events
//     */
//
//    @Override
//    public void takeView(SurveyContract.ActivityView view) {
//        mActivityQuizView = view;
//        if(!mIsDataLoaded) loadQuiz();
//    }
//
//    @Override
//    public void dropView() {
//        mActivityQuizView = null;
//    }
//
//    @Override
//    public SurveyContract.ActivityView getView() {
//        if(mActivityQuizView == null) return new SurveyViewDummy();
//        return mActivityQuizView;
//    }
//
//    @Override
//    public void takeRadioButtonQuestionFragmentView(SurveyContract.RadioButtonQuestionFragment view, int questionPosition) {
//        if(!mIsDataLoaded) return; // in case fragment was recreated after app was destroyed, but presenter have nothing yet
//        mFragmentRadioButtonQuestionView = view;
//        mCurrentFragmentType = FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION;
//        showRadioButtonQuestion(mCurrentQuestion);
//    }
//
//    @Override
//    public void takeCheckboxQuestionFragmentView(SurveyContract.CheckboxQuestionFragment view, int questionPosition) {
//        if(!mIsDataLoaded) return; // in case fragment was recreated after app was destroyed, but presenter have nothing yet
//        mCheckboxQuestionFragment = view;
//        mCurrentFragmentType = FRAGMENT_TYPE_QUIZ_CHECKBOX;
//        showCheckboxQuestion(mCurrentQuestion);
//    }
//
//    @Override
//    public void takeTextAreaFragment(SurveyContract.TextAreaQuestionFragment view, int questionPosition) {
//        if(!mIsDataLoaded) return; // in case fragment was recreated after app was destroyed, but presenter have nothing yet
//        mTextAreaQuestionFragment = view;
//        mCurrentFragmentType = FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION;
//        showTextAreaQuestion(mCurrentQuestion);
//        // TODO
//    }
//
//    @Override
//    public void takeResultFragmentView(SurveyContract.ResultFragment view) {
//        if(!mIsDataLoaded) return; // in case fragment was recreated after app was destroyed, but presenter have nothing yet
//        mFragmentResultView = view;
//        mCurrentFragmentType = FRAGMENT_TYPE_QUIZ_RESULT;
//        showResult();
//    }
//
//    @Override
//    public void takeIntroFragmentView(SurveyContract.IntroFragment view) {
//        if(!mIsDataLoaded) return; // in case fragment was recreated after app was destroyed, but presenter have nothing yet
//        mIntroFragment = view;
//        mCurrentFragmentType = FRAGMENT_TYPE_QUIZ_INTRO;
//        showIntro();
//    }
//
//    @Override
//    public void takeContinueFragment(SurveyContract.ContinueFragment view) {
//        if(!mIsDataLoaded) return; // in case fragment was recreated after app was destroyed, but presenter have nothing yet
//        mContinueFragment = view;
//        mCurrentFragmentType = FRAGMENT_TYPE_QUIZ_CONTINUE;
//        showContinue();
//    }
//
//    /**
//     * onClick events
//     */
//
//    @Override
//    public void onRadioButtonAnswerClicked(int answerPosition, int fragmentType) {
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//                int showResultAfterAnswer = survey.getResult().getViewFlags().getShowResultAfterAnswer();
//                switch (showResultAfterAnswer){
//                    case 0:
//                        List <Survey.Question> questions = Arrays.asList(survey.getQuestions());
//                        List <Survey.Question.Answer> answers = Arrays.asList(questions.get(mCurrentQuestion).getAnswers());
//                        boolean hasTextInput = answers.get(answerPosition).getBody().hastTextInput();
//
//                        deselectAll();
//                        getRadioButtonQuestionView().selectQuestion(answerPosition);
//
//                        mCurrentAnswerPosition = answerPosition;
//
//                        if(hasTextInput) {
//                            getRadioButtonQuestionView().focusTextInput(true, answerPosition);
//                            String userInput = getRadioButtonQuestionView().getTextInput(answerPosition);
//                            if(Strings.isNullOrEmpty(userInput)){
//                                getView().disableNextButton();
//                            }
//                        } else {
//                            getView().enableNextButton();
//                        }
//
//                        break;
//                    case 1: {
//                        deselectAll();
//
//                        mCurrentAnswerPosition = answerPosition;
//                        getRadioButtonQuestionView().disableOnTouchListeners();
//                        validateAnswer();
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable() {  }
//        });
//    }
//
//    @Override
//    public void onCheckboxAnswerClicked(int answerPosition, int fragmentType) {
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//                List <Survey.Question> questions = Arrays.asList(survey.getQuestions());
//                List <Survey.Question.Answer> answers = Arrays.asList(questions.get(mCurrentQuestion).getAnswers());
//                boolean hasTextInput = answers.get(answerPosition).getBody().hastTextInput();
//
//                switch (mCheckBoxAnswers[answerPosition]){
//                    case UNCHECKED:
//                        getCheckBoxQuestionView().clearFocus();
//                        getCheckBoxQuestionView().selectQuestion(answerPosition);
//                        if (hasTextInput) getCheckBoxQuestionView().focusTextInput(true, answerPosition);
//                        break;
//                    case CHECKED:
//                        getCheckBoxQuestionView().clearFocus();
//                        getCheckBoxQuestionView().unselectQuestion(answerPosition);
//                        if (hasTextInput) getCheckBoxQuestionView().focusTextInput(false, answerPosition);
//                        break;
//                }
//
//                if(mCheckBoxAnswers[answerPosition] == UNCHECKED){
//                    mCheckBoxAnswers[answerPosition] = CHECKED;
//                } else if(mCheckBoxAnswers[answerPosition] == CHECKED){
//                    mCheckBoxAnswers[answerPosition] = UNCHECKED;
//                }
//
//                checkButtonNextStatus(answers);
//
//            }
//
//            @Override
//            public void onDataNotAvailable() {}
//        });
//
//    }
//
//    @Override
//    public void onEditTextClicked(int answerPosition, int fragmentType) {
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//                List <Survey.Question> questions = Arrays.asList(survey.getQuestions());
//                List <Survey.Question.Answer> answers = Arrays.asList(questions.get(mCurrentQuestion).getAnswers());
//                boolean hasTextInput = answers.get(answerPosition).getBody().hastTextInput();
//
//                switch (fragmentType){
//                    case RADIO_BUTTON_QUESTION:{
//                        deselectAll();
//                        getRadioButtonQuestionView().selectQuestion(answerPosition);
//                        getRadioButtonQuestionView().focusTextInput(true, answerPosition);
//
//                        if(hasTextInput) {
//                            getRadioButtonQuestionView().focusTextInput(true, answerPosition);
//                            String userInput = getCheckBoxQuestionView().getTextInput(answerPosition);
//                            if(Strings.isNullOrEmpty(userInput)){
//                                getView().disableNextButton();
//                            }
//                        }
//                        break;
//                    }
//                    case CHECKBOX_QUESTION:{
//                        getCheckBoxQuestionView().clearFocus();
//                        getCheckBoxQuestionView().selectQuestion(answerPosition);
//                        getCheckBoxQuestionView().focusTextInput(true, answerPosition);
//
//                        if(mCheckBoxAnswers[answerPosition]==UNCHECKED){
//                            mCheckBoxAnswers[answerPosition] = CHECKED;
//                        }
//                        checkButtonNextStatus(answers);
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable() {}
//        });
//
//    }
//
//    @Override
//    public void onStartButtonClicked() {
//        getView().showBottomBar();
//        getView().showAppBar();
//
//        int animationType = FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT;
//        setUpNextQuestion(animationType);
//    }
//
//    @Override
//    public void onContinueButtonClicked() {
//        getView().showBottomBar();
//        getView().showAppBar();
//
//        int animationType = FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT;
//        setUpNextQuestion(animationType);
//    }
//
//    @Override
//    public void onNextButtonClicked() {
//
//
//        getView().disableNextButton();
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//
//
//                String sQuestionType = survey.getQuestions()[mCurrentQuestion].getBody().getType();
//                int showResultAfterAnswer = survey.getResult().getViewFlags().getShowResultAfterAnswer();
//
//                if(showResultAfterAnswer==0||sQuestionType.equals("textarea")) { // if we have a textarea question, no need in showing the validation info
//                    saveAnswer();
//                } else {
//                    if((mCurrentQuestion + 1) < mQuestionsCount){
//                        mCurrentQuestion++;
//                        int animationType = FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT;
//                        setUpNextQuestion(animationType);
//                    } else {
//                        valuateResult();
//                    }
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                getView().enableNextButton();
//                getView().showSnackBar("Что-то пошло не так");
//            }
//        });
//
//    }
//
//    @Override
//    public void onDisabledNextButtonClicked(int fragmentType) {
//        String text = "";
//        switch (fragmentType){
//            case CHECKBOX_QUESTION:
//                text = "Заполните текстовое поле или снимите выделение";
//                break;
//            case RADIO_BUTTON_QUESTION:
//                text = "Заполните текстовое поле или выберите другой вариант";
//                break;
//            case TEXT_AREA:
//                text = "Заполните текстовое поле";
//                break;
//        }
//        getView().showSnackBar(text);
//    }
//
//    @Override
//    public void onDoneClicked() {
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//                logFinish(survey.getType(), survey.getID());
//            }
//
//            @Override
//            public void onDataNotAvailable() {}
//        });
//
//        getView().close();
//    }
//
//    @Override
//    public void onCloseClicked() {
//        getView().showWarningOnExitUi();
//    }
//
//    @Override
//    public void onBackClicked() {
//        getView().close();
//    }
//
//    @Override
//    public void onExitWarningAccepted() {
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//                logExit(survey.getType(), survey.getID());
//            }
//
//            @Override
//            public void onDataNotAvailable() {}
//        });
//        mQuizRepository.refreshSurvey();
//        getView().close();
//    }
//
//    @Override
//    public void onEmptyEditTextView(int fragmentType) {
//        setDisabledButtonListener(fragmentType);
//    }
//
//    @Override
//    public void onBackPressed() {
////        mActivityQuizView.close();
//        if(mCurrentFragmentType==FRAGMENT_TYPE_QUIZ_INTRO ||
//                mCurrentFragmentType==FRAGMENT_TYPE_QUIZ_CONTINUE ||
//                mCurrentFragmentType==FRAGMENT_TYPE_QUIZ_RESULT){
//            getView().close();
//        } else {
//            getView().showWarningOnExitUi();
//        }
//    }
//
//    @Override
//    public void onTextChanged(String text, int fragmentType) {
//        switch (fragmentType){
//            case CHECKBOX_QUESTION:
//                mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//                    @Override
//                    public void onSurveyLoaded(Survey survey) {
//                        List <Survey.Question> questions = Arrays.asList(survey.getQuestions());
//                        List <Survey.Question.Answer> answers = Arrays.asList(questions.get(mCurrentQuestion).getAnswers());
//                        checkButtonNextStatus(answers);
//                    }
//
//                    @Override
//                    public void onDataNotAvailable() {}
//                });
//                break;
//            case RADIO_BUTTON_QUESTION:
//            case TEXT_AREA:
//                if(Strings.isNullOrEmpty(text)){
//                    getView().disableNextButton();
//                } else {
//                    getView().enableNextButton();
//                }
//        }
//
//    }
//
//    @Override
//    public SurveyContract.ResultFragment getResultView() {
//        if(mFragmentResultView == null){
//            return new SurveyViewDummy();
//        }
//        return mFragmentResultView;
//    }
//
//    @Override
//    public SurveyContract.IntroFragment getIntroView() {
//        if(mIntroFragment == null){
//            return new SurveyViewDummy();
//        }
//        return mIntroFragment;
//    }
//
//    @Override
//    public SurveyContract.ContinueFragment getContinueView() {
//        if(mContinueFragment == null){
//            return new SurveyViewDummy();
//        }
//        return mContinueFragment;
//    }
//
//    @Override
//    public SurveyContract.RadioButtonQuestionFragment getRadioButtonQuestionView() {
//        if(mFragmentRadioButtonQuestionView == null){
//            return new SurveyViewDummy();
//        }
//        return mFragmentRadioButtonQuestionView;
//    }
//
//    @Override
//    public SurveyContract.CheckboxQuestionFragment getCheckBoxQuestionView() {
//        if(mCheckboxQuestionFragment == null){
//            return new SurveyViewDummy();
//        }
//        return mCheckboxQuestionFragment;
//    }
//
//    @Override
//    public SurveyContract.TextAreaQuestionFragment getTextAreaQuestionView() {
//        if(mTextAreaQuestionFragment == null){
//            return new SurveyViewDummy();
//        }
//        return mTextAreaQuestionFragment;
//    }
//
//    // private methods
//
//    private void checkButtonNextStatus(List <Survey.Question.Answer> answers){
//        boolean isEnabled = false;
//        for (int i = 0; i < mCheckBoxAnswers.length; i++){
//            if(mCheckBoxAnswers[i]==1){
//                boolean hasTextInput = answers.get(i).getBody().hastTextInput();
//                if(hasTextInput) {
//                    String userInput = getCheckBoxQuestionView().getTextInput(i);
//                    if(Strings.isNullOrEmpty(userInput)){
//                        isEnabled = false;
//                        break;
//                    }
//                }
//                isEnabled = true;
//            }
//        }
//
//        if (isEnabled){
//            getView().enableNextButton();
//        } else {
//            getView().disableNextButton();
//        }
//
//    }
//
//    private void setDisabledButtonListener(int fragmentType){
//        getView().setDisabledNextButtonWarning(fragmentType);
//    }
//
//    private void loadQuiz(){
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//                int animationType = FRAGMENT_TRANSITION_ADD;
//                int userAnsweredQuestionCount = survey.getCover().getRespondentAnswersCount();
//                mQuestionsCount = survey.getQuestions().length;
//                mCurrentQuestion = userAnsweredQuestionCount;
//
//                String title = survey.getCover().getSurveyName();
//                getView().setTitle(title);
//
////                mCurrentQuestion = 0; // for testing
////                userAnsweredQuestionCount = 0; // for testing
////                mIntentSource = INTENT_SOURCE_NOTIFICATION; // for testing
//
//                if(userAnsweredQuestionCount == 0){ // no answers
//                    int showIntroScreen = survey.getResult().getViewFlags().getIntroScreen(); // check for intro screen
////                    int showIntroScreen = 1; // for testing
//                    if (showIntroScreen == 0){
//                        // show questions
//                        getView().showBottomBar();
//                        getView().showAppBar();
//                        setUpNextQuestion(animationType);
//                    } else if (showIntroScreen == 1) {
//                        // show intro screen
//                        getView().attachFragment(FRAGMENT_TYPE_QUIZ_INTRO, mCurrentQuestion, animationType);
//                    }
//                } else { // there are answers
//                    if(userAnsweredQuestionCount == mQuestionsCount){ // there are all the answers
//                        // show results
////                        setUpResultScreen();
//                        getView().attachFragment(FRAGMENT_TYPE_QUIZ_RESULT, mCurrentQuestion, animationType);
//                    } else { // there are some answers
//                        switch (mIntentSource) {
//                            case INTENT_SOURCE_PUSH:
//                            case INTENT_SOURCE_NOTIFICATION:{
//                                // show continue screen
//                                getView().attachFragment(FRAGMENT_TYPE_QUIZ_CONTINUE, mCurrentQuestion, animationType);
//                                break;
//                            }
//                            case INTENT_SOURCE_CARD:
//                            case INTENT_SOURCE_DEFAULT:{
//                                // show question
//                                getView().showBottomBar();
//                                getView().showAppBar();
//
//                                setUpNextQuestion(animationType);
//                            }
//                        }
//                    }
//                }
//
//                logOpen(survey.getType(), survey.getID());
//                mIsDataLoaded = true;
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                getView().showSnackBar("Что-то пошло не так");
//            }
//        });
//    }
//
//    private void loadImage(int fragmentType, String imageUrl, int imageHeight){
//        mCompositeDisposable.add(mGetImage.execute(new GetImage.RequestValues(imageUrl, SCREEN_WIDTH_PHYSICAL))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responseValue -> {
//                    Image image = responseValue.fillImage();
//                    String encodedImage = image.getEncodedImage();
//                    boolean isCached = image.getIsCached();
//                    if(Strings.isNullOrEmpty(encodedImage)) return; // TODO do i really need this?
//                    switch (fragmentType){
//                        case FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION:{
//                            getRadioButtonQuestionView().showImage(encodedImage, isCached);
//                            break;
//                        }
//                        case FRAGMENT_TYPE_QUIZ_CHECKBOX:{
//                            getCheckBoxQuestionView().showImage(encodedImage, isCached);
//                            break;
//                        }
//                        case FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION:{
//                            getTextAreaQuestionView().showImage(encodedImage, isCached);
//                            break;
//                        }
//                    }
//                }, throwable -> {
//                    getView().showSnackBar("Что-то пошло не так");
//                }));
////        mImageRepository.fillImage(imageUrl, SCREEN_WIDTH_PHYSICAL, imageHeight, new ImageDataSource.LoadImageCallback() {
////            @Override
////            public void onImageLoaded(Image img, boolean isCached) {
////                String encodedImage = img.getBinaryImage();
////                switch (fragmentType){
////                    case FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION:{
////                        getRadioButtonQuestionView().showImage(encodedImage, isCached);
////                        break;
////                    }
////                    case FRAGMENT_TYPE_QUIZ_CHECKBOX:{
////                        getCheckBoxQuestionView().showImage(encodedImage, isCached);
////                        break;
////                    }
////                    case FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION:{
////                        getTextAreaQuestionView().showImage(encodedImage, isCached);
////                        break;
////                    }
////                }
////            }
////
////            @Override
////            public void onDataNotAvailable() {
////                getView().showSnackBar("Что-то пошло не так"); // TODO should give an option to redownload
////            }
////        });
//    }
//
//    private void loadBackgroundImage(String imageUrlWithSize, int screenType){
//        String backgroundImageUrl = LinkHandler.getPhotoLink(imageUrlWithSize);
//        int width = LinkHandler.getPhotoPixelWidthMatchingScreenHeight(imageUrlWithSize);
//
//        mCompositeDisposable.add(mGetImage.execute(new GetImage.RequestValues(imageUrlWithSize, SCREEN_WIDTH_PHYSICAL))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responseValue -> {
//                    Image image = responseValue.fillImage();
//                    String encodedImage = image.getEncodedImage();
//                    boolean isCached = image.getIsCached();
//                    if(Strings.isNullOrEmpty(encodedImage)) return; // TODO do i really need this?
//                    switch (screenType) {
//                        case FRAGMENT_TYPE_QUIZ_INTRO:
//                            getIntroView().showImage(encodedImage, isCached);
//                            break;
//                        case FRAGMENT_TYPE_QUIZ_RESULT:
//                            getResultView().showImage(encodedImage, isCached);
//                            break;
//                        case FRAGMENT_TYPE_QUIZ_CONTINUE:
//                            getContinueView().showImage(encodedImage, isCached);
//                            break;
//                    }
//                }, throwable -> {
//
//                }));
//
////        mImageRepository.fillImage(backgroundImageUrl, width, SCREEN_HEIGHT_ACTIVE_SIZE, new ImageDataSource.LoadImageCallback() {
////            @Override
////            public void onImageLoaded(Image img, boolean isCached) {
////                String encodedImage = img.getBinaryImage();
////                switch (screenType) {
////                    case FRAGMENT_TYPE_QUIZ_INTRO:
////                        getIntroView().showImage(encodedImage, isCached);
////                        break;
////                    case FRAGMENT_TYPE_QUIZ_RESULT:
////                        getResultView().showImage(encodedImage, isCached);
////                        break;
////                    case FRAGMENT_TYPE_QUIZ_CONTINUE:
////                        getContinueView().showImage(encodedImage, isCached);
////                        break;
////                }
////            }
////
////            @Override
////            public void onDataNotAvailable() {
////                getView().showSnackBar("Что-то пошло не так"); // TODO should give an option to redownload
////            }
////        });
//    }
//
//    private void showRadioButtonQuestion(int currentQuestionNumber){
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//
//                List <Survey.Question> questions = Arrays.asList(survey.getQuestions());
//                Survey.Question currentQuestion = questions.get(currentQuestionNumber);
//
//                logShowQuestion(survey.getType(), survey.getID(), currentQuestion.getId());
//
//                String imageUrlDirty = currentQuestion.getBody().getImgUrl();
//                String questionText = currentQuestion.getBody().getText();
//
//                List <Survey.Question.Answer> answers = Arrays.asList(questions.get(currentQuestionNumber).getAnswers());
//
//                getRadioButtonQuestionView().showTitle(questionText);
//                for(int i = 0; i < answers.size(); i++){
//                    String answer = answers.get(i).getBody().getText();
//                    boolean hasTextInput = answers.get(i).getBody().hastTextInput();
//                    int position = i;
//                    showRadioButtonAnswer(answer, position, hasTextInput);
//                }
//
//                if(Strings.isNullOrEmpty(imageUrlDirty)){
//                    return;
//                }
//
//                int imageHeight = LinkHandler.getPhotoPixelHeightMatchingScreenWidth(imageUrlDirty);
//                String imageUrl = LinkHandler.getPhotoLink(imageUrlDirty);
//                getRadioButtonQuestionView().setImageHeight(imageHeight);
//                loadImage(FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION, imageUrl, imageHeight);
//            }
//
//            @Override
//            public void onDataNotAvailable() {}
//        });
//    }
//
//    private void showCheckboxQuestion(int questionNumber){
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//
//                List <Survey.Question> questions = Arrays.asList(survey.getQuestions());
//                Survey.Question currentQuestion = questions.get(questionNumber);
//
//                logShowQuestion(survey.getType(), survey.getID(), currentQuestion.getId());
//
//                String imageUrlDirty = currentQuestion.getBody().getImgUrl();
//                String questionText = currentQuestion.getBody().getText();
//
//                List <Survey.Question.Answer> answers = Arrays.asList(questions.get(questionNumber).getAnswers());
//
//                mCheckBoxAnswers = new int[answers.size()];
//
//                getCheckBoxQuestionView().showTitle(questionText);
//                for(int i = 0; i < answers.size(); i++){
//                    if(i > 33) break; // a bit hacky but oh well
//
//                    String answer = answers.get(i).getBody().getText();
//                    boolean hasTextInput = answers.get(i).getBody().hastTextInput();
//                    int position = i;
//                    showCheckboxAnswer(answer, position, hasTextInput);
//                }
//
//                if(Strings.isNullOrEmpty(imageUrlDirty)){
//                    return;
//                }
//
//                int imageHeight = LinkHandler.getPhotoPixelHeightMatchingScreenWidth(imageUrlDirty);
//                String imageUrl = LinkHandler.getPhotoLink(imageUrlDirty);
//                getCheckBoxQuestionView().setImageHeight(imageHeight);
//                loadImage(FRAGMENT_TYPE_QUIZ_CHECKBOX, imageUrl, imageHeight);
//            }
//
//            @Override
//            public void onDataNotAvailable() {}
//        });
//    }
//
//    private void showTextAreaQuestion(int questionNumber){
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//
//                List <Survey.Question> questions = Arrays.asList(survey.getQuestions());
//                Survey.Question currentQuestion = questions.get(questionNumber);
//
//                logShowQuestion(survey.getType(), survey.getID(), currentQuestion.getId());
//
//                String imageUrlDirty = currentQuestion.getBody().getImgUrl();
//                String questionText = currentQuestion.getBody().getText();
//
//                getTextAreaQuestionView().showTitle(questionText);
//                getTextAreaQuestionView().setUpETListener();
//                setDisabledButtonListener(TEXT_AREA);
//
//
//                if(Strings.isNullOrEmpty(imageUrlDirty)){
//                    return;
//                }
//
//                int imageHeight = LinkHandler.getPhotoPixelHeightMatchingScreenWidth(imageUrlDirty);
//                String imageUrl = LinkHandler.getPhotoLink(imageUrlDirty);
//                getRadioButtonQuestionView().setImageHeight(imageHeight);
//                loadImage(FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION, imageUrl, imageHeight);
//            }
//
//            @Override
//            public void onDataNotAvailable() {}
//        });
//    }
//
//    private void showCheckboxAnswer(String answer, int position, boolean hasTextInput){
//        getCheckBoxQuestionView().showQuestion(answer, position);
//        if (hasTextInput) getCheckBoxQuestionView().showTextInput(position);
//    }
//
//    private void showRadioButtonAnswer(String answer, int position, boolean hasTextInput){
//        getRadioButtonQuestionView().showQuestion(answer, position);
//        if (hasTextInput) getRadioButtonQuestionView().showTextInput(position);
//    }
//
//    private void showContinue(){
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//                String name = survey.getCover().getSurveyName();
//                String date = survey.getCover().getEndDate();
//                String title = survey.getCover().getTitle();
//                String description = survey.getCover().getDescription();
//
//                int total = survey.getCover().getRequredAnswersCount();
//                int current = survey.getCover().getRespondentAnswersCount();
//                int percent = (int)((current * 100.0f) / total);
//
//                String imageUrlWithSize = survey.getCover().getImgUrl();
//                loadBackgroundImage(imageUrlWithSize, FRAGMENT_TYPE_QUIZ_CONTINUE);
//
//                getContinueView().showDate(name + " до " + date);
//                getContinueView().showTitle(title);
//                getContinueView().setBarPercentage(percent);
//                getContinueView().showCompletionStatus("Завершено на " + percent + "%");
//
//                if(Strings.isNullOrEmpty(description)){
//                    getContinueView().hideDescription();
//                } else {
//                    getContinueView().showDescription(description);
//                }
//
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//
//            }
//        });
//    }
//
//    private void showIntro(){
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//                String name = survey.getCover().getSurveyName();
//                String date = survey.getCover().getEndDate();
//                String title = survey.getCover().getTitle();
//                String description = survey.getCover().getDescription();
//
//                String imageUrlWithSize = survey.getCover().getImgUrl();
//                loadBackgroundImage(imageUrlWithSize, FRAGMENT_TYPE_QUIZ_INTRO);
//
//                getIntroView().showDate(name + " до " + date);
//                getIntroView().showTitle(title);
//
//                if(Strings.isNullOrEmpty(description)){
//                    getIntroView().hideDescription();
//                } else {
//                    getIntroView().showDescription(description);
//                }
//
//                getIntroView().showIntroText();
//
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//
//            }
//        });
//    }
//
//    private void showResult(){
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//                logResultScreen(survey.getType(), survey.getID());
//
//                String imageUrlWithSize = survey.getCover().getImgUrl();
//                loadBackgroundImage(imageUrlWithSize, FRAGMENT_TYPE_QUIZ_RESULT);
//
//                String message = survey.getResult().getMessage();
//
//                int showResult = survey.getResult().getViewFlags().getQuizResult();
//                if(showResult == 0){
//                    getResultView().hideResult();
//                } else if (showResult == 1){
//                    String result = survey.getResult().getQuizResult();
//                    getResultView().showResult(result);
//                }
//
//                getResultView().showComment(message);
//
//
//                getView().hideBottomBar();
//                getView().hideAppBar();
//            }
//
//            @Override
//            public void onDataNotAvailable() {}
//        });
//    }
//
//    private void saveAnswer(){
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//                String sQuestionType = survey.getQuestions()[mCurrentQuestion].getBody().getType();
//                if(sQuestionType == null) return;
//
//                if(sQuestionType.equals("radio")){
//                    Survey.Question question = survey.getQuestions()[mCurrentQuestion];
//                    Survey.Question.Answer questionAnswer = question.getAnswers()[mCurrentAnswerPosition];
//
//                    int questionId = question.getId();
//                    int answerId = questionAnswer.getID();
//
//                    Answer answer = new Answer();
//                    answer.setAnswerId(answerId);
//                    answer.setQuestionId(questionId);
//
//                    boolean hasTextInput = questionAnswer.getBody().hastTextInput();
//                    if(hasTextInput){
//                        String userInput = getRadioButtonQuestionView().getTextInput(mCurrentAnswerPosition);
//                        answer.setText(userInput);
//                    }
//
//                    saveAnswerRemotely(answer);
//
//                } else if (sQuestionType.equals("checkbox")){
//                    Survey.Question question = survey.getQuestions()[mCurrentQuestion];
//
//                    List<Answer> answers = new ArrayList<>();
//
//                    for (int i = 0; i < mCheckBoxAnswers.length; i++){
//
//                        if(mCheckBoxAnswers[i] == 0) {
//
//                        } else {
//                            Survey.Question.Answer questionAnswer = question.getAnswers()[i];
//
//                            int questionId = question.getId();
//                            int answerId = questionAnswer.getID();
//
//                            Answer answer = new Answer();
//                            answer.setAnswerId(answerId);
//                            answer.setQuestionId(questionId);
//
//                            boolean hasTextInput = questionAnswer.getBody().hastTextInput();
//                            if(hasTextInput){
//                                String userInput = getCheckBoxQuestionView().getTextInput(i);
//                                answer.setText(userInput);
//                            }
//
//                            answers.add(answer);
//                        }
//
//                    }
//
//                    saveAnswersRemotely(answers);
//
//                } else if (sQuestionType.equals("textarea")){
//                    Survey.Question question = survey.getQuestions()[mCurrentQuestion];
//                    int questionId = question.getId();
//
//                    Answer answer = new Answer();
//                    answer.setQuestionId(questionId);
//
//                    String userInput = getTextAreaQuestionView().getTextInput();
//                    answer.setText(userInput);
//
//                    saveAnswerRemotely(answer);
//                }
//
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                getView().showSnackBar("Что-то пошло не так"); // TODO should give an option to redownload
//            }
//        });
//    }
//
//    private void validateAnswer(){
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//                    @Override
//                    public void onSurveyLoaded(Survey survey) {
//                        Survey.Question question = survey.getQuestions()[mCurrentQuestion];
//                        Survey.Question.Answer userAnswer = question.getAnswers()[mCurrentAnswerPosition];
//
//                        int questionId = question.getId();
//                        int answerId = userAnswer.getID();
//
//                        Answer answer = new Answer();
//                        answer.setAnswerId(answerId);
//                        answer.setQuestionId(questionId);
//
//                        mQuizRepository.uploadAnswer(mQuizId, answer, new SurveyDataSource.UploadAnswerCallback() {
//                            @Override
//                            public void onAnswerUploaded() {
//                                mUpdateNotifier.reportUpdate(new SurveyUpdate());
//                                // TODO show right answer
//                                mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//                                            @Override
//                                            public void onSurveyLoaded(Survey survey) {
//
//                                                int isCorrect = userAnswer.getBody().getCorrectAnswer();
//                                                if(isCorrect==1){
//                                                    int position = mCurrentAnswerPosition;
//                                                    boolean correct = true;
//                                                    String comment = userAnswer.getBody().getCommentOnAnswer();
//
//                                                    getRadioButtonQuestionView().showAnswerStatus(correct, position);
//                                                    if(!Strings.isNullOrEmpty(comment)) {
//                                                        getRadioButtonQuestionView().showAnswerComment(correct, comment, position);
//                                                    }
//                                                } else {
//                                                    List<Survey.Question.Answer> questionAnswers = Arrays.asList(question.getAnswers());
//
//                                                    Survey.Question.Answer answer;
//                                                    for (int i = 0; i < questionAnswers.size(); i++) {
//                                                        answer = questionAnswers.get(i);
//                                                        if(answer.getBody().getCorrectAnswer()==1) {
//                                                            int position = i;
//                                                            boolean correct = true;
//
//                                                            getRadioButtonQuestionView().showAnswerStatus(correct, position);
//                                                        }
//                                                    }
//
//                                                    int position = mCurrentAnswerPosition;
//                                                    boolean correct = false;
//                                                    String comment = userAnswer.getBody().getCommentOnAnswer();
//
//                                                    getRadioButtonQuestionView().showAnswerStatus(correct, position);
//                                                    if(!Strings.isNullOrEmpty(comment)) {
//                                                        getRadioButtonQuestionView().showAnswerComment(correct, comment, position);
//                                                    }
//
//                                                }
//
//                                                getView().enableNextButton();
//
//                                            }
//
//                                            @Override
//                                            public void onDataNotAvailable() {
//
//                                            }
//                                });
//                            }
//
//                            @Override
//                            public void onDataNotAvailable() {
//                                getRadioButtonQuestionView().selectQuestion(mCurrentAnswerPosition);
//                                getRadioButtonQuestionView().enableOnTouchListeners();
//                                getView().showSnackBar("Что-то пошло не так");
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onDataNotAvailable() {
//
//                    }
//        });
//    }
//
//    private void saveAnswerRemotely(Answer answer){
//        mQuizRepository.uploadAnswer(mQuizId, answer, new SurveyDataSource.UploadAnswerCallback() {
//            @Override
//            public void onAnswerUploaded() {
//                mUpdateNotifier.reportUpdate(new SurveyUpdate());
//                if((mCurrentQuestion + 1) < mQuestionsCount){
//                    mCurrentQuestion++;
//                    int animationType = FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT;
//                    setUpNextQuestion(animationType);
//                } else if ((mCurrentQuestion + 1) == mQuestionsCount) {
//                    valuateResult();
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                getView().showSnackBar("Что-то пошло не так");
//            }
//        });
//    }
//
//    private void saveAnswersRemotely(List <Answer> answers){
//        mQuizRepository.uploadAnswers(mQuizId, answers, new SurveyDataSource.UploadAnswersCallback() {
//            @Override
//            public void onAnswersUploaded() {
//                mUpdateNotifier.reportUpdate(new SurveyUpdate());
//                if((mCurrentQuestion + 1) < mQuestionsCount){
//                    mCurrentQuestion++;
//                    int animationType = FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT;
//                    setUpNextQuestion(animationType);
//                } else if ((mCurrentQuestion + 1) == mQuestionsCount) {
//                    valuateResult();
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                getView().showSnackBar("Что-то пошло не так");
//            }
//        });
//    }
//
//    private void setUpNextQuestion(int animationType){
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//                int questionType = 0;
//                String sQuestionType = survey.getQuestions()[mCurrentQuestion].getBody().getType();
//                if(sQuestionType == null) return;
//                if(sQuestionType.equals("radio")){
//                    questionType = FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION;
//                } else if (sQuestionType.equals("checkbox")){
//                    questionType = FRAGMENT_TYPE_QUIZ_CHECKBOX;
//                } else if (sQuestionType.equals("textarea")){
//                    questionType = FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION;
//                }
//
//                if((mCurrentQuestion + 1) == mQuestionsCount) mActivityQuizView.setNextButtonText("Результаты");
//                getView().setCounterText("Вопрос " + (mCurrentQuestion + 1) + " из " + mQuestionsCount);
//                getView().attachFragment(questionType, mCurrentQuestion, animationType);
//                getView().disableNextButton();
//            }
//
//            @Override
//            public void onDataNotAvailable() {}
//        });
//    }
//
//    private void valuateResult(){
//        mQuizRepository.refreshSurvey();
//        mQuizRepository.getSurvey(mQuizId, new SurveyDataSource.LoadSurveyCallback() {
//            @Override
//            public void onSurveyLoaded(Survey survey) {
//                getView().attachFragment(FRAGMENT_TYPE_QUIZ_RESULT, mCurrentQuestion, FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT);
//            }
//
//            @Override
//            public void onDataNotAvailable() {}
//        });
//    }
//
//    private void deselectAll() {
//        int elementsCount = 33;
//        for(int i = 0; i < elementsCount; i++){
//            getRadioButtonQuestionView().deselectQuestion(i);
//            getRadioButtonQuestionView().focusTextInput(false, i);
//        }
//    }
//
//    // logging
//
//    @Override
//    public void logOpen(String type, int surveyId) {
//        mLogger.onOpenSurvey(type, surveyId);
//    }
//
//    @Override
//    public void logFinish(String type, int surveyId) {
//        mLogger.onFinishSurvey(type, surveyId);
//    }
//
//    @Override
//    public void logExit(String type, int surveyId) {
//        mLogger.onExitSurvey(type, surveyId);
//    }
//
//    @Override
//    public void logShowQuestion(String type, int surveyId, int questionId) {
//        mLogger.onShowSurveyQuestion(type, surveyId, questionId);
//    }
//
//    @Override
//    public void logResultScreen(String type, int surveyId) {
//        mLogger.onShowSurveyResult(type, surveyId);
//    }
//}