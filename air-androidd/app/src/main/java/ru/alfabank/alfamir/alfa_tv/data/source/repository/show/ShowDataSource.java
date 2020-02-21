package ru.alfabank.alfamir.alfa_tv.data.source.repository.show;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import ru.alfabank.alfamir.alfa_tv.data.dto.ShowRaw;
import ru.alfabank.alfamir.alfa_tv.data.dto.ShowUriRaw;

public interface ShowDataSource {

    Observable<List<ShowRaw>> getShowList(RequestValues requestValues);

    Observable<ShowRaw> getShow(int id);

    Observable<String> getVideoUrl(String videoId, String phoneIp, String password);

    Observable<ShowUriRaw> getVideoUri(String videoId, String phoneIp, String password);

    Observable<Boolean> getCurrentVideoState(int videoId);


    class RequestValues {
        private boolean mIsCacheDirty;
        public RequestValues(boolean isCacheDirty){
            mIsCacheDirty = isCacheDirty;
        }
        public boolean getIsCacheDirty() {
            return mIsCacheDirty;
        }
    }

}
