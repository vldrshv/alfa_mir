package ru.alfabank.alfamir.notification.data.source.repository;


import java.util.List;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.dto.notifications_settings.NotificationsSettings;
import ru.alfabank.alfamir.notification.data.dto.NotificationRaw;

public interface NotificationsDataSource {

    Observable<String> sendPushToken(String token);

    Observable<List<NotificationRaw>> getNotifications(long notificationId);

    Observable <String> getSecretSantaStatus();

    void getUserSettings(LoadSettingsCallback callback);

    void uploadUserSettings(NotificationsSettings settings, UploadSettingsCallback callback);

    interface LoadSettingsCallback {
        void onSettingsLoaded(NotificationsSettings settings);
        void onServerNotAvailable();
    }

    interface UploadSettingsCallback {
        void onSettingsUploaded();
        void onServerNotAvailable();
    }

}
