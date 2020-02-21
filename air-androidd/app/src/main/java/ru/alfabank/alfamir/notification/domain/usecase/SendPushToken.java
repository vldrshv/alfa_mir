package ru.alfabank.alfamir.notification.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.notification.data.source.repository.NotificationsRepository;

public class SendPushToken extends UseCase<SendPushToken.RequestValues, SendPushToken.ResponseValue> {

    private NotificationsRepository mNotificationsRepository;

    @Inject
    SendPushToken(NotificationsRepository notificationsRepository) {
        mNotificationsRepository = notificationsRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        String token = requestValues.getToken();
        return mNotificationsRepository.sendPushToken(token)
                .flatMap(searchResult -> Observable.just(new ResponseValue()));
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String mToken;
        public RequestValues(String token){
            mToken = token;
        }
        public String getToken() {
            return mToken;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

    }
}