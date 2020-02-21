package ru.alfabank.alfamir.calendar_event.data.source.repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.calendar_event.data.source.remote.CalendarEventRemoteDataSource;

@Module
abstract public class CalendarEventRepositoryModule {
    @Singleton
    @Binds
    @Remote
    abstract CalendarEventDataSource provideHomeDataSource(CalendarEventRemoteDataSource dataSource);
}