package ru.alfabank.alfamir.alfa_tv.data.source.repository.show;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.alfa_tv.data.source.remote.ShowRemoteDataSource;
import ru.alfabank.alfamir.di.qualifiers.Remote;

@Module
abstract public class ShowRepositoryModule {
    @Singleton
    @Binds
    @Remote
    abstract ShowDataSource provideProfileRemoteDataSource(ShowRemoteDataSource dataSource);
}