package ru.alfabank.alfamir.search.data.dto;

import com.google.gson.annotations.SerializedName;

public class PageRaw {
    @SerializedName("id")
    String mId;
    @SerializedName("title")
    String mTitle;
    @SerializedName("siteurl")
    String mFeedId;
    @SerializedName("type")
    String mFeedType;
    @SerializedName("pubdate")
    String mPublishedDate;
    @SerializedName("imageurl")
    String mImageUrl;

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getFeedId() {
        return mFeedId;
    }

    public String getFeedType() {
        return mFeedType;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
