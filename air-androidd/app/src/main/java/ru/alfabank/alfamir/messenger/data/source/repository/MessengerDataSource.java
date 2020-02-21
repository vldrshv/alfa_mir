package ru.alfabank.alfamir.messenger.data.source.repository;

import io.reactivex.Observable;
import ru.alfabank.alfamir.messenger.data.dto.*;
import ru.alfabank.alfamir.messenger.data.dto.Attachment;
import ru.alfabank.alfamir.messenger.data.dto.MessageRaw;

import java.util.List;

public interface MessengerDataSource {

    Observable<List<PollDataRaw>> longPoll(List<String> pollDataId);

    Observable<ChatRaw> loadChat(List<String> userId, String chatId, int amount, String type);

    Observable<List<MessageRaw>> loadMoreMessages(String type, String chatId, long messageId, int amount, int direction);

    Observable<List<ChatLightRaw>> loadChats(LoadChatListRequestValues requestValues);

    Observable<MessageRaw> sendMessage(String chatId, String text, String type);

    Observable<MessageRaw> sendMessage(String chatId, String message, List<Attachment> attachments, String type);

    Observable<String> sendMessageReadStatus(List<Long> ids, String chatId);

    Observable<String> reportOnline();

    Observable<String> reportOffline();

    class LoadChatListRequestValues {
        private boolean mIsCacheDirty;

        public LoadChatListRequestValues(boolean isCacheDirty) {
            mIsCacheDirty = isCacheDirty;
        }

        public boolean getIsCacheDirty() {
            return mIsCacheDirty;
        }
    }

}
