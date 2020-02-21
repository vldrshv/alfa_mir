package ru.alfabank.alfamir.profile.data.source.repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.profile.data.source.remote.ProfileRemoteDataSource;
import ru.alfabank.alfamir.di.qualifiers.Remote;

@Module
abstract public class ProfileRepositoryModule {

    @Singleton
    @Binds
    @Remote
    abstract ProfileDataSource provideProfileRemoteDataSource(ProfileRemoteDataSource dataSource);



}
