package ru.alfabank.alfamir.utility.date_formatter;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
abstract public class DateFormatterModule {
    @Singleton
    @Binds
    abstract DateFormatter provideDateFormatter(DateFormatterImpl dateFormatter);
}