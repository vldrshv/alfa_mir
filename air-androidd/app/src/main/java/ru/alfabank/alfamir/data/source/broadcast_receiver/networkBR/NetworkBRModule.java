package ru.alfabank.alfamir.data.source.broadcast_receiver.networkBR;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class NetworkBRModule {

    @Singleton
    @Binds
    abstract NetworkBR getConnectionTracker(NetworkBRImp presenter);

}