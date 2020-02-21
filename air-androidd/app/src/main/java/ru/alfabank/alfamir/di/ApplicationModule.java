package ru.alfabank.alfamir.di;

import android.content.Context;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.di.qualifiers.AppContext;

@Module
public abstract class ApplicationModule {

    @Binds
    @AppContext
    abstract Context bindContext(App application);

}
