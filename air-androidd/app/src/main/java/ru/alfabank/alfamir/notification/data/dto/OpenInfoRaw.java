package ru.alfabank.alfamir.notification.data.dto;

import com.google.gson.annotations.SerializedName;

public class OpenInfoRaw {
    @SerializedName("type")
    String mFeedType;
    @SerializedName("commentid")
    String mCommentId;
    @SerializedName("iscomment")
    int mIsComment;
    @SerializedName("userid")
    String mUserLogin;
    @SerializedName("postid")
    String mPostId;
    @SerializedName("posturl")
    String mFeedUrl;

    public String getFeedType() {
        return mFeedType;
    }

    public String getCommentId() {
        return mCommentId;
    }

    public int getIsComment() {
        return mIsComment;
    }

    public String getUserLogin() {
        return mUserLogin;
    }

    public String getPostId() {
        return mPostId;
    }

    public String getFeedUrl() {
        return mFeedUrl;
    }
}
