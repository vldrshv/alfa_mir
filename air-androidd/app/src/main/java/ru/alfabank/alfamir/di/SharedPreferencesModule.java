package ru.alfabank.alfamir.di;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.di.qualifiers.AppContext;
import ru.alfabank.alfamir.di.qualifiers.shared_pref.InfoDialogStatus;
import ru.alfabank.alfamir.di.qualifiers.shared_pref.NotificationSettings;
import ru.alfabank.alfamir.di.qualifiers.shared_pref.UserIdData;

import static android.content.Context.MODE_PRIVATE;

@Module
public class SharedPreferencesModule {

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(@AppContext Context context){
        return context.getSharedPreferences("boolean", MODE_PRIVATE);
    }

    @NotificationSettings
    @Provides
    @Singleton
    SharedPreferences provideNotificationSharedPreferences(@AppContext Context context){
        return context.getSharedPreferences("notification_settings", MODE_PRIVATE);
    }

    @InfoDialogStatus
    @Provides
    @Singleton
    SharedPreferences provideInfoDialogsStatus(@AppContext Context context){
        return context.getSharedPreferences("info_dialog_status", MODE_PRIVATE);
    }

    @UserIdData
    @Provides
    @Singleton
    SharedPreferences provideUserIdData(@AppContext Context context){
        return context.getSharedPreferences("user_id_data", MODE_PRIVATE);
    }

}
