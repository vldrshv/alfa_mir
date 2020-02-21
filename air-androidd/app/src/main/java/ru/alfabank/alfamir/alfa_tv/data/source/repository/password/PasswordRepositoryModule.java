package ru.alfabank.alfamir.alfa_tv.data.source.repository.password;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
abstract public class PasswordRepositoryModule {
    @Singleton
    @Binds
    abstract PasswordDataSource provideProfileRemoteDataSource(PasswordRepository dataSource);
}