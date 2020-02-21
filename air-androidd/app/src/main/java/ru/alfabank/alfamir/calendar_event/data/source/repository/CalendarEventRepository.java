package ru.alfabank.alfamir.calendar_event.data.source.repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.calendar_event.data.dto.CalendarEventRaw;

@Singleton
public class CalendarEventRepository implements CalendarEventDataSource {

    private CalendarEventDataSource mCalendarEventRemoteDataSource;
    private Object mLock = new Object();
    private List<CalendarEventRaw> mCalendarEventRawCache;

    @Inject
    CalendarEventRepository(@Remote CalendarEventDataSource calendarEventRemoteDataSource){
        mCalendarEventRemoteDataSource = calendarEventRemoteDataSource;
    }

    @Override
    public Observable<List<CalendarEventRaw>> getEvents(RequestValues requestValues) {
        boolean isCacheDirty = requestValues.getIsCacheDirty();

        if(isCacheDirty || mCalendarEventRawCache == null){
//            if(mCalendarEventRawCache != null) mCalendarEventRawCache.clear();
            return mCalendarEventRemoteDataSource.getEvents(requestValues)
                    .flatMap(calendarEventRawList -> {
                        saveToCache(calendarEventRawList);
                        return Observable.just(calendarEventRawList);
                    });
        }

        return Observable.just(mCalendarEventRawCache);
    }

    private void saveToCache(List<CalendarEventRaw> calendarEventRawList) {
        synchronized (mLock){
            if(mCalendarEventRawCache == null){
                mCalendarEventRawCache = new ArrayList<>();
            } else {
                mCalendarEventRawCache.clear();
            }
            mCalendarEventRawCache = calendarEventRawList;
        }
    }
}
