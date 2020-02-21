package ru.alfabank.alfamir.notification.data.source.repository;

import android.content.SharedPreferences;
import androidx.annotation.Nullable;


import com.google.common.base.Strings;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.alfabank.alfamir.data.dto.notifications_settings.NotificationsSettings;
import ru.alfabank.alfamir.data.source.broadcast_receiver.сonnectionTracker.ConnectionTracker;
import ru.alfabank.alfamir.di.qualifiers.Local;
import ru.alfabank.alfamir.di.qualifiers.shared_pref.NotificationSettings;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.notification.data.dto.NotificationRaw;

@Singleton
public class  NotificationsRepository implements NotificationsDataSource {

    private NotificationsDataSource mNotificationsRemoteDataSource;
    private NotificationsDataSource mNotificationsLocalDataSource;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private NotificationsSettings mCachedSettings;
    private Disposable mDisposable;
    private String mSecretSantaStatusCached;

    @Inject
    public NotificationsRepository(@Remote NotificationsDataSource notificationsDataSource,
                                   @Local NotificationsDataSource notificationsLocalDataSource,
                                   @NotificationSettings SharedPreferences sharedPreferences,
                                   ConnectionTracker connectionHandler){
        mNotificationsRemoteDataSource = notificationsDataSource;
        mNotificationsLocalDataSource = notificationsLocalDataSource;
        mSharedPreferences = sharedPreferences;
        mEditor = mSharedPreferences.edit();

        mDisposable = connectionHandler.getConnectionStatusListener().subscribe(aBoolean -> checkForUnsentData());
    }

    @Override
    public Observable<String> sendPushToken(String token) {
        return mNotificationsRemoteDataSource.sendPushToken(token);
    }

    @Override
    public Observable<List<NotificationRaw>> getNotifications(long notificationId) {
        return mNotificationsRemoteDataSource.getNotifications(notificationId);
    }

    @Override
    public Observable<String> getSecretSantaStatus() {
        if (!Strings.isNullOrEmpty(mSecretSantaStatusCached)) return Observable.just(mSecretSantaStatusCached);
        return mNotificationsRemoteDataSource.getSecretSantaStatus().flatMap(s -> {
            mSecretSantaStatusCached = s;
            return Observable.just(s);
        });
    }

    @Override
    public void getUserSettings(LoadSettingsCallback callback) {
        if(mCachedSettings!=null){
            callback.onSettingsLoaded(mCachedSettings);
            return;
        }

        mNotificationsLocalDataSource.getUserSettings(new LoadSettingsCallback() {
            @Override
            public void onSettingsLoaded(NotificationsSettings settings) {
                mCachedSettings = settings;
                callback.onSettingsLoaded(mCachedSettings);
            }

            @Override
            public void onServerNotAvailable() {
                getFromRemoteDataSource(callback);
            }
        });

    }

    private void getFromRemoteDataSource(LoadSettingsCallback callback){
        mNotificationsRemoteDataSource.getUserSettings(new LoadSettingsCallback() {
            @Override
            public void onSettingsLoaded(NotificationsSettings settings) {
                mCachedSettings = settings;
                callback.onSettingsLoaded(settings);
            }

            @Override
            public void onServerNotAvailable() {
                callback.onServerNotAvailable();
            }
        });
    }

    @Override
    public void uploadUserSettings(NotificationsSettings settings, @Nullable UploadSettingsCallback callback) {
        mCachedSettings = settings;
        mNotificationsLocalDataSource.uploadUserSettings(settings, null);
        mNotificationsRemoteDataSource.uploadUserSettings(settings, new UploadSettingsCallback() {
            @Override
            public void onSettingsUploaded() {
                if(callback==null) return;
                callback.onSettingsUploaded();
            }

            @Override
            public void onServerNotAvailable() {
                setUploadStatus(false);
                if(callback==null) return;
                callback.onServerNotAvailable();
            }
        });
    }


    private void setUploadStatus(boolean isUploaded){
        int status = isUploaded ? 1 : 0;
        mEditor.putInt("isUploaded", status);
        mEditor.commit();
    }

    private void checkForUnsentData(){
        int isUploaded = mSharedPreferences.getInt("isUploaded", -1);
        switch (isUploaded){
            case 0:{
                mNotificationsLocalDataSource.getUserSettings(new LoadSettingsCallback() {
                    @Override
                    public void onSettingsLoaded(NotificationsSettings settings) {
                        mNotificationsRemoteDataSource.uploadUserSettings(settings, new UploadSettingsCallback() {
                            @Override
                            public void onSettingsUploaded() {
                                setUploadStatus(true);
                            }

                            @Override
                            public void onServerNotAvailable() {

                            }
                        });
                    }

                    @Override
                    public void onServerNotAvailable() {

                    }
                });
                break;
            }
            case 1: {

                break;
            }
        }
    }

    public void resetUserPushSettings(){
        uploadUserSettings(new NotificationsSettings(), null);
    }

}
