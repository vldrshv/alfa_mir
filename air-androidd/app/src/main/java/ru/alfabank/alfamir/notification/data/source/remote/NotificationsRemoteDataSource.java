package ru.alfabank.alfamir.notification.data.source.remote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.dto.notifications_settings.NotificationsSettings;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.notification.data.dto.NotificationRaw;
import ru.alfabank.alfamir.notification.data.source.repository.NotificationsDataSource;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;

public class NotificationsRemoteDataSource implements NotificationsDataSource {
    private final WebService mWebService;

    @Inject
    NotificationsRemoteDataSource(WebService service, LogWrapper logWrapper) {
        mWebService = service;
    }


    @Override
    public Observable<String> sendPushToken(String token) {
        String request = RequestFactory.INSTANCE.formSendPushTokenRequest(token);
        return mWebService.requestX(request);
    }

    @Override
    public Observable<List<NotificationRaw>> getNotifications(long notificationId) {
        String request = RequestFactory.INSTANCE.formGetNotificationsRequest(notificationId);
        return mWebService.requestX(request)
                .map(JsonWrapper::getNotificationRawList);
    }

    @Override
    public Observable<String> getSecretSantaStatus() {
        String request = RequestFactory.INSTANCE.formCheckSecretSantaStatus();
        return mWebService.requestX(request);
    }

    @Override
    public void getUserSettings(LoadSettingsCallback callback) {
        mWebService.getUserSettings(callback);
    }

    @Override
    public void uploadUserSettings(NotificationsSettings settings, UploadSettingsCallback callback) {
        mWebService.uploadUserSettings(settings, callback);
    }

}
