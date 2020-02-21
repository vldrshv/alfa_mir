package ru.alfabank.alfamir.utility.update_notifier;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class UpdateNotifierModule {

    @Singleton
    @Binds
    abstract UpdateNotifier getConnectionTracker(UpdateNotifierImpl presenter);

}
