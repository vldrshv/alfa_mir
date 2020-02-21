package ru.alfabank.alfamir.search.data.source.repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.search.data.source.remote.SearchRemoteDataSource;

@Module
abstract public class SearchRepositoryModule {
    @Singleton
    @Binds
    @Remote
    abstract SearchDataSource provideSearchDataSource(SearchRemoteDataSource dataSource);
}