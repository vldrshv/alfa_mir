package ru.alfabank.alfamir.post.presentation.post.dagger_module;

import com.google.common.base.Strings;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.di.qualifiers.post.*;
import ru.alfabank.alfamir.di.qualifiers.post.CommentsFirst;
import ru.alfabank.alfamir.di.qualifiers.post.FeedType;
import ru.alfabank.alfamir.di.qualifiers.post.FeedUrl;
import ru.alfabank.alfamir.di.qualifiers.post.PostType;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;
import ru.alfabank.alfamir.di.qualifiers.post.CommentId;
import ru.alfabank.alfamir.di.qualifiers.post.PostId;
import ru.alfabank.alfamir.di.qualifiers.post.PostPosition;
import ru.alfabank.alfamir.di.qualifiers.post.PostUrl;
import ru.alfabank.alfamir.post.presentation.post.PostActivity;
import ru.alfabank.alfamir.post.presentation.post.PostPresenter;
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostContract;

@Module
public abstract class PostActivityModule {

    @ActivityScoped
    @Binds
    abstract PostContract.Presenter peoplePresenter(PostPresenter presenter);

    @Provides
    @ActivityScoped
    @FeedType
    static int provideFeedType(PostActivity activity) {
        return activity.getIntent().getIntExtra(Constants.Post.FEED_TYPE, -1);
    }

    @Provides
    @ActivityScoped
    @CommentsFirst
    static boolean provideHasComments(PostActivity activity) {
        return activity.getIntent().getBooleanExtra(Constants.Post.COMMENTS_FIRST, false);
    }

    @Provides
    @ActivityScoped
    @PostPosition
    static int providePostPosition(PostActivity activity) {
        return activity.getIntent().getIntExtra(Constants.Post.POST_POSITION, -1);
    }

    @Provides
    @ActivityScoped
    @PostId
    static String providePostId(PostActivity activity) {
        String postId = activity.getIntent().getStringExtra(Constants.Post.POST_ID);
        if (Strings.isNullOrEmpty(postId)) return "";
        return postId;
    }

    @Provides
    @ActivityScoped
    @PostType
    static String providePostType(PostActivity activity) {
        String postType = activity.getIntent().getStringExtra(Constants.Post.POST_TYPE);
        if (Strings.isNullOrEmpty(postType)) return "";
        return postType;
    }


    @Provides
    @ActivityScoped
    @FeedUrl
    static String provideFeedUrl(PostActivity activity) {
        String postUrl = activity.getIntent().getStringExtra(Constants.Post.FEED_URL);
        if (Strings.isNullOrEmpty(postUrl)) return "";
        return postUrl;
    }

    @Provides
    @ActivityScoped
    @CommentId
    static String provideCommentId(PostActivity activity) {
        String commentId = activity.getIntent().getStringExtra(Constants.Post.COMMENT_ID);
        if (Strings.isNullOrEmpty(commentId)) return "";
        return commentId;
    }

    @Provides
    @ActivityScoped
    @PostUrl
    static String providePostUrl(PostActivity activity) {
        String postUrl = activity.getIntent().getStringExtra(Constants.Post.POST_URL);
        if (Strings.isNullOrEmpty(postUrl)) return "";
        return postUrl;
    }

}
