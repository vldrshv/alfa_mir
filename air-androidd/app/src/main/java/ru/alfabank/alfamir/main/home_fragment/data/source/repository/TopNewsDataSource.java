package ru.alfabank.alfamir.main.home_fragment.data.source.repository;

import java.util.List;

import io.reactivex.Observable;
import ru.alfabank.alfamir.main.home_fragment.data.dto.TopNewsRaw;

public interface TopNewsDataSource {

    Observable<List<TopNewsRaw>> getTopNews(RequestValues requestValues);

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
