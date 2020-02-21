package ru.alfabank.alfamir.feed.presentation.feed_with_header;

import ru.alfabank.alfamir.data.source.repositories.old_trash.SubscribeRepository;
import ru.alfabank.alfamir.di.qualifiers.feed.HeaderVisibility;
import ru.alfabank.alfamir.di.qualifiers.feed.TitlesVisibility;
import ru.alfabank.alfamir.di.qualifiers.post.*;
import ru.alfabank.alfamir.di.qualifiers.post.FeedID;
import ru.alfabank.alfamir.di.qualifiers.post.FeedType;
import ru.alfabank.alfamir.di.qualifiers.post.FeedUrl;
import ru.alfabank.alfamir.di.qualifiers.post.PostCreationEnabled;
import ru.alfabank.alfamir.favorites.data.source.repository.FavoriteRepository;
import ru.alfabank.alfamir.feed.data.source.repository.PostRepository;
import ru.alfabank.alfamir.feed.presentation.feed.FeedPresenter;
import ru.alfabank.alfamir.di.qualifiers.post.FeedTitle;
import ru.alfabank.alfamir.image.domain.usecase.GetImage;
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract;

import javax.inject.Inject;

public class FeedWithHeaderPresenter extends FeedPresenter implements FeedWithHeaderContract.Presenter {

    private String mFeedTitle;

    @Inject
    FeedWithHeaderPresenter(PostRepository postRepository,
                            GetImage getImage,
                            FavoriteRepository favoriteRepository,
                            SubscribeRepository subscribeRepository,
                            LoggerContract.Provider logger,
                            @FeedID String feedId,
                            @FeedUrl String feedUrl,
                            @FeedType String feedType,
                            @FeedTitle String feedTitle,
                            @TitlesVisibility boolean showTitles,
                            @HeaderVisibility boolean showFeedHeader,
                            @PostCreationEnabled boolean postCreationEnabled){
        super(
                postRepository,
                getImage,
                favoriteRepository,
                subscribeRepository,
                logger,
                feedId,
                feedUrl,
                feedType,
                showFeedHeader);
        mFeedTitle = feedTitle;
    }

    @Override
    public void takeView(FeedWithHeaderContract.View view) {
        super.takeView(view);
        view.setToolbarTitle(mFeedTitle);
    }

    @Override
    public void onOptionsClicked() {

    }
}

