package ru.alfabank.alfamir.survey.data.source.repository;


import com.google.common.base.Strings;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.alfabank.alfamir.survey.data.dto.Answer;
import ru.alfabank.alfamir.survey.data.dto.Survey;
import ru.alfabank.alfamir.survey.data.dto.SurveyCover;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;

import static ru.alfabank.alfamir.Constants.Log.LOG_SURVEY;

@Singleton
public class SurveyRepository implements SurveyDataSource {

    private final SurveyDataSource mSurveyRemoteDataSource;
    private Map<Integer, Survey> mCachedSurveyList;
    private SurveyCover mCachedSurveyCover;
    private boolean mCacheIsDirty;
    private LogWrapper mLog;

    @Inject
    SurveyRepository(@Remote SurveyDataSource surveyRemoteDataSource,
                     LogWrapper logWrapper) {
        mSurveyRemoteDataSource = surveyRemoteDataSource;
        mLog = logWrapper;
    }

    @Override
    public void hideSurvey(String surveyId, HideSurveyCallback callback) {
        mSurveyRemoteDataSource.hideSurvey(surveyId, new HideSurveyCallback() {
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
        Survey cachedSurvey = getSurveyFromCache(surveyId);
        if(cachedSurvey != null && !mCacheIsDirty){

            mLog.debug(LOG_SURVEY, this.toString() + "getSurvey local");
            callback.onSurveyLoaded(cachedSurvey);
            return;
        }
        mLog.debug(LOG_SURVEY, this.toString() + "getSurvey remote");
        getSurveyFromRemoteDataSource(surveyId, callback);
    }

    public SurveyCover getCachedSurveyCover(){
        return mCachedSurveyCover;
    }

    public void dropCachedSurveyCover(){
        mCachedSurveyCover = null;
    }

    @Override
    public void getNewsInjectionSurvey(LoadNewsInjectionSurveyCallback callback) {
        mSurveyRemoteDataSource.getNewsInjectionSurvey(new LoadNewsInjectionSurveyCallback() {
            @Override
            public void onNewsInjectionSurveyLoaded(SurveyCover surveyCover) {
                if(surveyCover != null) mCachedSurveyCover = surveyCover;
                callback.onNewsInjectionSurveyLoaded(surveyCover);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }


    @Override
    public void uploadAnswers(String surveyId, List<Answer> answers, UploadAnswersCallback callback) {
        mSurveyRemoteDataSource.uploadAnswers(surveyId, answers, new UploadAnswersCallback() {
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
        mSurveyRemoteDataSource.uploadAnswer(surveyId, answer, new UploadAnswerCallback() {
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
        mCacheIsDirty = true;
    }

    private void getSurveyFromRemoteDataSource(String surveyId, LoadSurveyCallback callback){
        mSurveyRemoteDataSource.getSurvey(surveyId, new LoadSurveyCallback() {
            @Override
            public void onSurveyLoaded(Survey survey) {
                saveToCache(survey);
                callback.onSurveyLoaded(survey);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void saveToCache(Survey survey){
        if(mCachedSurveyList == null){
            mCachedSurveyList = new LinkedHashMap<>();
        }
        mCachedSurveyList.put(survey.getID(), survey);
        mCacheIsDirty = false;
    }

    private Survey getSurveyFromCache(String surveyId){
        if(Strings.isNullOrEmpty(surveyId)){
            return null;
        } else {
            if(mCachedSurveyList == null || mCachedSurveyList.isEmpty()){
                return null;
            } else {
                return mCachedSurveyList.get(Integer.parseInt(surveyId));
            }
        }
    }

}
