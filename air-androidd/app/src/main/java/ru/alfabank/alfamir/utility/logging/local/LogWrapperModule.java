package ru.alfabank.alfamir.utility.logging.local;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LogWrapperModule {

    @Binds
    abstract LogWrapper logWrapper (LogWrapperImp logWrapperImp);
}