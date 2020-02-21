package ru.alfabank.alfamir.feed_new.data.source.remote;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.feed_new.data.dto.FeedRaw;
import ru.alfabank.alfamir.feed_new.data.source.repository.PostDataSource;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;

public class PostRemoteDataSource implements PostDataSource {

    private WebService mService;

    @Inject
    PostRemoteDataSource(WebService service){
        mService = service;
    }

    @Override
    public Observable<FeedRaw> getFeed(String feedId, String feedType, int isHeaderRequired, String timeStamp) {
        String request = RequestFactory.INSTANCE.formFeedRequestNew(feedId, feedType, isHeaderRequired, timeStamp);
        return mService.requestX(request).map(JsonWrapper::getFeed);
    }
}
