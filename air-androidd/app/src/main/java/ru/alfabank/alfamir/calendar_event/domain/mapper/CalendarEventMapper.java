package ru.alfabank.alfamir.calendar_event.domain.mapper;

import com.google.common.base.Strings;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.calendar_event.data.dto.CalendarEventRaw;
import ru.alfabank.alfamir.calendar_event.presentation.dto.CalendarEvent;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter;

import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_3;
import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_7;
import static ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_10;
import static ru.alfabank.alfamir.Constants.DateFormatter.TIME_ZONE_GREENWICH;

public class CalendarEventMapper implements Function<CalendarEventRaw, CalendarEvent> {

    private DateFormatter mDateFormatter;

    @Inject
    public CalendarEventMapper(DateFormatter dateFormatter){
        mDateFormatter = dateFormatter;
    }

    @Override
    public CalendarEvent apply(CalendarEventRaw calendarEventRaw) throws Exception {
        String title = calendarEventRaw.getTitle();
        String description = calendarEventRaw.getDescription();
        String unformattedStartDate = calendarEventRaw.getStartDate();
        String unformattedEndDate = calendarEventRaw.getEndDate();
        String location = calendarEventRaw.getLocation();
        String picUrl = calendarEventRaw.getPicURL();

        String date = mDateFormatter.formatDate(unformattedStartDate, // TODO should test for null
                DATE_PATTERN_10, DATE_PATTERN_3, TIME_ZONE_GREENWICH);
        String startTime = mDateFormatter.formatDate(unformattedStartDate,
                DATE_PATTERN_10, DATE_PATTERN_7, TIME_ZONE_GREENWICH);

        String endTime = mDateFormatter.formatDate(unformattedEndDate,
                DATE_PATTERN_10, DATE_PATTERN_7, TIME_ZONE_GREENWICH);
        String time = startTime;
        if(!Strings.isNullOrEmpty(endTime)) time = time + " - " + endTime;

        CalendarEvent calendarEvent = new CalendarEvent.Builder()
                .id(calendarEventRaw.getId())
                .title(title)
                .description(description)
                .date(date)
                .time(time)
                .location(location)
                .picUrl(picUrl)
                .tapable(calendarEventRaw.getTapable())
                .isSliDo(calendarEventRaw.isSliDo())
                .build();
        return calendarEvent;
    }
}