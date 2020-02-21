package ru.alfabank.alfamir.utility.logging.remote;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by U_M0WY5 on 28.05.2018.
 */

@Module
public abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract OnClearFromRecentService bindClearFromRecentService();

    

}
