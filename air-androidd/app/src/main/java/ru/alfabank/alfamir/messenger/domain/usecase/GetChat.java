package ru.alfabank.alfamir.messenger.domain.usecase;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerRepository;
import ru.alfabank.alfamir.messenger.domain.mapper.ChatMapper;
import ru.alfabank.alfamir.messenger.presentation.dto.Chat;
import ru.alfabank.alfamir.messenger.presentation.dto.DisplayableMessageItem;
import ru.alfabank.alfamir.messenger.presentation.dto.Message;
import ru.alfabank.alfamir.messenger.presentation.dto.User;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;

public class GetChat {
    private MessengerRepository mMessengerRepository;
    private ChatMapper mChatMapper;
    private LogWrapper mLogWrapper;
    private DateFormatter mDateFormatter;

    @Inject
    GetChat(MessengerRepository messengerRepository,
                   ChatMapper chatMapper,
                   LogWrapper logWrapper,
                   DateFormatter dateFormatter) {
        mMessengerRepository = messengerRepository;
        mChatMapper = chatMapper;
        mLogWrapper = logWrapper;
        mDateFormatter = dateFormatter;
    }

    public Observable<ResponseValue> execute(String type, List<String> userId, String chatId) {
        int amount = 60;
        return mMessengerRepository.loadChat(userId, chatId, amount, type)
                .map(mChatMapper)
                .flatMap(chat -> {
                    int onlineStatus = 0;
                    String lastOnline = "";
                    String respondentId = "";
                    List<User> users = chat.getUsers();
                    for (User user : users) {
                        if (!Constants.Initialization.getUSER_LOGIN().toLowerCase().equals(user.getId().toLowerCase())) {
                            respondentId = user.getId().toLowerCase();
                            onlineStatus = user.getOnlineStatus();
                            lastOnline = user.getLastOnline();
                            lastOnline = getLastOnlineTimeFormatted(lastOnline);
                            break;
                        }
                    }
                    List<Long> newMsgIds = getNewMessageIds(chat.getDisplayableMessageItemList());
                    return Observable.just(new ResponseValue(respondentId, lastOnline, onlineStatus, newMsgIds, chat));
                });
    }

    private String getLastOnlineTimeFormatted(String lastOnline) {
        long lLastOnline = mDateFormatter.formatDate(lastOnline, Constants.DateFormatter.DATE_PATTERN_10, Constants.DateFormatter.TIME_ZONE_GREENWICH);
        String date;
        if (DateUtils.isToday(lLastOnline)) {
            date = "был(-а) сегодня " + mDateFormatter.formatDate(lLastOnline, Constants.DateFormatter.DATE_PATTERN_10, Constants.DateFormatter.DATE_PATTERN_7);
        } else if (DateUtils.isToday(lLastOnline + TimeUnit.DAYS.toMillis(1))) {
            date = "был(-а) вчера " + mDateFormatter.formatDate(lLastOnline, Constants.DateFormatter.DATE_PATTERN_10, Constants.DateFormatter.DATE_PATTERN_7);
        } else {
            date = "был(-а) " + mDateFormatter.formatDate(lLastOnline, Constants.DateFormatter.DATE_PATTERN_10, Constants.DateFormatter.DATE_PATTERN_1);
        }
        return date;
    }

    private List<Long> getNewMessageIds(List<DisplayableMessageItem> displayableMessageItems) {
        List<Long> msgIds = new ArrayList<>();
        for (DisplayableMessageItem displayableMessageItem : displayableMessageItems) {
            if (displayableMessageItem instanceof Message) {
                Message message = (Message) displayableMessageItem;
                long msgId = message.getId();
                msgIds.add(msgId);
            }
        }
        return msgIds;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private List<String> mUserId;
        private String mChatId;

        public RequestValues(List<String> userId, String chatId) {
            mUserId = userId;
            mChatId = chatId;
        }

        List<String> getUserId() {
            return mUserId;
        }

        String getChatId() {
            return mChatId;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private String mLastOnline;
        private String mRespondentId;
        private int mCorrespondentOnlineStatus;
        private List<Long> mNewMsgIds;
        private Chat mChat;

        public ResponseValue(
                String respondentId,
                String lastOnline,
                int correspondentOnlineStatus,
                List<Long> newMsgIds,
                Chat chat) {
            mRespondentId = respondentId;
            mLastOnline = lastOnline;
            mCorrespondentOnlineStatus = correspondentOnlineStatus;
            mNewMsgIds = newMsgIds;
            mChat = chat;
        }

        public Chat getChat() {
            return mChat;
        }

        public String getRespondentId() {
            return mRespondentId;
        }

        public int getCorrespondentOnlineStatus() {
            return mCorrespondentOnlineStatus;
        }

        public List<Long> getNewMsgIds() {
            return mNewMsgIds;
        }

        public String getLastOnline() {
            return mLastOnline;
        }
    }
}
