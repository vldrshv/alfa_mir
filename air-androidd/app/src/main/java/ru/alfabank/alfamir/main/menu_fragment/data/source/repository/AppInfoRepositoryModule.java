package ru.alfabank.alfamir.main.menu_fragment.data.source.repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.main.menu_fragment.data.source.remote.AppInfoRemoteDataSorce;

@Module
abstract public class AppInfoRepositoryModule {
    @Singleton
    @Binds
    @Remote
    abstract AppInfoDataSource provideSearchDataSource(AppInfoRemoteDataSorce appInfoRemoteDataSorce);
}