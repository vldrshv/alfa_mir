package ru.alfabank.alfamir.post.data.dto;

import java.util.List;

import ru.alfabank.alfamir.post.presentation.dto.PostElement;

public class Post {

    private String mId;
    private String mType;
    private String mUrl;
    private String mThreadId;
    private int mCurrentUserLike;
    private int mLikesCount;
    private int mCommentsCount;
    private int mCommentsEnabled;
    private List<PostElement> mPostElementList;
    private List<PostInterface.PostImage> mPostImages;
    private List<PostInterface.PostVideo> mPostVideo;
    private List<PostInterface.PostUri> mPostUri;
    public Post(String id,
                String threadId,
                String type,
                String url,
                int currentUserLike,
                int likesCount,
                int commentsCount,
                int commentsEnabled,
                List<PostElement> postElements,
                List<PostInterface.PostImage> postImages,
                List<PostInterface.PostVideo> postVideo,
                List<PostInterface.PostUri> postUri){
        mId = id;
        mThreadId = threadId;
        mType = type;
        mUrl = url;
        mCurrentUserLike = currentUserLike;
        mLikesCount = likesCount;
        mCommentsCount = commentsCount;
        mCommentsEnabled = commentsEnabled;
        mPostElementList = postElements;
        mPostImages = postImages;
        mPostVideo = postVideo;
        mPostUri = postUri;
    }

    public String getId() {
        return mId;
    }

    public String getThreadId() {
        return mThreadId;
    }

    public String getType() {
        return mType;
    }

    public String getUrl() {
        return mUrl;
    }

    public int getCommentsCount() {
        return mCommentsCount;
    }

    public int getCommentsEnabled() {
        return mCommentsEnabled;
    }

    public List<PostElement> getPostElementList() {
        return mPostElementList;
    }

    public List<PostInterface.PostImage> getPageImages() {
        return mPostImages;
    }

    public List<PostInterface.PostVideo> getPageVideos() {
        return mPostVideo;
    }

    public List<PostInterface.PostUri> getPostUris() {
        return mPostUri;
    }

    public int getCurrentUserLike() {
        return mCurrentUserLike;
    }

    public int getLikesCount() {
        return mLikesCount;
    }

    public void setCurrentUserLike(int mCurrentUserLike) {
        this.mCurrentUserLike = mCurrentUserLike;
    }

    public void setLikesCount(int mLikesCount) {
        this.mLikesCount = mLikesCount;
    }

    public void setCommentsCount(int mCommentsCount) {
        this.mCommentsCount = mCommentsCount;
    }
}
