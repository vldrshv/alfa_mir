package ru.alfabank.alfamir.utility.logging.remote;

import dagger.Binds;
import dagger.Module;

/**
 * Created by U_M0WY5 on 28.05.2018.
 */

@Module
public abstract class LoggerModule {

    @Binds
    abstract LoggerContract.Provider logger (Logger logger);
}