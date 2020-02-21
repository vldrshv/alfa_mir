package ru.alfabank.alfamir.calendar_event.data.source.repository;

import java.util.List;

import io.reactivex.Observable;
import ru.alfabank.alfamir.calendar_event.data.dto.CalendarEventRaw;

public interface CalendarEventDataSource {

    Observable<List<CalendarEventRaw>> getEvents(RequestValues requestValues);

    class RequestValues {
        private boolean mIsCacheDirty;
        private int mEventAmount;
        public RequestValues(int eventAmount, boolean isCacheDirty){
            mEventAmount = eventAmount;
            mIsCacheDirty = isCacheDirty;
        }
        public boolean getIsCacheDirty() {
            return mIsCacheDirty;
        }
        public int getEventAmount() {
            return mEventAmount;
        }
    }

}
