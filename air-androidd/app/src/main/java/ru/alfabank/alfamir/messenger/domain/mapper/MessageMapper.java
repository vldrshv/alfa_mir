package ru.alfabank.alfamir.messenger.domain.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.messenger.data.dto.MessageRaw;
import ru.alfabank.alfamir.messenger.data.dto.StatusRaw;
import ru.alfabank.alfamir.messenger.data.dto.UserRaw;
import ru.alfabank.alfamir.messenger.presentation.dto.Message;
import ru.alfabank.alfamir.messenger.presentation.dto.Status;
import ru.alfabank.alfamir.messenger.presentation.dto.User;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter;

import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_7;
import static ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_10;
import static ru.alfabank.alfamir.Constants.DateFormatter.TIME_ZONE_GREENWICH;


public class MessageMapper implements Function<MessageRaw, Message> {

    private DateFormatter mDateFormatter;
    private StatusMapper mStatusMapper;
    private UserMapper mUserMapper;

    @Inject
    public MessageMapper(DateFormatter dateFormatter,
                         StatusMapper statusMapper,
                         UserMapper userMapper) {
        mDateFormatter = dateFormatter;
        mStatusMapper = statusMapper;
        mUserMapper = userMapper;
    }

    @Override
    public Message apply(MessageRaw messageRaw) throws Exception {
        long id = messageRaw.getId();
        String chatId = messageRaw.getChatId();
        String unformattedDate = messageRaw.getDate();
        String recipientId = messageRaw.getRecipientId();
        String text = messageRaw.getText();
        UserRaw senderRaw = messageRaw.getSenderUserRaw();
        long lUnformattedDate = mDateFormatter.formatDate(unformattedDate, DATE_PATTERN_10, TIME_ZONE_GREENWICH);

        String date = mDateFormatter.formatDate(unformattedDate, DATE_PATTERN_10, DATE_PATTERN_7, TIME_ZONE_GREENWICH);
        if (recipientId != null) recipientId = recipientId.toLowerCase();
        User senderUser = mUserMapper.apply(senderRaw);
        List<StatusRaw> statusRawList = messageRaw.getStatusRaw();
        List<Status> statusList = new ArrayList<>();
        for (StatusRaw statusRaw : statusRawList) {
            Status status = mStatusMapper.apply(statusRaw);
            statusList.add(status);
        }

        return new Message.Builder()
                .id(id)
                .chadId(chatId)
                .date(date)
                .lDate(lUnformattedDate)
                .recipientId(recipientId)
                .senderUser(senderUser)
                .text(text)
                .statusList(statusList)
                .attachments(messageRaw.getAttachments())
                .build();
    }
}
