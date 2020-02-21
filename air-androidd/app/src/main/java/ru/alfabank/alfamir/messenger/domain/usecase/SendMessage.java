package ru.alfabank.alfamir.messenger.domain.usecase;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.messenger.data.dto.Attachment;
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerRepository;
import ru.alfabank.alfamir.messenger.domain.mapper.MessageMapper;
import ru.alfabank.alfamir.messenger.presentation.dto.Message;

import javax.inject.Inject;
import java.util.List;

public class SendMessage {
    private MessengerRepository mMessengerRepository;
    private MessageMapper mMessageMapper;

    @Inject
    SendMessage(MessengerRepository messengerRepository,
                MessageMapper messageMapper) {
        mMessengerRepository = messengerRepository;
        mMessageMapper = messageMapper;
    }


    public Observable<Message> sendMessage(String chatId, String text, List<Attachment> attachments, long messageId, String type) {
        if (attachments.isEmpty()) {
            return execute(chatId, text, messageId, type);
        }
        return mMessengerRepository.sendMessage(chatId, text, attachments, type)
                .flatMap(messageRaw -> {
                    mMessengerRepository.refreshChatList();
                    Message message = mMessageMapper.apply(messageRaw);
                    message.setViewId(messageId);
                    return Observable.just(message);
                });
    }

    public Observable<Message> execute(String chatId, String text, long messageId, String type) {
        return mMessengerRepository.sendMessage(chatId, text, type)
                .flatMap(messageRaw -> {
                    mMessengerRepository.refreshChatList();
                    Message message = mMessageMapper.apply(messageRaw);
                    message.setViewId(messageId);
                    return Observable.just(message);
                });
    }

    public static class RequestValues implements UseCase.RequestValues {
        private final String mChatId;
        private final String mText;
        private final long mMessageId;

        public RequestValues(String chatId,
                             String text,
                             long messageId) {
            mChatId = chatId;
            mText = text;
            mMessageId = messageId;
        }

        String getChatId() {
            return mChatId;
        }

        String getText() {
            return mText;
        }

        long getMessageId() {
            return mMessageId;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private final Message mMessage; // TODO check

        ResponseValue(Message message) {
            mMessage = message;
        }

        public Message getMessage() {
            return mMessage;
        }
    }
}

