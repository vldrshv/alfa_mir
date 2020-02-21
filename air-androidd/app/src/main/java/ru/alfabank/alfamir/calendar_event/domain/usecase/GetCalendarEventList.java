package ru.alfabank.alfamir.calendar_event.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.calendar_event.data.source.repository.CalendarEventDataSource;
import ru.alfabank.alfamir.calendar_event.data.source.repository.CalendarEventRepository;
import ru.alfabank.alfamir.calendar_event.domain.mapper.CalendarEventMapper;
import ru.alfabank.alfamir.calendar_event.presentation.dto.CalendarEvent;

public class GetCalendarEventList extends UseCase<GetCalendarEventList.RequestValues, GetCalendarEventList.ResponseValue> {

    private CalendarEventRepository mCalendarEventRepository;
    private CalendarEventMapper mCalendarEventMapper;

    @Inject
    public GetCalendarEventList(CalendarEventRepository calendarEventRepository,
                                CalendarEventMapper calendarEventMapper) {
        mCalendarEventRepository = calendarEventRepository;
        mCalendarEventMapper = calendarEventMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        int eventAmount = requestValues.getNewsItemAmount();
        boolean isCacheDirty = false;
        return mCalendarEventRepository.getEvents(new CalendarEventDataSource.RequestValues(eventAmount, isCacheDirty))
                .flatMapIterable(topNewsRawList -> topNewsRawList)
                .map(mCalendarEventMapper)
                .toList()
                .flatMapObservable(calendarEventList -> Observable.just(new ResponseValue(calendarEventList)));
    }

    public static class RequestValues implements UseCase.RequestValues {
        private int mNewsItemAmount;
        public RequestValues(int newsItemAmount){
            mNewsItemAmount = newsItemAmount;
        }
        int getNewsItemAmount() {
            return mNewsItemAmount;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private List<CalendarEvent> mCalendarEventList;
        public ResponseValue(List<CalendarEvent> calendarEventList){
            mCalendarEventList = calendarEventList;
        }
        public List<CalendarEvent> getCalendarEventList() {
            return mCalendarEventList;
        }
    }
}