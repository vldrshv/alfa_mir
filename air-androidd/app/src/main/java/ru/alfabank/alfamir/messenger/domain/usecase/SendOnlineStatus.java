package ru.alfabank.alfamir.messenger.domain.usecase;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerRepository;

import javax.inject.Inject;


public class SendOnlineStatus extends UseCase<SendOnlineStatus.RequestValues, SendOnlineStatus.ResponseValue> {

    private MessengerRepository mMessengerRepository;

    @Inject
    SendOnlineStatus(MessengerRepository messengerRepository) {
        mMessengerRepository = messengerRepository;
    }

    @Override
    public Observable<SendOnlineStatus.ResponseValue> execute(SendOnlineStatus.RequestValues requestValues) {
        return mMessengerRepository.reportOnline()
                .flatMap(json -> Observable.just(new ResponseValue()));
    }

    public static class RequestValues implements UseCase.RequestValues {
    }

    public static class ResponseValue implements UseCase.ResponseValue {
    }
}
