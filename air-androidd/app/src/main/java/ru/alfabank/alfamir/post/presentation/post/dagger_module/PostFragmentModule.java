package ru.alfabank.alfamir.post.presentation.post.dagger_module;

import android.content.Intent;

import com.google.common.base.Strings;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.data.source.repositories.old_trash.SubscribeRepository;
import ru.alfabank.alfamir.di.qualifiers.feed.TitlesVisibility;
import ru.alfabank.alfamir.di.qualifiers.post.CommentId;
import ru.alfabank.alfamir.di.qualifiers.post.CommentsFirst;
import ru.alfabank.alfamir.di.qualifiers.post.FeedID;
import ru.alfabank.alfamir.di.qualifiers.post.FeedType;
import ru.alfabank.alfamir.di.qualifiers.post.FeedUrl;
import ru.alfabank.alfamir.di.qualifiers.post.PostId;
import ru.alfabank.alfamir.di.qualifiers.post.PostPosition;
import ru.alfabank.alfamir.di.qualifiers.post.PostUrl;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.post.presentation.post.PostFragment;
import ru.alfabank.alfamir.post.presentation.post.PostPresenter;
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostContract;

@Module
public abstract class PostFragmentModule {

    @FragmentScoped
    @Binds
    abstract PostContract.Presenter peoplePresenter(PostPresenter presenter);

    @Provides
    @FragmentScoped
    static SubscribeRepository provideSubscribeRepository(App app) {
        return app.getSubscribeRepositoryNew();
    }

    @Provides
    @FragmentScoped
    @PostId
    static String providePostId(PostFragment fragment) {
        String postId = fragment.getActivity().getIntent().getStringExtra(Constants.Post.POST_ID);
        if(Strings.isNullOrEmpty(postId)) return "";
        return postId;
    }

    @Provides
    @FragmentScoped
    @FeedID
    static String provideFeedId(PostFragment fragment) {
        String postType = fragment.getActivity().getIntent().getStringExtra(Constants.Post.FEED_ID);
        if(Strings.isNullOrEmpty(postType)) return "";
        return postType;
    }

    @Provides
    @FragmentScoped
    @FeedType
    static String provideFeedType(PostFragment fragment) {
        Intent intent = fragment.getActivity().getIntent();
        String feedType = intent.getStringExtra(Constants.Post.FEED_TYPE);

        if(Strings.isNullOrEmpty(feedType)){
            feedType = intent.getDataString();
        }

        return Strings.isNullOrEmpty(feedType)? "" : feedType;
    }

    @Provides
    @FragmentScoped
    @FeedUrl
    static String provideFeedUrl(PostFragment fragment) {
        String postUrl = fragment.getActivity().getIntent().getStringExtra(Constants.Post.FEED_URL);
        if(Strings.isNullOrEmpty(postUrl)) return "";
        return postUrl;
    }

    @Provides
    @FragmentScoped
    @TitlesVisibility
    static boolean provideTitlesVisibility(PostFragment fragment) {
        String feedId = fragment.getActivity().getIntent().getStringExtra(Constants.Post.FEED_ID);
        if(feedId != null && feedId.equals("http://alfa/Info/photostream")) return false;
        return true;
    }

    @Provides
    @FragmentScoped
    @PostPosition
    static int providePostPosition(PostFragment fragment) {
        return fragment.getActivity().getIntent().getIntExtra(Constants.Post.POST_POSITION, -1);
    }

    @Provides
    @FragmentScoped
    @CommentsFirst
    static boolean provideHasComments(PostFragment fragment) {
        return fragment.getActivity().getIntent().getBooleanExtra(Constants.Post.COMMENTS_FIRST, false);
    }

    @Provides
    @FragmentScoped
    @CommentId
    static String provideCommentId(PostFragment fragment) {
        String commentId = fragment.getActivity().getIntent().getStringExtra(Constants.Post.COMMENT_ID);
        if(Strings.isNullOrEmpty(commentId)) return "";
        return commentId;
    }

    @Provides
    @FragmentScoped
    @PostUrl
    static String providePostUrl(PostFragment fragment) {
        String postUrl = fragment.getActivity().getIntent().getStringExtra(Constants.Post.POST_URL);
        Intent intent = fragment.getActivity().getIntent();
        if(Strings.isNullOrEmpty(postUrl)) return "";
        return postUrl;
    }



}
