package ru.alfabank.alfamir.feed.data.source.repository;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import ru.alfabank.alfamir.data.dto.comment.Comment;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.feed.data.dto.FeedWithHeader;
import ru.alfabank.alfamir.feed.data.dto.HeaderRawOld;
import ru.alfabank.alfamir.feed.presentation.dto.FeedElement;
import ru.alfabank.alfamir.post.data.dto.Post;
import ru.alfabank.alfamir.post.data.dto.PostInterface;
import ru.alfabank.alfamir.post.data.dto.PostRaw;
import ru.alfabank.alfamir.post.presentation.dto.PostElement;
import ru.alfabank.alfamir.utility.converter.PostElementsConverter;
import ru.alfabank.alfamir.utility.static_utilities.DateConverter;

import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_0;
import static ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_GREENWICH;


@Singleton
public class PostRepository implements PostDataSource {

    private final PostDataSource mPostRemoteDataSource;
    private PostElementsConverter mConverter;
    private Map<String, List<FeedElement>> mCachedFeeds = new HashMap<>();
//    private Map<String, Boolean> mCachesAreDirty = new HashMap<>();

    @Inject
    PostRepository(@Remote PostDataSource postRemoteDataSource, PostElementsConverter converter) {
        mPostRemoteDataSource = postRemoteDataSource;
        mConverter = converter;
    }

    @Override
    public Observable<List<FeedElement>> getMainFeed(String feedId, String timeStamp, int newsCount) {
        if (Strings.isNullOrEmpty(timeStamp)) mCachedFeeds.remove(feedId);
        return mPostRemoteDataSource.getMainFeed(feedId, timeStamp, newsCount)
                .flatMap(posts -> {
                    saveToCache(feedId, posts);
                    return Observable.just(posts);
                });
    }

    public Observable<List<FeedElement>> getFeedNew(String feedId, String feedUrl, String feedType, String timeStamp, int newsCount, boolean showFeedHeader) {
        if (Strings.isNullOrEmpty(timeStamp)) mCachedFeeds.remove(feedId);
        return mPostRemoteDataSource.getFeed(feedId, feedUrl, feedType, timeStamp, newsCount, showFeedHeader)
                .flatMap(feedWithHeader -> {
                    List<FeedElement> feed = new ArrayList<>();
                    HeaderRawOld header = feedWithHeader.getHeader();
                    if (header != null) feed.add(header);
                    List<PostRaw> posts = feedWithHeader.getPosts();
                    feed.addAll(posts);
                    saveToCache(feedId, feed);
                    return Observable.just(feed);
                });
    }

    public Completable likePostTest(String postId, String feedUrl, String feedType, int newUserLikeStatus, String feedId) {
        return Completable.fromObservable(mPostRemoteDataSource.likePostNew(postId, feedUrl, feedType, newUserLikeStatus).flatMap(json -> {
            boolean feedCachedWithProperId = mCachedFeeds.containsKey(feedId);

            if (feedCachedWithProperId) {
                List<FeedElement> feed = mCachedFeeds.get(feedId);
                for (FeedElement feedElement : feed) {
                    if (feedElement instanceof PostRaw) {
                        PostRaw post = (PostRaw) feedElement;
                        if (post.getId().equals(postId)) {
                            post.setCurrentUserLike(newUserLikeStatus);
                            if (newUserLikeStatus == 0) {
                                post.setLikes(post.getLikes() - 1);
                            } else if (newUserLikeStatus == 1) {
                                post.setLikes(post.getLikes() + 1);
                            }
                        }
                    }
                }
            } else {
                for (Map.Entry<String, List<FeedElement>> feeds : mCachedFeeds.entrySet()) {
                    List<FeedElement> feed = feeds.getValue();
                    for (FeedElement feedElement : feed) {
                        if (feedElement instanceof PostRaw) {
                            PostRaw post = (PostRaw) feedElement;
                            if (post.getId().equals(postId)) {
                                post.setCurrentUserLike(newUserLikeStatus);
                                if (newUserLikeStatus == 0) {
                                    post.setLikes(post.getLikes() - 1);
                                } else if (newUserLikeStatus == 1) {
                                    post.setLikes(post.getLikes() + 1);
                                }
                            }
                        }
                    }
                }
            }
            return Observable.just(json);
        }));
    }

    public Observable<Post> getPostNew(String postUrl) {
        return loadPostFromRemoteDataSource(postUrl).flatMap(post -> convertPost(post));
    }

    public Observable<Post> getPostNew(String feedId, String postId, String feedUrl, String feedType) {
//        if (mCacheIsDirty) {
//            return loadPostFromRemoteDataSource(feedId, postId, feedUrl, feedType).flatMap(post -> convertPost(post)); // if refresh is requested
//        }

        boolean isCacheEmpty = mCachedFeeds.isEmpty();
        boolean feedCached = mCachedFeeds.containsKey(feedId);
        if (!isCacheEmpty && feedCached) {
            List<FeedElement> feed = mCachedFeeds.get(feedId);
            for (FeedElement element : feed) {
                if (element instanceof PostRaw) {
                    PostRaw post = (PostRaw) element;
                    if (post.getId().equals(postId)) {
                        return convertPost(post);
                    }
                }
            }
        } else if (!isCacheEmpty) {
            for (Map.Entry<String, List<FeedElement>> feeds : mCachedFeeds.entrySet()) {
                List<FeedElement> feed = feeds.getValue();
                for (FeedElement feedElement : feed) {
                    if (feedElement instanceof PostRaw) {
                        PostRaw post = (PostRaw) feedElement;
                        if (post.getId().equals(postId)) {
                            return convertPost(post);
                        }
                    }
                }
            }
        }
        return loadPostFromRemoteDataSource(feedId, postId, feedUrl, feedType).flatMap(post -> convertPost(post));
    }

    public Observable<List<PostElement>> getCommentsNew(String postType, String threadId, String postUrl) {
        return mPostRemoteDataSource.getComments(postType, threadId, postUrl).flatMap(comments -> {
            List<PostElement> elements = mConverter.getCommentElements(comments);
            return Observable.just(elements);
        });
    }

    @Override // dead
    public Observable<PostRaw> getPost(String postUrl, String postId, String postType) {

        return null;
    }

    private Observable<PostRaw> loadPostFromRemoteDataSource(String feedId, String postId, String feedUrl, String feedType) {
        return mPostRemoteDataSource.getPost(postId, feedUrl, feedType)
                .flatMap(post -> {
                    return Observable.just(post);
                });
    }

    private Observable<PostRaw> loadPostFromRemoteDataSource(String postUrl) {
        return mPostRemoteDataSource.getPost(postUrl)
                .flatMap(post -> Observable.just(post));
    }

    @Override
    public Observable<FeedWithHeader> getFeed(String feedId, String feedUrl, String feedType, String timeStamp, int newsCount, boolean showFeedHeader) {

        return mPostRemoteDataSource.getFeed(feedId, feedUrl, feedType, timeStamp, newsCount, showFeedHeader)
                .flatMap(posts -> {
                    return Observable.just(posts);
                });
    }

    @Override
    public Observable<List<Comment>> getComments(String postType, String threadId, String postUrl) {
        return mPostRemoteDataSource.getComments(postType, threadId, postUrl);
    }

    @Override
    public Observable<String> likePostNew(String postId, String postUrl, String postType, int likeStatus) {
        return mPostRemoteDataSource.likePostNew(postId, postUrl, postType, likeStatus);
    }

    @Override
    public Observable<String> likeComment(String commentId, String postUrl, String commentType, int likeStatus) {
        return mPostRemoteDataSource.likeComment(commentId, postUrl, commentType, likeStatus);
    }

    @Override
    public Observable<String> sendComment(String postId, String postType, String postUrl, String threadId, String parentId, String comment) {
        return mPostRemoteDataSource.sendComment(postId, postType, postUrl, threadId, parentId, comment);
    }

    @Override
    public Observable<String> deleteComment(String commentId, String commentType, String postUrl) {
        return mPostRemoteDataSource.deleteComment(commentId, commentType, postUrl);
    }

    @Override
    public void likePost(String postId, String postUrl, String postType, int likeStatus, LikePostCallback callback) {
        mPostRemoteDataSource.likePost(postId, postUrl, postType, likeStatus, new LikePostCallback() {
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
        mPostRemoteDataSource.getPost(postId, postUrl, postType, new LoadPostCallback() {
            @Override
            public void onPostLoaded(PostRaw postRaw) {
                callback.onPostLoaded(postRaw);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getComments(String postType, String threadId, String postUrl, LoadCommentsCallback callback) {
        mPostRemoteDataSource.getComments(postType, threadId, postUrl, new LoadCommentsCallback() {
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
    public void getMainFeed(String feedId, String timeStamp, int newsCount, LoadPostsCallback callback) {
        getPostsFromRemoteDataSource(feedId, timeStamp, newsCount, callback);
    }

    @Override
    public void deletePost(String postType, String postUrl, String postId, DeletePostCallback callback) {
        mPostRemoteDataSource.deletePost(postType, postUrl, postId, new DeletePostCallback() {
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

    @Override
    public Observable<PostRaw> getPost(String postUrl) {
        return mPostRemoteDataSource.getPost(postUrl);
    }

    private void getPostsFromRemoteDataSource(String feedId, String timeStamp, int newsCount, LoadPostsCallback callback) {
        mPostRemoteDataSource.getMainFeed(feedId, timeStamp, newsCount, new LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<FeedElement> feed) {
                saveToCache(feedId, feed);
                callback.onPostsLoaded(feed);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public Observable<String> getVideo(String videoId, String phoneIp) {
        return mPostRemoteDataSource.getVideo(videoId, phoneIp);
    }

    public void dropCache(String feedId) {
        mCachedFeeds.remove(feedId);
    }

    public void deleteCachedPostAtPosition(String postType, int position) {
        mCachedFeeds.get(postType).remove(position);
    }

    private void saveToCache(String feedId, List<FeedElement> feed) {
        if (mCachedFeeds.containsKey(feedId)) {
            List<FeedElement> cachedFeed = mCachedFeeds.get(feedId);
            cachedFeed.addAll(feed);
        } else mCachedFeeds.put(feedId, feed);
    }

    public String getLastPostPubTimeMinusOneSecond(String feedId) {
        if (mCachedFeeds.containsKey(feedId)) {
            List<FeedElement> feed = mCachedFeeds.get(feedId);
            FeedElement feedElement = feed.get(feed.size() - 1);
            PostRaw post = (PostRaw) feedElement;
            String lastItemTime = post.getPubTime();
            long milliseconds = DateConverter.convertToMillis(lastItemTime, DATE_PATTERN_0, TIME_ZONE_GREENWICH);
            milliseconds = milliseconds - 1000;
            lastItemTime = DateConverter.convertToUtcDate(milliseconds, DATE_PATTERN_0);
            return lastItemTime;
        } else {
            return null;
        }
    }

    public FeedElement getCachedPostAtPosition(String feedId, int position) {
        if (mCachedFeeds.containsKey(feedId) && position < mCachedFeeds.get(feedId).size()) {
            List<FeedElement> feed = mCachedFeeds.get(feedId);
            FeedElement feedElement = feed.get(position);
            return feedElement;
        } else {
            return null;
        }
    }

    public int getCachedPostsListSize(String feedId) {
        if (mCachedFeeds.containsKey(feedId)) {
            List<FeedElement> feed = mCachedFeeds.get(feedId);
            int size = feed.size();
            return size;
        } else {
            return -1;
        }
    }

    public int getItemViewType(String feedId, int position) {
        if (mCachedFeeds.containsKey(feedId)) {
            List<FeedElement> elements = mCachedFeeds.get(feedId);
            int type = elements.get(position).getViewType();
            return type;
        } else {
            return -1;
        }
    }

    public void refreshItem() {
    }

    // convert post to list of elements (post.presentation.dto)
    private Observable<Post> convertPost(PostRaw post) {
        String id = post.getId();
        String threadId = post.getThreadId();
        String type = post.getType();
        String url = post.getPostUrl();
        int currentUserLike = post.getCurrentUserLike();
        int likesCount = post.getLikes();
        int commentsCount = post.getCommentsCount();
        int commentsEnabled = post.isCommentable();
        List<PostElement> elements = mConverter.getPostElements(post);
        List<PostInterface.PostImage> postImages = new ArrayList<>();
        List<PostInterface.PostVideo> postVideos = new ArrayList<>();
        List<PostInterface.PostUri> postUris = new ArrayList<>();
        PostInterface.InteractionElement interactionElements = post.getInteractionElement();
        if (interactionElements != null) {
            PostInterface.PostImage[] images = interactionElements.getImages();
            if (images != null) postImages = Arrays.asList(images);
            PostInterface.PostVideo[] videos = interactionElements.getVideos();
            if (videos != null) postVideos = Arrays.asList(videos);
            PostInterface.PostUri[] uris = interactionElements.getUris();
            if (uris != null) postUris = Arrays.asList(uris);
        }

        Post postNew = new Post(id, threadId, type, url, currentUserLike, likesCount, commentsCount, commentsEnabled, elements, postImages, postVideos, postUris);
        return Observable.just(postNew);
    }

}
