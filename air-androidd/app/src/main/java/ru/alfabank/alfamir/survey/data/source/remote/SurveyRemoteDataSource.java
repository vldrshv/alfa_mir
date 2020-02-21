package ru.alfabank.alfamir.survey.data.source.remote;

import java.util.List;

import javax.inject.Inject;

import ru.alfabank.alfamir.survey.data.dto.Answer;
import ru.alfabank.alfamir.survey.data.dto.Survey;
import ru.alfabank.alfamir.survey.data.dto.SurveyCover;
import ru.alfabank.alfamir.survey.data.source.repository.SurveyDataSource;
import ru.alfabank.alfamir.data.source.remote.api.WebService;

/**
 * Created by U_M0WY5 on 24.04.2018.
 */

public class SurveyRemoteDataSource implements SurveyDataSource {

    private WebService service;

    @Inject
    SurveyRemoteDataSource(WebService service){
        this.service = service;
    }

    @Override
    public void hideSurvey(String surveyId, HideSurveyCallback callback) {
        service.hideSurvey(surveyId, new HideSurveyCallback() {
            @Override
            public void onSurveyHidden() {
                callback.onSurveyHidden();
            }

            @Override
            public void onServerNotAvailable() {
                callback.onServerNotAvailable();
            }
        });
    }

    @Override
    public void getSurvey(String surveyId, LoadSurveyCallback callback) {
        service.getSurvey(surveyId, new LoadSurveyCallback() {
            @Override
            public void onSurveyLoaded(Survey survey) {
                callback.onSurveyLoaded(survey);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getNewsInjectionSurvey(LoadNewsInjectionSurveyCallback callback) {
        service.getNewsInjectionSurvey(new LoadNewsInjectionSurveyCallback() {
            @Override
            public void onNewsInjectionSurveyLoaded(SurveyCover surveyCover) {
                callback.onNewsInjectionSurveyLoaded(surveyCover);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void uploadAnswers(String surveyId, List <Answer> answers, UploadAnswersCallback callback) {
        service.uploadAnswers(surveyId, answers, new UploadAnswersCallback() {
            @Override
            public void onAnswersUploaded() {
                callback.onAnswersUploaded();
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void uploadAnswer(String surveyId, Answer answer, UploadAnswerCallback callback) {
        service.uploadAnswer(surveyId, answer, new UploadAnswerCallback() {
            @Override
            public void onAnswerUploaded() {
                callback.onAnswerUploaded();
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void refreshSurvey() {

    }
}