package ru.alfabank.alfamir.feed_new.presentation.feed;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.alfamir.di.qualifiers.post.FeedID;
import ru.alfabank.alfamir.di.qualifiers.post.FeedType;
import ru.alfabank.alfamir.di.qualifiers.post.FeedUrl;
import ru.alfabank.alfamir.feed_new.domain.usecase.GetFeed;
import ru.alfabank.alfamir.feed_new.presentation.feed.contract.FeedFragmentContract;

import javax.inject.Inject;

public class FeedPresenter implements FeedFragmentContract.Presenter {

    private FeedFragmentContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private GetFeed mGetFeed;
    private String mFeedType;
    private String mFeedUrl;
    private String mFeedId;

    @Inject
    FeedPresenter(@FeedID String feedId,
                  @FeedUrl String feedUrl,
                  @FeedType String feedType,
                  GetFeed getFeed) {
        mGetFeed = getFeed;
        mFeedId = feedId;
        mFeedUrl = feedUrl;
        mFeedType = feedType;
    }

    @Override
    public void takeView(FeedFragmentContract.View view) {
        mView = view;
        loadFeed();
    }

    private void loadFeed() {
        mCompositeDisposable.add(
                mGetFeed.execute(new GetFeed.RequestValues(mFeedUrl, mFeedType))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(status -> {
                                },
                                Throwable::printStackTrace));
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public FeedFragmentContract.View getView() {
        return null;
    }
}
