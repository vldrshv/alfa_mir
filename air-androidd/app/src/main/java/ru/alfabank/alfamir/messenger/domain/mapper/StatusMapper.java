package ru.alfabank.alfamir.messenger.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.messenger.data.dto.StatusRaw;
import ru.alfabank.alfamir.messenger.presentation.dto.Status;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter;

import static ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_10;
import static ru.alfabank.alfamir.Constants.DateFormatter.TIME_ZONE_GREENWICH;

public class StatusMapper implements Function<StatusRaw, Status> {

    private DateFormatter mDateFormatter;

    @Inject
    public StatusMapper(DateFormatter dateFormatter){
        mDateFormatter = dateFormatter;
    }

    @Override
    public Status apply(StatusRaw messageRaw) throws Exception {
        String chatId = messageRaw.getChatId();
        long msgId = messageRaw.getMsgId();
        String state = messageRaw.getState();
        String userLogin = messageRaw.getUserLogin();
        String date = messageRaw.getDate();
        long longDate = mDateFormatter.formatDate(date,
                DATE_PATTERN_10, TIME_ZONE_GREENWICH);

        Status status = new Status.Builder()
                .chatId(chatId)
                .msgId(msgId)
                .state(state)
                .userLogin(userLogin)
                .date(longDate)
                .build();
        return status;
    }

}
