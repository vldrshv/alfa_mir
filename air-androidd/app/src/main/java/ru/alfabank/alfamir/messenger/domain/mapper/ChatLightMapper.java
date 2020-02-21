package ru.alfabank.alfamir.messenger.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.messenger.data.dto.ChatLightRaw;
import ru.alfabank.alfamir.messenger.presentation.dto.ChatLight;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter;

import static ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_10;
import static ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_7;
import static ru.alfabank.alfamir.Constants.DateFormatter.TIME_ZONE_GREENWICH;

public class ChatLightMapper implements Function<ChatLightRaw, ChatLight> {

    private DateFormatter mDateFormatter;

    @Inject
    public ChatLightMapper(DateFormatter dateFormatter){
        mDateFormatter = dateFormatter;
    }

    @Override
    public ChatLight apply(ChatLightRaw chatRaw) throws Exception {
        String id = chatRaw.getId();
        String type = chatRaw.getType();
        String title = chatRaw.getTitle();
        int unreadCount = chatRaw.getUnreadCount();
        String lastMessage = chatRaw.getLastMessage();
        String lastMessageDate = chatRaw.getLastMessageDate();
        lastMessageDate = mDateFormatter.formatDate(lastMessageDate, DATE_PATTERN_10,
                DATE_PATTERN_7, TIME_ZONE_GREENWICH);
        String lastMessageUserLogin = chatRaw.getLastMessageUserLogin();
        String lastMessageUserName = chatRaw.getLastMessageUserName();
        String correspondentLogin = chatRaw.getCorrespondentLogin();
        String correspondentPicUrl = chatRaw.getCorrespondentPicUrl();
        String correspondentTitle = chatRaw.getCorrespondentTitle();
        int correspondentOnlineStatus = chatRaw.getCorrespondentOnlineStatus();

        ChatLight chatLight = new ChatLight.Builder()
                .id(id)
                .type(type)
                .title(title)
                .unreadCount(unreadCount)
                .lastMessage(lastMessage)
                .lastMessageDate(lastMessageDate)
                .lastMessageUserLogin(lastMessageUserLogin)
                .lastMessageUserName(lastMessageUserName)
                .correspondentLogin(correspondentLogin.toLowerCase())
                .correspondentPicUrl(correspondentPicUrl)
                .correspondentTitle(correspondentTitle)
                .correspondentOnlineStatus(correspondentOnlineStatus)
                .build();
        return chatLight;
    }

}