package ru.alfabank.alfamir.calendar_event.data.source.remote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.calendar_event.data.dto.CalendarEventRaw;
import ru.alfabank.alfamir.calendar_event.data.source.repository.CalendarEventDataSource;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;

public class CalendarEventRemoteDataSource implements CalendarEventDataSource {

    private WebService mWebService;

    @Inject
    CalendarEventRemoteDataSource(WebService webService) {
        mWebService = webService;
    }

    @Override
    public Observable<List<CalendarEventRaw>> getEvents(RequestValues requestValues) {
        int amount = requestValues.getEventAmount();
        String request = RequestFactory.INSTANCE.formGetCalendarEventRequest(amount);
        return mWebService.requestX(request)
                .map(JsonWrapper::calendarEventList);
    }
}
