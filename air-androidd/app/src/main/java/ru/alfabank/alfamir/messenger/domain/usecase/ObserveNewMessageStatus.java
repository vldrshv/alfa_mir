package ru.alfabank.alfamir.messenger.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerRepository;
import ru.alfabank.alfamir.messenger.domain.mapper.StatusMapper;
import ru.alfabank.alfamir.messenger.presentation.dto.Status;

public class ObserveNewMessageStatus extends UseCase<ObserveNewMessageStatus.RequestValues, ObserveNewMessageStatus.ResponseValue> {
    private MessengerRepository mMessengerRepository;
    private StatusMapper mStatusMapper;

    @Inject
    public ObserveNewMessageStatus(MessengerRepository messengerRepository,
                                   StatusMapper statusMapper){
        mMessengerRepository = messengerRepository;
        mStatusMapper = statusMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        return Observable.just(new ResponseValue(mMessengerRepository.subscribeForStatus()
                .map(mStatusMapper)));
    }

    public static class RequestValues implements UseCase.RequestValues { }

    public static class ResponseValue implements UseCase.ResponseValue {
        private Observable<Status> mStatusObservable;
        public ResponseValue(Observable <Status> statusObservable){
            mStatusObservable = statusObservable;
        }
        public Observable<Status> getStatusObservable() {
            return mStatusObservable;
        }
    }

}