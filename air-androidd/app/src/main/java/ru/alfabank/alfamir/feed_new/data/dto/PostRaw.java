package ru.alfabank.alfamir.feed_new.data.dto;

import com.google.gson.annotations.SerializedName;

public class PostRaw {

    @SerializedName("newsid")
    private
    String mId;

    @SerializedName("newstitle")
    private
    String mTitle;

    @SerializedName("pubtime")
    private
    String mPubTime;

    @SerializedName("updatedtime")
    private
    String mUpdateTime;

    @SerializedName("likecount")
    private int mLikeCount;

    @SerializedName("currentuserlike")
    private int mCurrentUserLike;

    @SerializedName("commentscount")
    private int mCommentCount;

    @SerializedName("bodyhtml")
    private String mHtmlContent;

    @SerializedName("mainimgbase64")
    private String mImageUrl;

    @SerializedName("author")
    private AuthorRaw mAuthor;

    @SerializedName("posturl")
    private String mFeedUrl;

    @SerializedName("threadid")
    private String mThreadId;

    @SerializedName("contenttype")
    private String mFeedType;

    @SerializedName("cancomment")
    private int mCommentEnabled;

    @SerializedName("sitetitle")
    private String mHeading;

    @SerializedName("candelete")
    private int mDeleteEnabled;

    @SerializedName("isfavorite")
    private int mIsFavorite;

    @SerializedName("issubscribed")
    private int mIsSubscribed;

    @SerializedName("page_objects_array")
    private PostInteractionElementRaw mInteractionElement;

    @SerializedName("card_view_text")
    private String mContentPreviewText;

    @SerializedName("show_settings")
    private PostParametersRaw mPostParameters;

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPubTime() {
        return mPubTime;
    }

    public String getUpdateTime() {
        return mUpdateTime;
    }

    public int getLikeCount() {
        return mLikeCount;
    }

    public int getCurrentUserLike() {
        return mCurrentUserLike;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public String getHtmlContent() {
        return mHtmlContent;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public AuthorRaw getAuthor() {
        return mAuthor;
    }

    public String getFeedUrl() {
        return mFeedUrl;
    }

    public String getThreadId() {
        return mThreadId;
    }

    public String getFeedType() {
        return mFeedType;
    }

    public int getCommentEnabled() {
        return mCommentEnabled;
    }

    public String getHeading() {
        return mHeading;
    }

    public int getDeleteEnabled() {
        return mDeleteEnabled;
    }

    public int getIsFavorite() {
        return mIsFavorite;
    }

    public int getIsSubscribed() {
        return mIsSubscribed;
    }

    public PostInteractionElementRaw getInteractionElement() {
        return mInteractionElement;
    }

    public String getContentPreviewText() {
        return mContentPreviewText;
    }

    public PostParametersRaw getPostParameters() {
        return mPostParameters;
    }

}
