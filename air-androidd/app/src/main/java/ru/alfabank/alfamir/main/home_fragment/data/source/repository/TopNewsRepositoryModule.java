package ru.alfabank.alfamir.main.home_fragment.data.source.repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.main.home_fragment.data.source.remote.TopNewsRemoteDataSource;

@Module
abstract public class TopNewsRepositoryModule {
    @Singleton
    @Binds
    @Remote
    abstract TopNewsDataSource provideHomeDataSource(TopNewsRemoteDataSource dataSource);
}