package ru.alfabank.alfamir.feed_new.data.source.repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.feed_new.data.source.remote.PostRemoteDataSource;

@Module
abstract public class PostRepositoryModule {
    @Singleton
    @Binds
    @Remote
    abstract PostDataSource provideProfileRemoteDataSource(PostRemoteDataSource dataSource);
}