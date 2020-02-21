package ru.alfabank.alfamir.survey.presentation.dagger_module;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import ru.alfabank.alfamir.di.qualifiers.ID;
import ru.alfabank.alfamir.di.qualifiers.IntentSource;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.survey.presentation.SurveyActivity;
import ru.alfabank.alfamir.survey.presentation.SurveyPresenter;
import ru.alfabank.alfamir.survey.presentation.contract.SurveyContract;
import ru.alfabank.alfamir.survey.presentation.fragments.CheckboxQuestionFragment;
import ru.alfabank.alfamir.survey.presentation.fragments.ContinueFragment;
import ru.alfabank.alfamir.survey.presentation.fragments.IntroFragment;
import ru.alfabank.alfamir.survey.presentation.fragments.RadioButtonQuestionFragment;
import ru.alfabank.alfamir.survey.presentation.fragments.ResultFragment;
import ru.alfabank.alfamir.survey.presentation.fragments.TextAreaQuestionFragment;

import static ru.alfabank.alfamir.Constants.INTENT_SOURCE;
import static ru.alfabank.alfamir.Constants.INTENT_SOURCE_DEFAULT;
import static ru.alfabank.alfamir.Constants.QUIZ_ID;

/**
 * Created by U_M0WY5 on 24.04.2018.
 */

@Module
public abstract class SurveyPresenterModule {

//    static SurveyPresenter mPresenter;

    @Provides
    @ActivityScoped
    @ID
    static String provideQuizId(SurveyActivity activity) {
        return activity.getIntent().getStringExtra(QUIZ_ID);
    }

    @Provides
    @ActivityScoped
    @IntentSource
    static int provideIntentSource(SurveyActivity activity) {
        return activity.getIntent().getIntExtra(INTENT_SOURCE, INTENT_SOURCE_DEFAULT);
    }

    @ActivityScoped
    @Binds
    abstract SurveyContract.Presenter quizPresenter(SurveyPresenter presenter); // old n working

    @FragmentScoped
    @ContributesAndroidInjector
    abstract RadioButtonQuestionFragment radioButtonQuestionFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract CheckboxQuestionFragment checkboxQuestionFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract TextAreaQuestionFragment textAreaQuestionFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ResultFragment resultFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract IntroFragment introFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ContinueFragment continueFragment();

}
