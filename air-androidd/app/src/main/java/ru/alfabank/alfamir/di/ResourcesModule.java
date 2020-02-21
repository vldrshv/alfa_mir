package ru.alfabank.alfamir.di;


import android.content.Context;
import android.content.res.Resources;

import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.di.qualifiers.AppContext;

@Module
public class ResourcesModule {

    @Provides
    Resources provideSharedPreferences(@AppContext Context context){
        return context.getResources();
    }

}