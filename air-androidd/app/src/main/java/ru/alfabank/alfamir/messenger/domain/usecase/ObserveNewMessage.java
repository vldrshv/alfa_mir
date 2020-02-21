package ru.alfabank.alfamir.messenger.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerRepository;
import ru.alfabank.alfamir.messenger.domain.mapper.MessageMapper;
import ru.alfabank.alfamir.messenger.presentation.dto.Message;

public class ObserveNewMessage extends UseCase<ObserveNewMessage.RequestValues, ObserveNewMessage.ResponseValue> {
    private MessengerRepository mMessengerRepository;
    private MessageMapper mMessageMapper;

    @Inject
    ObserveNewMessage(MessengerRepository messengerRepository,
                             MessageMapper messageMapper){
        mMessengerRepository = messengerRepository;
        mMessageMapper = messageMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        return Observable.just(new ResponseValue(mMessengerRepository.subscribeForMessage()
                .map(mMessageMapper)));
    }

    public static class RequestValues implements UseCase.RequestValues { }

    public static class ResponseValue implements UseCase.ResponseValue {
        private Observable<Message> mMessageObservable;
        ResponseValue(Observable <Message> messageObservable){
            mMessageObservable = messageObservable;
        }
        public Observable<Message> getMessageObservable() {
            return mMessageObservable;
        }
    }

}
