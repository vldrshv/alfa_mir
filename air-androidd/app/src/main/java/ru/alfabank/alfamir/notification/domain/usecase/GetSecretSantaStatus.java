package ru.alfabank.alfamir.notification.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.notification.data.source.repository.NotificationsRepository;

public class GetSecretSantaStatus
 extends UseCase<GetSecretSantaStatus.RequestValues, GetSecretSantaStatus.ResponseValue> {

    private NotificationsRepository mNotificationsRepository;

    @Inject
    GetSecretSantaStatus(NotificationsRepository notificationsRepository) {
        mNotificationsRepository = notificationsRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        return mNotificationsRepository.getSecretSantaStatus()
                .flatMap(response -> {
                    int status =  Integer.parseInt(response);
                    return Observable.just(new ResponseValue(status));
                });

    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private int mStatus;
        ResponseValue(int status) {
            mStatus = status;
        }
        public int getStatus() {
            return mStatus;
        }
    }
}