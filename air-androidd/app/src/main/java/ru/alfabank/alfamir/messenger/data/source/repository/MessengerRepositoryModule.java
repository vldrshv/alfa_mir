package ru.alfabank.alfamir.messenger.data.source.repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.messenger.data.source.remote.MessengerRemoteDataSource;
import ru.alfabank.alfamir.di.qualifiers.Remote;

@Module
abstract public class MessengerRepositoryModule {
    @Singleton
    @Binds
    @Remote
    abstract MessengerDataSource provideProfileRemoteDataSource(MessengerRemoteDataSource dataSource);
}
