package ru.alfabank.alfamir.feed_new.data.source.repository;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.feed_new.data.dto.FeedRaw;

public class PostRepository implements PostDataSource {

    private PostDataSource mPostRemoteDataSource;

    @Inject
    PostRepository(@Remote PostDataSource postRemoteDataSource){
        mPostRemoteDataSource = postRemoteDataSource;
    }

    @Override
    public Observable<FeedRaw> getFeed(String feedId, String feedType, int isHeaderRequired, String timeStamp) {
        return mPostRemoteDataSource.getFeed(feedId, feedType, isHeaderRequired, timeStamp);
    }



}
