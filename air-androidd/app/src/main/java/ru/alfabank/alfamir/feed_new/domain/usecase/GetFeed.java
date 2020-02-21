package ru.alfabank.alfamir.feed_new.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.feed_new.data.source.repository.PostRepository;
import ru.alfabank.alfamir.feed_new.domain.mapper.FeedMapper;
import ru.alfabank.alfamir.feed_new.presentation.dto.DisplayableFeedItem;

public class GetFeed extends UseCase<GetFeed.RequestValues, GetFeed.ResponseValue> {
    private PostRepository mPostRepository;
    private FeedMapper mFeedMapper;

    @Inject
    public GetFeed(PostRepository postRepository,
                   FeedMapper feedMapper) {
        mPostRepository = postRepository;
        mFeedMapper = feedMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        String feedType = requestValues.getFeedType();
        String feedUrl = requestValues.getFeedUrl();
        int isHeaderRequired = 1;
        String timeStamp = "";
        return mPostRepository.getFeed(feedUrl, feedType, isHeaderRequired, timeStamp)
                .map(mFeedMapper)
                .flatMap(feed -> {
                    List<DisplayableFeedItem> displayableFeedItems = feed.getDisplayableItemList();
                    return Observable.just(new ResponseValue(displayableFeedItems));
                });
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String mFeedType;
        private String mFeedUrl;
        public RequestValues(String feedUrl, String feedType){
            mFeedType = feedType;
            mFeedUrl = feedUrl;
        }
        public String getFeedType() {
            return mFeedType;
        }
        public String getFeedUrl() {
            return mFeedUrl;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private List<DisplayableFeedItem> mDisplayableFeedItems;
        ResponseValue (List<DisplayableFeedItem> displayableFeedItems){
            mDisplayableFeedItems = displayableFeedItems;
        }
        public List<DisplayableFeedItem> getDisplayableFeedItems() {
            return mDisplayableFeedItems;
        }
    }
}