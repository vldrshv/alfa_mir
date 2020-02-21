package ru.alfabank.alfamir.favorites.data.dto;

import com.google.gson.annotations.SerializedName;

public class FavoritePostRaw {
    @SerializedName("title")
    String mTitle;
    @SerializedName("imageurl")
    String mImageUrl;
    @SerializedName("type")
    String mFeedType;
    @SerializedName("pageid")
    String mPostId;
    @SerializedName("pubdate")
    String mPubDate;
    @SerializedName("posturl")
    String mFeedUrl;

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getFeedType() {
        return mFeedType;
    }

    public String getPostId() {
        return mPostId;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public String getFeedUrl() {
        return mFeedUrl;
    }
}
