package ru.alfabank.alfamir.messenger.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerRepository;

public class SendMessageStatus extends UseCase<SendMessageStatus.RequestValues, SendMessageStatus.ResponseValue> {
    private MessengerRepository mMessengerRepository;

    @Inject
    public SendMessageStatus(MessengerRepository messengerRepository){
        mMessengerRepository = messengerRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        List<Long> msgId = requestValues.getMsgIds();
        String chatId = requestValues.getIds();
        return mMessengerRepository.sendMessageReadStatus(msgId, chatId)
                .flatMap(json -> Observable.just(new ResponseValue()));
    }

    public static class RequestValues implements UseCase.RequestValues {
        private List<Long> mMsgIds;
        private String mChatId;
        public RequestValues(List<Long> msgIds, String chatId){
            mMsgIds = msgIds;
            mChatId = chatId;
        }
        List<Long> getMsgIds() {
            return mMsgIds;
        }
        String getIds() {
            return mChatId;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue { }
}
