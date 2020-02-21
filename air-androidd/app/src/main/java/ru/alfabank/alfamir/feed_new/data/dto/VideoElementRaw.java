package ru.alfabank.alfamir.feed_new.data.dto;

import com.google.gson.annotations.SerializedName;

public class VideoElementRaw {
    @SerializedName("id")
    private
    String mArchiveId;
    @SerializedName("html_id")
    private
    String mId;
    @SerializedName("poster_url")
    private
    String mPostUrl;
    @SerializedName("poster_width")
    private
    int mPostWidth;
    @SerializedName("poster_height")
    private
    int mPostHeight;

    public String getArchiveId() {
        return mArchiveId;
    }

    public String getId() {
        return mId;
    }

    public String getPostUrl() {
        return mPostUrl;
    }

    public int getPostWidth() {
        return mPostWidth;
    }

    public int getPostHeight() {
        return mPostHeight;
    }
}
