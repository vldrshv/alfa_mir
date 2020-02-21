package ru.alfabank.alfamir.feed.data.source.remote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.dto.comment.Comment;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.feed.data.dto.FeedWithHeader;
import ru.alfabank.alfamir.feed.data.source.repository.PostDataSource;
import ru.alfabank.alfamir.feed.presentation.dto.FeedElement;
import ru.alfabank.alfamir.post.data.dto.PostRaw;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;

public class PostRemoteDataSource implements PostDataSource {

    private final WebService service;
    private LogWrapper mLog;
    private FakeDataSource mMockDs;
    private boolean isTest = false;

    @Inject
    PostRemoteDataSource(WebService service, LogWrapper logWrapper, FakeDataSource mockDs) {
        this.service = service;
        mLog = logWrapper;
        mMockDs = mockDs;
    }

    @Override
    public void likePost(String postId, String postUrl, String postType, int likeStatus, LikePostCallback callback) {
        service.likePost(postId, postUrl, postType, likeStatus, new LikePostCallback() {
            @Override
            public void onPostLiked() {
                callback.onPostLiked();
            }

            @Override
            public void onServerNotAvailable() {
                callback.onServerNotAvailable();
            }
        });
    }

    @Override
    public void getPost(String postId, String postUrl, String postType, LoadPostCallback callback) {

    }

    @Override
    public Observable<PostRaw> getPost(String postUrl) {
        String request = RequestFactory.INSTANCE.formPostRequest(postUrl);
        return service.requestX(request).
                map(JsonWrapper::getPostDeserialize);
    }

    @Override
    public Observable<PostRaw> getPost(String postId, String feedUrl, String feedType) {
        String request = RequestFactory.INSTANCE.formPostRequestNew(postId, feedUrl, feedType, "edited_html");
        return service.requestX(request).
                map(JsonWrapper::getPostDeserialize);
    }

    @Override
    public Observable<List<FeedElement>> getMainFeed(String feedId, String timeStamp, int newsCount) {
        if (isTest) {
            String json = mMockDs.getJson();
            List<FeedElement> feedElements = JsonWrapper.getPosts(json);
            return Observable.just(feedElements);
        }

        String request = RequestFactory.formMainRequestNew(feedId, timeStamp, newsCount, "edited_html");
        return service.requestX(request).map(JsonWrapper::getPosts);
    }

    @Override
    public Observable<String> getVideo(String videoId, String phoneIp) {
        String request = RequestFactory.INSTANCE.formGetTokenRequest(videoId, phoneIp, "");

        return service.requestX(request).map(response -> response);
    }

    @Override
    public Observable<FeedWithHeader> getFeed(String feedId, String feedUrl, String feedType, String timeStamp, int newsCount, boolean showFeedHeader) {
        String contentType = "edited_html";
        String request = RequestFactory.formFeedRequest(feedId, feedType, timeStamp, newsCount, showFeedHeader, contentType);
        return service.requestX(request)
                .map(JsonWrapper::getFeedWithHeader);
    }

    @Override
    public Observable<List<Comment>> getComments(String postType, String threadId, String postUrl) {
        String request = RequestFactory.INSTANCE.formGetCommentsRequest(postType, threadId, postUrl);
        return service.requestX(request).map(JsonWrapper::getComments);
    }

    @Override
    public Observable<String> likePostNew(String postId, String feedUrl, String feedType, int newUserLikeStatus) {
        String request = RequestFactory.INSTANCE.formLikePostRequest(postId, feedUrl, feedType, newUserLikeStatus);
        return service.requestX(request);
    }

    @Override
    public Observable<String> likeComment(String commentId, String postUrl, String commentType, int likeStatus) {
        String request = RequestFactory.INSTANCE.formLikeCommentRequest(commentId, postUrl, commentType, likeStatus);
        return service.requestX(request);
    }

    @Override
    public Observable<String> sendComment(String postId, String postType, String postUrl, String threadId, String parentId, String comment) {
        String request = RequestFactory.INSTANCE.formPostCommentRequestNew(postId, postType, postUrl, threadId, parentId, comment);
        return service.requestX(request);
    }

    @Override
    public Observable<String> deleteComment(String commentId, String commentType, String postUrl) {
        String request = RequestFactory.INSTANCE.formDeleteCommentRequest(commentId, commentType, postUrl);
        return service.requestX(request);
    }

    @Override
    public void getComments(String postType, String threadId, String postUrl, LoadCommentsCallback callback) {
        service.getComments(postType, threadId, postUrl, new LoadCommentsCallback() {
            @Override
            public void onCommentsLoaded(List<Comment> comments) {
                callback.onCommentsLoaded(comments);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getMainFeed(String postType, String timeStamp, int newsCount, LoadPostsCallback callback) {
    }

    @Override
    public void deletePost(String postType, String postUrl, String postId, DeletePostCallback callback) {
        service.deletePost(postType, postUrl, postId, new DeletePostCallback() {
            @Override
            public void onPostDeleted() {
                callback.onPostDeleted();
            }

            @Override
            public void onServerNotAvailable() {
                callback.onServerNotAvailable();
            }
        });
    }


}
