package ru.alfabank.alfamir.alfa_tv.data.source.repository.show;

import io.reactivex.Observable;
import ru.alfabank.alfamir.alfa_tv.data.dto.ShowRaw;
import ru.alfabank.alfamir.alfa_tv.data.dto.ShowUriRaw;
import ru.alfabank.alfamir.di.qualifiers.Remote;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.List;

@Singleton
public class ShowRepository implements ShowDataSource {

    private ShowDataSource mShowRemoteDataSource;
    private List<ShowRaw> mCachedShowList = null;
    private volatile boolean mIsCacheDirty = false;
    private final Object mLock = new Object();

    @Inject
    ShowRepository(@Remote ShowDataSource showDataSource) {
        mShowRemoteDataSource = showDataSource;
    }

    @Override
    public Observable<String> getVideoUrl(String videoId, String phoneIp, String password) {
        return mShowRemoteDataSource.getVideoUrl(videoId, phoneIp, password);
    }

    @Override
    public Observable<ShowUriRaw> getVideoUri(String videoId, String phoneIp, String password) {
        return mShowRemoteDataSource.getVideoUri(videoId, phoneIp, password);
    }

    @Override
    public Observable<Boolean> getCurrentVideoState(int videoId) {
        return mShowRemoteDataSource.getCurrentVideoState(videoId);
    }

    @Override
    public Observable<List<ShowRaw>> getShowList(RequestValues requestValues) {
        boolean isCacheDirty = requestValues.getIsCacheDirty();
        // check if should load from server
        if (isCacheDirty || mIsCacheDirty || mCachedShowList == null) {
            return mShowRemoteDataSource.getShowList(requestValues)
                    .flatMap(showRawList -> {
                        saveToCache(showRawList);
                        return Observable.just(showRawList);
                    });
        }
        return Observable.just(mCachedShowList);
    }

    @Override
    public Observable<ShowRaw> getShow(int id) {
        for (ShowRaw showRaw : mCachedShowList) {
            if (showRaw.getId() == id) {
                return Observable.just(showRaw);
            }
        }
        return null;
    }

    private void saveToCache(List<ShowRaw> showRawList) {
//            mCachedShowList = new ArrayList<>();
            mCachedShowList = showRawList;
            mIsCacheDirty = false;
    }
}
