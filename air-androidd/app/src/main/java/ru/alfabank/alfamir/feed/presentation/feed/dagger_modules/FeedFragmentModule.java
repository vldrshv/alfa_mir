package ru.alfabank.alfamir.feed.presentation.feed.dagger_modules;

import com.google.common.base.Strings;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.data.source.repositories.old_trash.SubscribeRepository;
import ru.alfabank.alfamir.di.qualifiers.feed.HeaderVisibility;
import ru.alfabank.alfamir.di.qualifiers.feed.TitlesVisibility;
import ru.alfabank.alfamir.di.qualifiers.feed.ToolbarVisibility;
import ru.alfabank.alfamir.di.qualifiers.post.*;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.di.qualifiers.post.FeedID;
import ru.alfabank.alfamir.di.qualifiers.post.FeedTitle;
import ru.alfabank.alfamir.di.qualifiers.post.FeedType;
import ru.alfabank.alfamir.di.qualifiers.post.FeedUrl;
import ru.alfabank.alfamir.di.qualifiers.post.PostCreationEnabled;
import ru.alfabank.alfamir.feed.presentation.feed.FeedFragment;
import ru.alfabank.alfamir.feed.presentation.feed.FeedPresenter;
import ru.alfabank.alfamir.feed.presentation.feed.contract.FeedContract;

@Module
public abstract class FeedFragmentModule {

    @FragmentScoped
    @Binds
    abstract FeedContract.Presenter feedPresenter (FeedPresenter presenter);

    @Provides
    @FragmentScoped
    static SubscribeRepository provideSubscribeRepository(App app) {
        return app.getSubscribeRepositoryNew();
    }

    @Provides
    @FragmentScoped
    @FeedType
    static String provideFeedType(FeedFragment fragment) {
        String feedType = fragment.getActivity().getIntent().getStringExtra(Constants.Post.FEED_TYPE);
        if(Strings.isNullOrEmpty(feedType)) return "";
        return feedType;
    }

    @Provides
    @FragmentScoped
    @FeedID
    static String provideFeedId(FeedFragment fragment) {
        String feedType = fragment.getActivity().getIntent().getStringExtra(Constants.Post.FEED_ID);
        if(Strings.isNullOrEmpty(feedType)) return "";
        return feedType;
    }

    @Provides
    @FragmentScoped
    @FeedUrl
    static String provideFeedUrl(FeedFragment fragment) {
        String feedType = fragment.getActivity().getIntent().getStringExtra(Constants.Post.FEED_URL);
        if(Strings.isNullOrEmpty(feedType)) return "";
        return feedType;
    }

    @Provides
    @FragmentScoped
    @FeedTitle
    static String provideFeedTitle(FeedFragment fragment) {
        String feedType = fragment.getActivity().getIntent().getStringExtra(Constants.Post.FEED_TITLE);
        if(Strings.isNullOrEmpty(feedType)) return "";
        return feedType;
    }

    @Provides
    @FragmentScoped
    @PostCreationEnabled
    static boolean providePostCreationEnabled(FeedFragment fragment) {
        boolean postCreationEnabled = fragment.getActivity().getIntent().getBooleanExtra(Constants.Post.POST_CREATION_ENABLED, false);
        return postCreationEnabled;
    }

    @Provides
    @FragmentScoped
    @ToolbarVisibility
    static boolean provideToolbarVisibility() {
        return true;
    }

    @Provides
    @FragmentScoped
    @HeaderVisibility
    static boolean provideHeaderVisibility() {
        return true;
    }


    @Provides
    @FragmentScoped
    @TitlesVisibility
    static boolean provideTitlesVisibility() {
        return true;
    }

}
