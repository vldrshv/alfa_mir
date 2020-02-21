package ru.alfabank.alfamir.survey.presentation


import com.google.common.base.Strings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.Constants.Companion.FRAGMENT_TRANSITION_ADD
import ru.alfabank.alfamir.Constants.Companion.FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT
import ru.alfabank.alfamir.Constants.Companion.FRAGMENT_TYPE_QUIZ_CHECKBOX
import ru.alfabank.alfamir.Constants.Companion.FRAGMENT_TYPE_QUIZ_CONTINUE
import ru.alfabank.alfamir.Constants.Companion.FRAGMENT_TYPE_QUIZ_INTRO
import ru.alfabank.alfamir.Constants.Companion.FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION
import ru.alfabank.alfamir.Constants.Companion.FRAGMENT_TYPE_QUIZ_RESULT
import ru.alfabank.alfamir.Constants.Companion.FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION
import ru.alfabank.alfamir.Constants.Companion.INTENT_SOURCE_CARD
import ru.alfabank.alfamir.Constants.Companion.INTENT_SOURCE_DEFAULT
import ru.alfabank.alfamir.Constants.Companion.INTENT_SOURCE_NOTIFICATION
import ru.alfabank.alfamir.Constants.Companion.INTENT_SOURCE_PUSH
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_PHYSICAL
import ru.alfabank.alfamir.Constants.Survey.CHECKBOX_QUESTION
import ru.alfabank.alfamir.Constants.Survey.RADIO_BUTTON_QUESTION
import ru.alfabank.alfamir.Constants.Survey.TEXT_AREA
import ru.alfabank.alfamir.di.qualifiers.ID
import ru.alfabank.alfamir.di.qualifiers.IntentSource
import ru.alfabank.alfamir.image.data.source.repository.ImageRepository
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.survey.data.dto.Answer
import ru.alfabank.alfamir.survey.data.dto.Survey
import ru.alfabank.alfamir.survey.data.source.repository.SurveyDataSource
import ru.alfabank.alfamir.survey.data.source.repository.SurveyRepository
import ru.alfabank.alfamir.survey.presentation.contract.SurveyContract
import ru.alfabank.alfamir.survey.presentation.dummy_view.SurveyViewDummy
import ru.alfabank.alfamir.utility.logging.remote.Logger
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract
import ru.alfabank.alfamir.utility.static_utilities.LinkHandler
import ru.alfabank.alfamir.utility.update_notifier.UpdateNotifier
import ru.alfabank.alfamir.utility.update_notifier.notifications.SurveyUpdate
import java.util.*
import javax.inject.Inject

class SurveyPresenter @Inject
internal constructor(@param:ID private val mQuizId: String,
                     @param:IntentSource private val mIntentSource: Int,
                     private val mGetImage: GetImage,
                     private val mQuizRepository: SurveyRepository,
                     private val mImageRepository: ImageRepository,
                     private val mUpdateNotifier: UpdateNotifier,
                     logger: Logger) : SurveyContract.Presenter, LoggerContract.Client.Survey {

    private var mActivityQuizView: SurveyContract.ActivityView? = null

    private var mFragmentRadioButtonQuestionView: SurveyContract.RadioButtonQuestionFragment? = null
    private var mCheckboxQuestionFragment: SurveyContract.CheckboxQuestionFragment? = null
    private var mFragmentResultView: SurveyContract.ResultFragment? = null
    private var mIntroFragment: SurveyContract.IntroFragment? = null
    private var mContinueFragment: SurveyContract.ContinueFragment? = null
    private var mTextAreaQuestionFragment: SurveyContract.TextAreaQuestionFragment? = null

    private var mQuestionsCount = -1
    private var mCurrentQuestion: Int = 0
    private var mCurrentAnswerPosition: Int = 0
    private var mCurrentFragmentType: Int = 0
    private var mCheckBoxAnswers: IntArray? = null
    private var mIsDataLoaded: Boolean = false

    private val mLogger: LoggerContract.Provider
    private val mCompositeDisposable = CompositeDisposable()

    init {
        mLogger = logger
    }

    /**
     * view Related events
     */

    override fun takeView(view: SurveyContract.ActivityView) {
        mActivityQuizView = view
        if (!mIsDataLoaded) loadQuiz()
    }

    override fun dropView() {
        mActivityQuizView = null
    }

    override fun getView(): SurveyContract.ActivityView {
        return if (mActivityQuizView == null) SurveyViewDummy() else mActivityQuizView!!
    }

    override fun takeRadioButtonQuestionFragmentView(view: SurveyContract.RadioButtonQuestionFragment, questionPosition: Int) {
        if (!mIsDataLoaded) return  // in case fragment was recreated after app was destroyed, but presenter have nothing yet
        mFragmentRadioButtonQuestionView = view
        mCurrentFragmentType = FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION
        showRadioButtonQuestion(mCurrentQuestion)
    }

    override fun takeCheckboxQuestionFragmentView(view: SurveyContract.CheckboxQuestionFragment, questionPosition: Int) {
        if (!mIsDataLoaded) return  // in case fragment was recreated after app was destroyed, but presenter have nothing yet
        mCheckboxQuestionFragment = view
        mCurrentFragmentType = FRAGMENT_TYPE_QUIZ_CHECKBOX
        showCheckboxQuestion(mCurrentQuestion)
    }

    override fun takeTextAreaFragment(view: SurveyContract.TextAreaQuestionFragment, questionPosition: Int) {
        if (!mIsDataLoaded) return  // in case fragment was recreated after app was destroyed, but presenter have nothing yet
        mTextAreaQuestionFragment = view
        mCurrentFragmentType = FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION
        showTextAreaQuestion(mCurrentQuestion)
        // TODO
    }

    override fun takeResultFragmentView(view: SurveyContract.ResultFragment) {
        if (!mIsDataLoaded) return  // in case fragment was recreated after app was destroyed, but presenter have nothing yet
        mFragmentResultView = view
        mCurrentFragmentType = FRAGMENT_TYPE_QUIZ_RESULT
        showResult()
    }

    override fun takeIntroFragmentView(view: SurveyContract.IntroFragment) {
        if (!mIsDataLoaded) return  // in case fragment was recreated after app was destroyed, but presenter have nothing yet
        mIntroFragment = view
        mCurrentFragmentType = FRAGMENT_TYPE_QUIZ_INTRO
        showIntro()
    }

    override fun takeContinueFragment(view: SurveyContract.ContinueFragment) {
        if (!mIsDataLoaded) return  // in case fragment was recreated after app was destroyed, but presenter have nothing yet
        mContinueFragment = view
        mCurrentFragmentType = FRAGMENT_TYPE_QUIZ_CONTINUE
        showContinue()
    }

    /**
     * onClick events
     */

    override fun onRadioButtonAnswerClicked(answerPosition: Int, fragmentType: Int) {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                when (survey.result.viewFlags.showResultAfterAnswer) {
                    0 -> {
                        val questions = survey.questions.asList()
                        val answers = questions[mCurrentQuestion].answers.asList()
                        val hasTextInput = answers[answerPosition].body.hastTextInput()

                        deselectAll()
                        radioButtonQuestionView.selectQuestion(answerPosition)

                        mCurrentAnswerPosition = answerPosition

                        if (hasTextInput) {
                            radioButtonQuestionView.focusTextInput(true, answerPosition)
                            val userInput = radioButtonQuestionView.getTextInput(answerPosition)
                            if (Strings.isNullOrEmpty(userInput)) {
                                view.disableNextButton()
                            }
                        } else {
                            view.enableNextButton()
                        }
                    }
                    1 -> {
                        deselectAll()

                        mCurrentAnswerPosition = answerPosition
                        radioButtonQuestionView.disableOnTouchListeners()
                        validateAnswer()
                    }
                }
            }

            override fun onDataNotAvailable() {}
        })
    }

    override fun onCheckboxAnswerClicked(answerPosition: Int, fragmentType: Int) {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                val questions = survey.questions.asList()
                val answers = questions[mCurrentQuestion].answers.asList()
                val hasTextInput = answers[answerPosition].body.hastTextInput()

                when (mCheckBoxAnswers!![answerPosition]) {
                    UNCHECKED -> {
                        checkBoxQuestionView.clearFocus()
                        checkBoxQuestionView.selectQuestion(answerPosition)
                        if (hasTextInput) checkBoxQuestionView.focusTextInput(true, answerPosition)
                    }
                    CHECKED -> {
                        checkBoxQuestionView.clearFocus()
                        checkBoxQuestionView.unselectQuestion(answerPosition)
                        if (hasTextInput) checkBoxQuestionView.focusTextInput(false, answerPosition)
                    }
                }

                if (mCheckBoxAnswers!![answerPosition] == UNCHECKED) {
                    mCheckBoxAnswers!![answerPosition] = CHECKED
                } else if (mCheckBoxAnswers!![answerPosition] == CHECKED) {
                    mCheckBoxAnswers!![answerPosition] = UNCHECKED
                }

                checkButtonNextStatus(answers)
            }

            override fun onDataNotAvailable() {}
        })

    }

    override fun onEditTextClicked(answerPosition: Int, fragmentType: Int) {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                val questions = survey.questions.asList()
                val answers = questions[mCurrentQuestion].answers.asList()
                val hasTextInput = answers[answerPosition].body.hastTextInput()

                when (fragmentType) {
                    RADIO_BUTTON_QUESTION -> {
                        deselectAll()
                        radioButtonQuestionView.selectQuestion(answerPosition)
                        radioButtonQuestionView.focusTextInput(true, answerPosition)

                        if (hasTextInput) {
                            radioButtonQuestionView.focusTextInput(true, answerPosition)
                            val userInput = checkBoxQuestionView.getTextInput(answerPosition)
                            if (Strings.isNullOrEmpty(userInput)) {
                                view.disableNextButton()
                            }
                        }
                    }
                    CHECKBOX_QUESTION -> {
                        checkBoxQuestionView.clearFocus()
                        checkBoxQuestionView.selectQuestion(answerPosition)
                        checkBoxQuestionView.focusTextInput(true, answerPosition)

                        if (mCheckBoxAnswers!![answerPosition] == UNCHECKED) {
                            mCheckBoxAnswers!![answerPosition] = CHECKED
                        }
                        checkButtonNextStatus(answers)
                    }
                }
            }

            override fun onDataNotAvailable() {}
        })
    }

    override fun onStartButtonClicked() {
        view.showBottomBar()
        view.showAppBar()

        val animationType = FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT
        setUpNextQuestion(animationType)
    }

    override fun onContinueButtonClicked() {
        view.showBottomBar()
        view.showAppBar()

        val animationType = FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT
        setUpNextQuestion(animationType)
    }

    override fun onNextButtonClicked() {

        view.disableNextButton()
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {


                val sQuestionType = survey.questions[mCurrentQuestion].body.type
                val showResultAfterAnswer = survey.result.viewFlags.showResultAfterAnswer

                if (showResultAfterAnswer == 0 || sQuestionType == "textarea") { // if we have a textarea question, no need in showing the validation info
                    saveAnswer()
                } else {
                    if (mCurrentQuestion + 1 < mQuestionsCount) {
                        mCurrentQuestion++
                        val animationType = FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT
                        setUpNextQuestion(animationType)
                    } else {
                        valuateResult()
                    }
                }
            }

            override fun onDataNotAvailable() {
                view.enableNextButton()
                view.showSnackBar("Что-то пошло не так")
            }
        })
    }

    override fun onDisabledNextButtonClicked(fragmentType: Int) {
        var text = ""
        when (fragmentType) {
            CHECKBOX_QUESTION -> text = "Заполните текстовое поле или снимите выделение"
            RADIO_BUTTON_QUESTION -> text = "Заполните текстовое поле или выберите другой вариант"
            TEXT_AREA -> text = "Заполните текстовое поле"
        }
        view.showSnackBar(text)
    }

    override fun onDoneClicked() {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                logFinish(survey.type, survey.id)
            }

            override fun onDataNotAvailable() {}
        })

        view.close()
    }

    override fun onCloseClicked() {
        view.showWarningOnExitUi()
    }

    override fun onBackClicked() {
        view.close()
    }

    override fun onExitWarningAccepted() {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                logExit(survey.type, survey.id)
            }

            override fun onDataNotAvailable() {}
        })
        mQuizRepository.refreshSurvey()
        view.close()
    }

    override fun onEmptyEditTextView(fragmentType: Int) {
        setDisabledButtonListener(fragmentType)
    }

    override fun onBackPressed() {
        //        mActivityQuizView.close();
        if (mCurrentFragmentType == FRAGMENT_TYPE_QUIZ_INTRO ||
                mCurrentFragmentType == FRAGMENT_TYPE_QUIZ_CONTINUE ||
                mCurrentFragmentType == FRAGMENT_TYPE_QUIZ_RESULT) {
            view.close()
        } else {
            view.showWarningOnExitUi()
        }
    }

    override fun onTextChanged(text: String, fragmentType: Int) {
        when (fragmentType) {
            CHECKBOX_QUESTION -> mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
                override fun onSurveyLoaded(survey: Survey) {
                    val questions = survey.questions.asList()
                    val answers = questions[mCurrentQuestion].answers.asList()
                    checkButtonNextStatus(answers)
                }

                override fun onDataNotAvailable() {}
            })
            RADIO_BUTTON_QUESTION, TEXT_AREA -> if (Strings.isNullOrEmpty(text)) {
                view.disableNextButton()
            } else {
                view.enableNextButton()
            }
        }
    }

    override fun getResultView(): SurveyContract.ResultFragment {
        return if (mFragmentResultView == null) {
            SurveyViewDummy()
        } else mFragmentResultView!!
    }

    override fun getIntroView(): SurveyContract.IntroFragment {
        return if (mIntroFragment == null) {
            SurveyViewDummy()
        } else mIntroFragment!!
    }

    override fun getContinueView(): SurveyContract.ContinueFragment {
        return if (mContinueFragment == null) {
            SurveyViewDummy()
        } else mContinueFragment!!
    }

    override fun getRadioButtonQuestionView(): SurveyContract.RadioButtonQuestionFragment {
        return if (mFragmentRadioButtonQuestionView == null) {
            SurveyViewDummy()
        } else mFragmentRadioButtonQuestionView!!
    }

    override fun getCheckBoxQuestionView(): SurveyContract.CheckboxQuestionFragment {
        return if (mCheckboxQuestionFragment == null) {
            SurveyViewDummy()
        } else mCheckboxQuestionFragment!!
    }

    override fun getTextAreaQuestionView(): SurveyContract.TextAreaQuestionFragment {
        return if (mTextAreaQuestionFragment == null) {
            SurveyViewDummy()
        } else mTextAreaQuestionFragment!!
    }

    // private methods

    private fun checkButtonNextStatus(answers: List<Survey.Question.Answer>) {
        var isEnabled = false
        for (i in mCheckBoxAnswers!!.indices) {
            if (mCheckBoxAnswers!![i] == 1) {
                val hasTextInput = answers[i].body.hastTextInput()
                if (hasTextInput) {
                    val userInput = checkBoxQuestionView.getTextInput(i)
                    if (Strings.isNullOrEmpty(userInput)) {
                        isEnabled = false
                        break
                    }
                }
                isEnabled = true
            }
        }

        if (isEnabled) {
            view.enableNextButton()
        } else {
            view.disableNextButton()
        }
    }

    private fun setDisabledButtonListener(fragmentType: Int) {
        view.setDisabledNextButtonWarning(fragmentType)
    }

    private fun loadQuiz() {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                val animationType = FRAGMENT_TRANSITION_ADD
                val userAnsweredQuestionCount = survey.cover.respondentAnswersCount
                mQuestionsCount = survey.questions.size
                mCurrentQuestion = userAnsweredQuestionCount

                val title = survey.cover.surveyName
                view.setTitle(title)

                if (userAnsweredQuestionCount == 0) { // no answers
                    val showIntroScreen = survey.result.viewFlags.introScreen // check for intro screen
                    if (showIntroScreen == 0) {
                        // show questions
                        view.showBottomBar()
                        view.showAppBar()
                        setUpNextQuestion(animationType)
                    } else if (showIntroScreen == 1) {
                        // show intro screen
                        view.attachFragment(FRAGMENT_TYPE_QUIZ_INTRO, mCurrentQuestion, animationType)
                    }
                } else { // there are answers
                    if (userAnsweredQuestionCount == mQuestionsCount) { // there are all the answers
                        view.attachFragment(FRAGMENT_TYPE_QUIZ_RESULT, mCurrentQuestion, animationType)
                    } else { // there are some answers
                        when (mIntentSource) {
                            INTENT_SOURCE_PUSH, INTENT_SOURCE_NOTIFICATION -> {
                                // show continue screen
                                view.attachFragment(FRAGMENT_TYPE_QUIZ_CONTINUE, mCurrentQuestion, animationType)
                            }
                            INTENT_SOURCE_CARD, INTENT_SOURCE_DEFAULT -> {
                                // show question
                                view.showBottomBar()
                                view.showAppBar()

                                setUpNextQuestion(animationType)
                            }
                        }
                    }
                }

                logOpen(survey.type, survey.id)
                mIsDataLoaded = true
            }

            override fun onDataNotAvailable() {
                view.showSnackBar("Что-то пошло не так")
            }
        })
    }

    private fun loadImage(fragmentType: Int, imageUrl: String) {

        CoroutineScope(Dispatchers.Main).launch {
            mCompositeDisposable.add(mGetImage.bitmap(imageUrl, SCREEN_WIDTH_PHYSICAL)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ bitmap ->
                        when (fragmentType) {
                            FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION -> radioButtonQuestionView.showImage(bitmap, true)
                            FRAGMENT_TYPE_QUIZ_CHECKBOX -> checkBoxQuestionView.showImage(bitmap, true)
                            FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION -> textAreaQuestionView.showImage(bitmap, true)
                        }
                    }, Throwable::printStackTrace))
        }
    }

    private fun loadBackgroundImage(imageUrlWithSize: String, screenType: Int) {

        CoroutineScope(Dispatchers.Main).launch {
            mCompositeDisposable.add(mGetImage.bitmap(imageUrlWithSize, SCREEN_WIDTH_PHYSICAL)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ bitmap ->
                        when (screenType) {
                            FRAGMENT_TYPE_QUIZ_INTRO -> introView.showImage(bitmap, true)
                            FRAGMENT_TYPE_QUIZ_RESULT -> resultView.showImage(bitmap, true)
                            FRAGMENT_TYPE_QUIZ_CONTINUE -> continueView.showImage(bitmap, true)
                        }
                    }, Throwable::printStackTrace))
        }
    }

    private fun showRadioButtonQuestion(currentQuestionNumber: Int) {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {

                val questions = survey.questions.asList()
                val currentQuestion = questions[currentQuestionNumber]

                logShowQuestion(survey.type, survey.id, currentQuestion.id)

                val imageUrlDirty = currentQuestion.body.imgUrl
                val questionText = currentQuestion.body.text

                val answers = questions[currentQuestionNumber].answers.asList()

                radioButtonQuestionView.showTitle(questionText)
                for (i in answers.indices) {
                    val answer = answers[i].body.text
                    val hasTextInput = answers[i].body.hastTextInput()
                    showRadioButtonAnswer(answer, i, hasTextInput)
                }

                if (Strings.isNullOrEmpty(imageUrlDirty)) {
                    return
                }

                val imageHeight = LinkHandler.getPhotoPixelHeightMatchingScreenWidth(imageUrlDirty)
                val imageUrl = LinkHandler.getPhotoLink(imageUrlDirty)
                radioButtonQuestionView.setImageHeight(imageHeight)
                loadImage(FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION, imageUrl)
            }

            override fun onDataNotAvailable() {}
        })
    }

    private fun showCheckboxQuestion(questionNumber: Int) {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {

                val questions = survey.questions.asList()
                val currentQuestion = questions[questionNumber]

                logShowQuestion(survey.type, survey.id, currentQuestion.id)

                val imageUrlDirty = currentQuestion.body.imgUrl
                val questionText = currentQuestion.body.text

                val answers = questions[questionNumber].answers.asList()

                mCheckBoxAnswers = IntArray(answers.size)

                checkBoxQuestionView.showTitle(questionText)
                for (i in answers.indices) {
                    if (i > 33) break // a bit hacky but oh well

                    val answer = answers[i].body.text
                    val hasTextInput = answers[i].body.hastTextInput()
                    showCheckboxAnswer(answer, i, hasTextInput)
                }

                if (Strings.isNullOrEmpty(imageUrlDirty)) {
                    return
                }

                val imageHeight = LinkHandler.getPhotoPixelHeightMatchingScreenWidth(imageUrlDirty)
                val imageUrl = LinkHandler.getPhotoLink(imageUrlDirty)
                checkBoxQuestionView.setImageHeight(imageHeight)
                loadImage(FRAGMENT_TYPE_QUIZ_CHECKBOX, imageUrl)
            }

            override fun onDataNotAvailable() {}
        })
    }

    private fun showTextAreaQuestion(questionNumber: Int) {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {

                val questions = survey.questions.asList()
                val currentQuestion = questions[questionNumber]

                logShowQuestion(survey.type, survey.id, currentQuestion.id)

                val imageUrlDirty = currentQuestion.body.imgUrl
                val questionText = currentQuestion.body.text

                textAreaQuestionView.showTitle(questionText)
                textAreaQuestionView.setUpETListener()
                setDisabledButtonListener(TEXT_AREA)

                if (Strings.isNullOrEmpty(imageUrlDirty)) {
                    return
                }

                val imageHeight = LinkHandler.getPhotoPixelHeightMatchingScreenWidth(imageUrlDirty)
                val imageUrl = LinkHandler.getPhotoLink(imageUrlDirty)
                radioButtonQuestionView.setImageHeight(imageHeight)
                loadImage(FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION, imageUrl)
            }

            override fun onDataNotAvailable() {}
        })
    }

    private fun showCheckboxAnswer(answer: String, position: Int, hasTextInput: Boolean) {
        checkBoxQuestionView.showQuestion(answer, position)
        if (hasTextInput) checkBoxQuestionView.showTextInput(position)
    }

    private fun showRadioButtonAnswer(answer: String, position: Int, hasTextInput: Boolean) {
        radioButtonQuestionView.showQuestion(answer, position)
        if (hasTextInput) radioButtonQuestionView.showTextInput(position)
    }

    private fun showContinue() {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                val name = survey.cover.surveyName
                val date = survey.cover.endDate
                val title = survey.cover.title
                val description = survey.cover.description

                val total = survey.cover.requredAnswersCount
                val current = survey.cover.respondentAnswersCount
                val percent = (current * 100.0f / total).toInt()

                val imageUrlWithSize = survey.cover.imgUrl
                loadBackgroundImage(imageUrlWithSize, FRAGMENT_TYPE_QUIZ_CONTINUE)

                continueView.showDate("$name до $date")
                continueView.showTitle(title)
                continueView.setBarPercentage(percent)
                continueView.showCompletionStatus("Завершено на $percent%")

                if (Strings.isNullOrEmpty(description)) {
                    continueView.hideDescription()
                } else {
                    continueView.showDescription(description)
                }

            }

            override fun onDataNotAvailable() {

            }
        })
    }

    private fun showIntro() {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                val name = survey.cover.surveyName
                val date = survey.cover.endDate
                val title = survey.cover.title
                val description = survey.cover.description

                val imageUrlWithSize = survey.cover.imgUrl
                loadBackgroundImage(imageUrlWithSize, FRAGMENT_TYPE_QUIZ_INTRO)

                introView.showDate("$name до $date")
                introView.showTitle(title)

                if (Strings.isNullOrEmpty(description)) {
                    introView.hideDescription()
                } else {
                    introView.showDescription(description)
                }

                introView.showIntroText()

            }

            override fun onDataNotAvailable() {

            }
        })
    }

    private fun showResult() {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                logResultScreen(survey.type, survey.id)

                val imageUrlWithSize = survey.cover.imgUrl
                loadBackgroundImage(imageUrlWithSize, FRAGMENT_TYPE_QUIZ_RESULT)

                val message = survey.result.message

                val showResult = survey.result.viewFlags.quizResult
                if (showResult == 0) {
                    resultView.hideResult()
                } else if (showResult == 1) {
                    val result = survey.result.quizResult
                    resultView.showResult(result)
                }

                resultView.showComment(message)


                view.hideBottomBar()
                view.hideAppBar()
            }

            override fun onDataNotAvailable() {}
        })
    }

    private fun saveAnswer() {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                val sQuestionType = survey.questions[mCurrentQuestion].body.type ?: return

                if (sQuestionType == "radio") {
                    val question = survey.questions[mCurrentQuestion]
                    val questionAnswer = question.answers[mCurrentAnswerPosition]

                    val questionId = question.id
                    val answerId = questionAnswer.id

                    val answer = Answer()
                    answer.answerId = answerId
                    answer.questionId = questionId

                    val hasTextInput = questionAnswer.body.hastTextInput()
                    if (hasTextInput) {
                        val userInput = radioButtonQuestionView.getTextInput(mCurrentAnswerPosition)
                        answer.text = userInput
                    }

                    saveAnswerRemotely(answer)

                } else if (sQuestionType == "checkbox") {
                    val question = survey.questions[mCurrentQuestion]

                    val answers = ArrayList<Answer>()

                    for (i in mCheckBoxAnswers!!.indices) {

                        if (mCheckBoxAnswers!![i] == 0) {

                        } else {
                            val questionAnswer = question.answers[i]

                            val questionId = question.id
                            val answerId = questionAnswer.id

                            val answer = Answer()
                            answer.answerId = answerId
                            answer.questionId = questionId

                            val hasTextInput = questionAnswer.body.hastTextInput()
                            if (hasTextInput) {
                                val userInput = checkBoxQuestionView.getTextInput(i)
                                answer.text = userInput
                            }

                            answers.add(answer)
                        }

                    }

                    saveAnswersRemotely(answers)

                } else if (sQuestionType == "textarea") {
                    val question = survey.questions[mCurrentQuestion]
                    val questionId = question.id

                    val answer = Answer()
                    answer.questionId = questionId

                    val userInput = textAreaQuestionView.textInput
                    answer.text = userInput

                    saveAnswerRemotely(answer)
                }

            }

            override fun onDataNotAvailable() {
                view.showSnackBar("Что-то пошло не так") // TODO should give an option to redownload
            }
        })
    }

    private fun validateAnswer() {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                val question = survey.questions[mCurrentQuestion]
                val userAnswer = question.answers[mCurrentAnswerPosition]

                val questionId = question.id
                val answerId = userAnswer.id

                val answer = Answer()
                answer.answerId = answerId
                answer.questionId = questionId

                mQuizRepository.uploadAnswer(mQuizId, answer, object : SurveyDataSource.UploadAnswerCallback {
                    override fun onAnswerUploaded() {
                        mUpdateNotifier.reportUpdate(SurveyUpdate())
                        // TODO show right answer
                        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
                            override fun onSurveyLoaded(survey: Survey) {

                                val isCorrect = userAnswer.body.correctAnswer
                                if (isCorrect == 1) {
                                    val position = mCurrentAnswerPosition
                                    val correct = true
                                    val comment = userAnswer.body.commentOnAnswer

                                    radioButtonQuestionView.showAnswerStatus(correct, position)
                                    if (!Strings.isNullOrEmpty(comment)) {
                                        radioButtonQuestionView.showAnswerComment(correct, comment, position)
                                    }
                                } else {
                                    val questionAnswers = question.answers.asList()

                                    var answer: Survey.Question.Answer
                                    for (i in questionAnswers.indices) {
                                        answer = questionAnswers[i]
                                        if (answer.body.correctAnswer == 1) {
                                            val correct = true

                                            radioButtonQuestionView.showAnswerStatus(correct, i)
                                        }
                                    }

                                    val position = mCurrentAnswerPosition
                                    val correct = false
                                    val comment = userAnswer.body.commentOnAnswer

                                    radioButtonQuestionView.showAnswerStatus(correct, position)
                                    if (!Strings.isNullOrEmpty(comment)) {
                                        radioButtonQuestionView.showAnswerComment(correct, comment, position)
                                    }
                                }

                                view.enableNextButton()
                            }

                            override fun onDataNotAvailable() {

                            }
                        })
                    }

                    override fun onDataNotAvailable() {
                        radioButtonQuestionView.selectQuestion(mCurrentAnswerPosition)
                        radioButtonQuestionView.enableOnTouchListeners()
                        view.showSnackBar("Что-то пошло не так")
                    }
                })
            }

            override fun onDataNotAvailable() {

            }
        })
    }

    private fun saveAnswerRemotely(answer: Answer) {
        mQuizRepository.uploadAnswer(mQuizId, answer, object : SurveyDataSource.UploadAnswerCallback {
            override fun onAnswerUploaded() {
                mUpdateNotifier.reportUpdate(SurveyUpdate())
                if (mCurrentQuestion + 1 < mQuestionsCount) {
                    mCurrentQuestion++
                    val animationType = FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT
                    setUpNextQuestion(animationType)
                } else if (mCurrentQuestion + 1 == mQuestionsCount) {
                    valuateResult()
                }
            }

            override fun onDataNotAvailable() {
                view.showSnackBar("Что-то пошло не так")
            }
        })
    }

    private fun saveAnswersRemotely(answers: List<Answer>) {
        mQuizRepository.uploadAnswers(mQuizId, answers, object : SurveyDataSource.UploadAnswersCallback {
            override fun onAnswersUploaded() {
                mUpdateNotifier.reportUpdate(SurveyUpdate())
                if (mCurrentQuestion + 1 < mQuestionsCount) {
                    mCurrentQuestion++
                    val animationType = FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT
                    setUpNextQuestion(animationType)
                } else if (mCurrentQuestion + 1 == mQuestionsCount) {
                    valuateResult()
                }
            }

            override fun onDataNotAvailable() {
                view.showSnackBar("Что-то пошло не так")
            }
        })
    }

    private fun setUpNextQuestion(animationType: Int) {
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                var questionType = 0
                val sQuestionType = survey.questions[mCurrentQuestion].body.type ?: return
                if (sQuestionType == "radio") {
                    questionType = FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION
                } else if (sQuestionType == "checkbox") {
                    questionType = FRAGMENT_TYPE_QUIZ_CHECKBOX
                } else if (sQuestionType == "textarea") {
                    questionType = FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION
                }

                if (mCurrentQuestion + 1 == mQuestionsCount) mActivityQuizView!!.setNextButtonText("Результаты")
                view.setCounterText("Вопрос " + (mCurrentQuestion + 1) + " из " + mQuestionsCount)
                view.attachFragment(questionType, mCurrentQuestion, animationType)
                view.disableNextButton()
            }

            override fun onDataNotAvailable() {}
        })
    }

    private fun valuateResult() {
        mQuizRepository.refreshSurvey()
        mQuizRepository.getSurvey(mQuizId, object : SurveyDataSource.LoadSurveyCallback {
            override fun onSurveyLoaded(survey: Survey) {
                view.attachFragment(FRAGMENT_TYPE_QUIZ_RESULT, mCurrentQuestion, FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT)
            }

            override fun onDataNotAvailable() {}
        })
    }

    private fun deselectAll() {
        val elementsCount = 33
        for (i in 0 until elementsCount) {
            radioButtonQuestionView.deselectQuestion(i)
            radioButtonQuestionView.focusTextInput(false, i)
        }
    }

    // logging

    override fun logOpen(type: String, surveyId: Int) {
        mLogger.onOpenSurvey(type, surveyId)
    }

    override fun logFinish(type: String, surveyId: Int) {
        mLogger.onFinishSurvey(type, surveyId)
    }

    override fun logExit(type: String, surveyId: Int) {
        mLogger.onExitSurvey(type, surveyId)
    }

    override fun logShowQuestion(type: String, surveyId: Int, questionId: Int) {
        mLogger.onShowSurveyQuestion(type, surveyId, questionId)
    }

    override fun logResultScreen(type: String, surveyId: Int) {
        mLogger.onShowSurveyResult(type, surveyId)
    }

    companion object {

        private val UNCHECKED = 0
        private val CHECKED = 1
    }
}