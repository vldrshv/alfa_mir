package ru.alfabank.alfamir.survey.data.source.repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.survey.data.source.remote.SurveyRemoteDataSource;
import ru.alfabank.alfamir.di.qualifiers.Remote;

/**
 * Created by U_M0WY5 on 24.04.2018.
 */

@Module
abstract public class SurveyRepositoryModule {

    @Singleton
    @Binds
    @Remote
    abstract SurveyDataSource provideSurveyRemoteDataSource(SurveyRemoteDataSource dataSource);

}