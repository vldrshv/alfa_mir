package ru.alfabank.alfamir.utility.database

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.alfabank.alfamir.di.qualifiers.AppContext
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDbProvider(@AppContext context: Context): DbProvider {
        return DbProviderImpl(context)
    }
}