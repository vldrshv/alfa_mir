package ru.alfabank.alfamir.favorites.data.source.repository;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.favorites.data.source.remote.FavoriteRemoteDataSource;

import javax.inject.Singleton;

@Module
abstract public class FavoriteRepositoryModule {
    @Singleton
    @Binds
    @Remote
    abstract FavoriteDataSource FavoriteRemoteDataSource(FavoriteRemoteDataSource dataSource);
}