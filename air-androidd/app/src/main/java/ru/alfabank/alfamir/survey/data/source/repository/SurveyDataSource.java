package ru.alfabank.alfamir.survey.data.source.repository;

import java.util.List;

import ru.alfabank.alfamir.survey.data.dto.Answer;
import ru.alfabank.alfamir.survey.data.dto.Survey;
import ru.alfabank.alfamir.survey.data.dto.SurveyCover;

/**
 * Created by U_M0WY5 on 24.04.2018.
 */

public interface SurveyDataSource {

    interface LoadSurveyCallback {
        void onSurveyLoaded(Survey survey);
        void onDataNotAvailable();
    }

    interface UploadAnswersCallback {
        void onAnswersUploaded();
        void onDataNotAvailable();
    }

    interface UploadAnswerCallback {
        void onAnswerUploaded();
        void onDataNotAvailable();
    }

    interface LoadNewsInjectionSurveyCallback {
        void onNewsInjectionSurveyLoaded(SurveyCover surveyCover);
        void onDataNotAvailable();
    }

    interface HideSurveyCallback {
        void onSurveyHidden();
        void onServerNotAvailable();
    }

    void hideSurvey(String surveyId, HideSurveyCallback callback);

    void getSurvey(String surveyId, LoadSurveyCallback callback);

    void getNewsInjectionSurvey(LoadNewsInjectionSurveyCallback callback);

    void uploadAnswers(String surveyId, List<Answer> answers, UploadAnswersCallback callback);

    void uploadAnswer(String surveyId, Answer answer, UploadAnswerCallback callback);

    void refreshSurvey();

}
