package ru.alfabank.alfamir.data.source.repositories.old_trash;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.notification.data.dto.ModelNotification;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;

import static ru.alfabank.alfamir.Constants.REQUEST_TYPE_GET_NEW_NOTIFICATIONS_COUNT;
import static ru.alfabank.alfamir.Constants.REQUEST_TYPE_LOAD;
import static ru.alfabank.alfamir.Constants.REQUEST_TYPE_LOAD_MORE;
import static ru.alfabank.alfamir.Constants.REQUEST_TYPE_MARK_ALL_NOTIFICATION_AS_VIEWED;
import static ru.alfabank.alfamir.Constants.REQUEST_TYPE_MARK_NOTIFICATION_AS_VIEWED;
import static ru.alfabank.alfamir.Constants.REQUEST_TYPE_RELOAD;

public class NotificationsRepositoryOld implements WebService.WebRequestListener {

    private NotificationsInflater activityCallback;
    private NewNotificationsCheck newNotificationsCheck;
    private List<ModelNotification> innerNotificationList;
    private List<ModelNotification> externalNotificationList;
    private WebService webService;
    private boolean isBadgeClicked;

    private SharedPreferences.Editor shPrefEditor;
    private SharedPreferences shPref;

    public NotificationsRepositoryOld(WebService webService, SharedPreferences.Editor shPrefEditor, SharedPreferences shPref) {
        this.webService = webService;
        this.shPrefEditor = shPrefEditor;
        this.shPref = shPref;
    }

    public void getNotifications(NotificationsInflater activityCallback, int requestType, int lastId) {
        if (isRepositoryIdle()) {
            switch (requestType) {
                case REQUEST_TYPE_LOAD: {
                    if (innerNotificationList != null) {
                        externalNotificationList = new ArrayList<>();
                        for (ModelNotification item : innerNotificationList) {
                            externalNotificationList.add(item);
                        }
                        activityCallback.onNotificationsReceived(externalNotificationList, requestType);
                    } else {
                        this.activityCallback = activityCallback;
                        webService.request(RequestFactory.INSTANCE.formGetNotifications(0), this, requestType);
                    }
                    break;
                }
                case REQUEST_TYPE_RELOAD: {
                    this.activityCallback = activityCallback;
                    webService.request(RequestFactory.INSTANCE.formGetNotifications(0), this, requestType);
                    break;
                }
                case REQUEST_TYPE_LOAD_MORE: {
                    this.activityCallback = activityCallback;
                    webService.request(RequestFactory.INSTANCE.formGetNotifications(lastId), this, requestType);
                    break;
                }
            }
        }
    }


    private boolean isRepositoryIdle() {
        return activityCallback == null;
    }

    public void markAllAsRead() {
        for (ModelNotification modelNotification : innerNotificationList) {
            modelNotification.setViewed(1);
        }
        webService.request(RequestFactory.INSTANCE.formMarkNotificationAsRead(-1), this, REQUEST_TYPE_MARK_ALL_NOTIFICATION_AS_VIEWED);
    }

    public void markAsRead(int messageId) {
        for (ModelNotification modelNotification : innerNotificationList) {
            if (modelNotification.getNotificationid() == messageId) {
                modelNotification.setViewed(1);
            }
        }
        webService.request(RequestFactory.INSTANCE.formMarkNotificationAsRead(messageId), this, REQUEST_TYPE_MARK_NOTIFICATION_AS_VIEWED);
    }

    @Override
    public void onResponse(JsonWrapper jsonWrapper, int responseType) {
        switch (responseType) {
            case REQUEST_TYPE_LOAD:
            case REQUEST_TYPE_RELOAD:
                innerNotificationList = jsonWrapper.getNotifications();
                externalNotificationList = jsonWrapper.getNotifications();

                if (externalNotificationList.size() != 0) {
                    shPrefEditor.putInt("last_item_id", innerNotificationList.get(0).getNotificationid());
                    shPrefEditor.commit();
                }
                if (activityCallback != null) {
                    activityCallback.onNotificationsReceived(externalNotificationList, responseType);
                }
                break;
            case REQUEST_TYPE_LOAD_MORE: {
                externalNotificationList = jsonWrapper.getNotifications();
                if (responseType == REQUEST_TYPE_LOAD_MORE) {
                    for (int i = 0; i < externalNotificationList.size(); i++) {
                        innerNotificationList.add(externalNotificationList.get(i));
                    }
                }
                if (activityCallback != null) {
                    activityCallback.onNotificationsReceived(externalNotificationList, responseType);
                }
            }
            case REQUEST_TYPE_MARK_NOTIFICATION_AS_VIEWED: {
                // TODO we need a way to check the id in response from Webservice
            }
            case REQUEST_TYPE_MARK_ALL_NOTIFICATION_AS_VIEWED: {

            }
            case REQUEST_TYPE_GET_NEW_NOTIFICATIONS_COUNT: {
                if (newNotificationsCheck != null) {
                    newNotificationsCheck.onNotificationsReceived(jsonWrapper.getNewNotificationsCount());
                }
            }
        }
        activityCallback = null;
    }

    @Override
    public void onFailure(JsonWrapper jsonWrapper, int responseType) {
        if (activityCallback != null) {
            activityCallback.onError();
        }
        activityCallback = null;
    }

    public interface NotificationsInflater {
        void onNotificationsReceived(List<ModelNotification> notifications, int responseType);

        void onError();
    }

    public interface NewNotificationsCheck {
        void onNotificationsReceived(int count);
    }

}
