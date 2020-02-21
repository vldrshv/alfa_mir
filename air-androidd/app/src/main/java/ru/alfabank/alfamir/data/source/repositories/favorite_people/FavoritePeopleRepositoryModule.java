package ru.alfabank.alfamir.data.source.repositories.favorite_people;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.data.source.remote.data_source.FavoritePeopleRemoteDataSource;
import ru.alfabank.alfamir.di.qualifiers.Remote;

/**
 * Created by mshvdvsk on 29/03/2018.
 */

@Module
abstract public class FavoritePeopleRepositoryModule {

    @Singleton
    @Binds
    @Remote
    abstract FavoritePeopleDataSource provideFavouritesRemoteDataSource(FavoritePeopleRemoteDataSource dataSource);

}
