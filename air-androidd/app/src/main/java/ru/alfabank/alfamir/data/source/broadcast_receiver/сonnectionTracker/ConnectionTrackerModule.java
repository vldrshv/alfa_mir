package ru.alfabank.alfamir.data.source.broadcast_receiver.сonnectionTracker;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ConnectionTrackerModule {
    @Singleton
    @Binds
    abstract ConnectionTracker getConnectionTracker(ConnectionTrackerImpl presenter);
}
