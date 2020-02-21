package ru.alfabank.alfamir.alfa_tv.domain.mapper;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.alfa_tv.data.dto.HostRaw;
import ru.alfabank.alfamir.alfa_tv.data.dto.ShowRaw;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.Host;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.Show;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter;
import ru.alfabank.alfamir.utility.initials.InitialsProvider;

import static ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_0;
import static ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_7;
import static ru.alfabank.alfamir.Constants.DateFormatter.TIME_ZONE_MOSCOW;
import static ru.alfabank.alfamir.Constants.Show.SHOW;
import static ru.alfabank.alfamir.Constants.Show.SHOW_CURRENT;

public class ShowMapper implements Function<ShowRaw, Show> {

    private HostMapper mHostMapper;
    private DateFormatter mDateFormatter;

    @Inject
    ShowMapper(HostMapper hostMapper,
               DateFormatter dateFormatter,
               InitialsProvider initialsProvider){
        mHostMapper = hostMapper;
        mDateFormatter = dateFormatter;
    }

    @Override
    public Show apply(ShowRaw showRaw) throws Exception {
        int id = showRaw.getId();

        String startDate = mDateFormatter.formatDate(showRaw.getStartDate(), DATE_PATTERN_0,
                DATE_PATTERN_7, TIME_ZONE_MOSCOW);
        String endDate = mDateFormatter.formatDate(showRaw.getEndDate(), DATE_PATTERN_0,
                DATE_PATTERN_7, TIME_ZONE_MOSCOW);
        long longStartDate = mDateFormatter.formatDate(showRaw.getStartDate(),
                DATE_PATTERN_0, TIME_ZONE_MOSCOW);
        long longEndDate = mDateFormatter.formatDate(showRaw.getEndDate(),
                DATE_PATTERN_0, TIME_ZONE_MOSCOW);
        String title = showRaw.getTitle();
        String description = showRaw.getDescription();
        HostRaw hostRaw = showRaw.getHost();
        Host host = mHostMapper.apply(hostRaw);
        String room = showRaw.getRoom();
        int isPasswordRequired = showRaw.getIsPasswordRequired();
        String password = showRaw.getPassword();
        int isOnAir = showRaw.getIsOnAir();
        Show show = new Show.Builder()
                .id(id)
                .startDate(startDate)
                .startDate(longStartDate)
                .endDate(endDate)
                .endDate(longEndDate)
                .title(title)
                .description(description)
                .host(host)
                .room(room)
                .isPasswordRequired(isPasswordRequired)
                .password(password)
                .isOnAir(isOnAir)
                .build();
        Long currentTimeAsMilliSec = new Date().getTime();
        if(isOnAir == 1 || currentTimeAsMilliSec > longStartDate && currentTimeAsMilliSec < longEndDate){
            show.setViewType(SHOW_CURRENT);
        } else {
            show.setViewType(SHOW);
        }
        return show;
    }
}
