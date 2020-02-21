package ru.alfabank.alfamir.feed_new.presentation.feed.dagger_module;

import com.google.common.base.Strings;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.App;
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
import ru.alfabank.alfamir.feed_new.presentation.feed.FeedPresenter;
import ru.alfabank.alfamir.feed_new.presentation.feed.contract.FeedFragmentContract;

;

@Module
public abstract class FeedFragmentModule {

    @FragmentScoped
    @Binds
    abstract FeedFragmentContract.Presenter peoplePresenter(FeedPresenter presenter);

    @Provides
    @FragmentScoped
    static SubscribeRepository provideSubscribeRepository(App app) {
        return app.getSubscribeRepositoryNew();
    }

    @Provides
    @FragmentScoped
    @FeedType
    static String provideFeedType() {
        String feedType = "blog";
        if(Strings.isNullOrEmpty(feedType)) return "";
        return feedType;
    }

    @Provides
    @FragmentScoped
    @FeedID
    static String provideFeedId() {
        String feedType = "http://alfa/Info/photostream";
        if(Strings.isNullOrEmpty(feedType)) return "";
        return feedType;
    }

    @Provides
    @FragmentScoped
    @FeedUrl
    static String provideFeedUrl() {
        String feedType = "http://alfa/Info/photostream";
        if(Strings.isNullOrEmpty(feedType)) return "";
        return feedType;
    }

    @Provides
    @FragmentScoped
    @FeedTitle
    static String provideFeedTitle() {
        String feedType = "haha";
        if(Strings.isNullOrEmpty(feedType)) return "";
        return feedType;
    }

    @Provides
    @FragmentScoped
    @ToolbarVisibility
    static boolean provideToolbarVisibility() {
        return false;
    }

    @Provides
    @FragmentScoped
    @HeaderVisibility
    static boolean provideHeaderVisibility() {
        return false;
    }

//    HeaderVisibility

    @Provides
    @FragmentScoped
    @PostCreationEnabled
    static boolean providePostCreationEnabled() {
        return true;
    }

    @Provides
    @FragmentScoped
    @TitlesVisibility
    static boolean provideTitlesVisibility() {
        return false;
    }
}