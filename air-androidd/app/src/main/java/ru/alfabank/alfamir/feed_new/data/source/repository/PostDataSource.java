package ru.alfabank.alfamir.feed_new.data.source.repository;

import io.reactivex.Observable;
import ru.alfabank.alfamir.feed_new.data.dto.FeedRaw;

public interface PostDataSource {

    Observable<FeedRaw> getFeed(String feedId, String feedType, int isHeaderRequired, String timeStamp);

}
