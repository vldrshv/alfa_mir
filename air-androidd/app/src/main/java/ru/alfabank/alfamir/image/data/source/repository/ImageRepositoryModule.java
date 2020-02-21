package ru.alfabank.alfamir.image.data.source.repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.image.data.source.remote.ImageRemoteDataSource;
import ru.alfabank.alfamir.di.qualifiers.Remote;

@Module
abstract public class ImageRepositoryModule {

    @Singleton
    @Binds
    @Remote
    abstract ImageDataSource provideImageRemoteDataSource(ImageRemoteDataSource dataSource);

}
