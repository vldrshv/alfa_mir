package ru.alfabank.alfamir.feed.data.source.repository;

import java.util.List;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.dto.comment.Comment;
import ru.alfabank.alfamir.feed.data.dto.FeedWithHeader;
import ru.alfabank.alfamir.feed.presentation.dto.FeedElement;
import ru.alfabank.alfamir.post.data.dto.PostRaw;

public interface PostDataSource {

    /**
     * new version of repo api
     */

    Observable<PostRaw> getPost(String postUrl);

    Observable<PostRaw> getPost(String postId, String feedUrl, String feedType);

    Observable<List<FeedElement>> getMainFeed(String feedId, String timeStamp, int newsCount);

    Observable<FeedWithHeader> getFeed(String feedId, String feedUrl, String feedType, String timeStamp, int newsCount, boolean showFeedHeader);

    Observable<List<Comment>> getComments(String postType, String threadId, String postUrl);

    Observable<String> likePostNew(String postId, String feedUrl, String feedType, int newUserLikeStatus);

    Observable<String> likeComment(String commentId, String postUrl, String commentType, int likeStatus);

    Observable<String> sendComment(String postId, String postType, String postUrl, String threadId, String parentId, String comment);

    Observable<String> deleteComment(String commentId, String commentType, String postUrl);

    Observable<String> getVideo(String videoId, String phoneIp);

    /**
     * old version of repo api
     */

    interface LoadPostCallback {
        void onPostLoaded(PostRaw postRaw);

        void onDataNotAvailable();
    }

    interface LoadCommentsCallback {
        void onCommentsLoaded(List<Comment> comments);

        void onDataNotAvailable();
    }

    interface LoadPostsCallback {
        void onPostsLoaded(List<FeedElement> post);

        void onDataNotAvailable();
    }

    interface DeletePostCallback {
        void onPostDeleted();

        default void onServerNotAvailable() {
        }
    }

    interface LikePostCallback {
        void onPostLiked();

        void onServerNotAvailable();
    }

    void likePost(String postId, String postUrl, String postType, int likeStatus, LikePostCallback callback);

    void getPost(String postId, String postUrl, String postType, LoadPostCallback callback);

    void getComments(String postType, String threadId, String postUrl, LoadCommentsCallback callback);

    void getMainFeed(String postType, String timeStamp, int newsCount, LoadPostsCallback callback);

    void deletePost(String postType, String postUrl, String postId, DeletePostCallback callback);

}
