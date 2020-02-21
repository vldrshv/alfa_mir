package ru.alfabank.alfamir.data.source.local;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.dto.notifications_settings.NotificationsSettings;
import ru.alfabank.alfamir.notification.data.dto.NotificationRaw;
import ru.alfabank.alfamir.notification.data.source.repository.NotificationsDataSource;
import ru.alfabank.alfamir.di.qualifiers.shared_pref.NotificationSettings;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;

/**
 * Created by U_m0wy5 on 29.05.2018.
 */


public class NotificationsLocalDataSource implements NotificationsDataSource {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Inject
    NotificationsLocalDataSource(@NotificationSettings SharedPreferences sharedPreferences){
        mSharedPreferences = sharedPreferences;
        mEditor = mSharedPreferences.edit();
    }

    @Override
    public Observable<String> sendPushToken(String token) {
        return null;
    }

    @Override
    public Observable<List<NotificationRaw>> getNotifications(long notificationId) {
        return null;
    }

    @Override
    public Observable<String> getSecretSantaStatus() {
        return null;
    }

    @Override
    public void getUserSettings(LoadSettingsCallback callback) {
        String jsonSettings = mSharedPreferences.getString("settings", null);
        NotificationsSettings settings = JsonWrapper.getNotificationSettings(jsonSettings);
        if(settings==null){
            callback.onServerNotAvailable();
        } else {
            callback.onSettingsLoaded(settings);
        }
    }

    @Override
    public void uploadUserSettings(NotificationsSettings settings, UploadSettingsCallback callback) {
        Gson gson = new Gson();
        String jsonSettings = gson.toJson(settings);
        mEditor.putString("settings", jsonSettings);
        mEditor.commit();
    }

}
