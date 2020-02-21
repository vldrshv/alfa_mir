package ru.alfabank.alfamir.feed.data.source.repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.feed.data.source.remote.PostRemoteDataSource;
import ru.alfabank.alfamir.di.qualifiers.Remote;

@Module
abstract public class PostRepositoryModule {
    @Singleton
    @Binds
    @Remote
    abstract PostDataSource provideProfileRemoteDataSource(PostRemoteDataSource dataSource);
}
