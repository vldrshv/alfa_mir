package ru.alfabank.alfamir.notification.data.source.repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.data.source.local.NotificationsLocalDataSource;
import ru.alfabank.alfamir.notification.data.source.remote.NotificationsRemoteDataSource;
import ru.alfabank.alfamir.di.qualifiers.Local;
import ru.alfabank.alfamir.di.qualifiers.Remote;

/**
 * Created by U_M0WY5 on 23.05.2018.
 */
@Module
abstract public class NotificationsRepositoryModule {

    @Singleton
    @Binds
    @Remote
    abstract NotificationsDataSource provideImageRemoteDataSource(NotificationsRemoteDataSource dataSource);

    @Singleton
    @Binds
    @Local
    abstract NotificationsDataSource provideImageLocalDataSource(NotificationsLocalDataSource dataSource);

}