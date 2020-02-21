package ru.alfabank.alfamir.utility.initials;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class InitialsProviderModule {

    @Binds
    abstract InitialsProvider logWrapper (InitialsProviderImp initialsProviderImp);
}