package ru.alfabank.alfamir.feed_new.data.dto;

import com.google.gson.annotations.SerializedName;

public class PostParametersRaw {
    @SerializedName("like_available")
    int mLikeEnabled;
    @SerializedName("comment_available")
    int mCommentEnabled;
    @SerializedName("menu_available")
    int mMenuEnabled;
    @SerializedName("rubric_visible")
    int mHeadingVisible;
    @SerializedName("title_visible")
    int mTitleVisible;

    public int getLikeEnabled() {
        return mLikeEnabled;
    }

    public int getCommentEnabled() {
        return mCommentEnabled;
    }

    public int getMenuEnabled() {
        return mMenuEnabled;
    }

    public int getHeadingVisible() {
        return mHeadingVisible;
    }

    public int getTitleVisible() {
        return mTitleVisible;
    }
}
