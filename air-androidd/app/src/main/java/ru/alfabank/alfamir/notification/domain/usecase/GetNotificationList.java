package ru.alfabank.alfamir.notification.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.notification.data.source.repository.NotificationsRepository;
import ru.alfabank.alfamir.notification.domain.mapper.NotificationMapper;
import ru.alfabank.alfamir.notification.presentation.dto.Notification;

public class GetNotificationList extends UseCase<GetNotificationList.RequestValues, GetNotificationList.ResponseValue> {

    private NotificationsRepository mNotificationsRepository;
    private NotificationMapper mNotificationMapper;

    @Inject
    GetNotificationList(NotificationsRepository notificationsRepository, NotificationMapper notificationMapper) {
        mNotificationsRepository = notificationsRepository;
        mNotificationMapper = notificationMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        long notificationId = requestValues.getNotificationId();
        return mNotificationsRepository.getNotifications(notificationId)
                .flatMapIterable(notificationRawList -> notificationRawList)
                .map(mNotificationMapper)
                .toList()
                .flatMapObservable(notificationList -> {

                    return Observable.just(new ResponseValue(notificationList));
                });
    }

    public static class RequestValues implements UseCase.RequestValues {
        private long mNotificationId;
        public RequestValues(long notificationId){
            mNotificationId = notificationId;
        }
        public long getNotificationId() {
            return mNotificationId;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private List<Notification> mNotificationList;
        public ResponseValue(List<Notification> notificationList){
            mNotificationList = notificationList;
        }
        public List<Notification> getNotificationList() {
            return mNotificationList;
        }
    }
}