package ru.alfabank.alfamir.data.source.repositories.favorite_pages;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.data.source.remote.data_source.FavoritePagesRemoteDataSource;
import ru.alfabank.alfamir.di.qualifiers.Remote;

@Module
abstract public class FavoritePagesRepositoryModule {

    @Singleton
    @Binds
    @Remote
    abstract FavoritePagesDataSource provideFavouritesRemoteDataSource(FavoritePagesRemoteDataSource dataSource);

}