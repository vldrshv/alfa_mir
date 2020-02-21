package ru.alfabank.alfamir.utility.network;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class IpProviderModule {

    @Binds
    abstract IpProvider logWrapper (IpProviderImp logWrapperImp);
}