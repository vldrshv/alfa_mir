package ru.alfabank.alfamir.main.home_fragment.data.source.repository;

import io.reactivex.Observable;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.main.home_fragment.data.dto.TopNewsRaw;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class TopNewsRepository implements TopNewsDataSource {

    private TopNewsDataSource mHomeRemoteDataSource;
    private Object mLock = new Object();
    private List<TopNewsRaw> mTopNewsRawCache;

    @Inject
    TopNewsRepository(@Remote TopNewsDataSource homeRemoteDataSource){
        mHomeRemoteDataSource = homeRemoteDataSource;
    }

    @Override
    public Observable<List<TopNewsRaw>> getTopNews(RequestValues requestValues) {
        boolean isCacheDirty = requestValues.getIsCacheDirty();
        if(isCacheDirty || mTopNewsRawCache == null){
            return mHomeRemoteDataSource.getTopNews(requestValues)
                    .flatMap(topNewsRawList -> {
                        saveToCache(topNewsRawList);
                        return Observable.just(topNewsRawList);
                    });
        }
        return Observable.just(mTopNewsRawCache);
    }

    private void saveToCache(List<TopNewsRaw> topNewsRawList) {
        synchronized (mLock){
            if(mTopNewsRawCache == null){
                mTopNewsRawCache = new ArrayList<>();
            } else {
                mTopNewsRawCache.clear();
            }
            mTopNewsRawCache = topNewsRawList;
        }
    }

}
