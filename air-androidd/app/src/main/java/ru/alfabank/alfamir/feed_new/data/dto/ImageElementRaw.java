package ru.alfabank.alfamir.feed_new.data.dto;

import com.google.gson.annotations.SerializedName;

public class ImageElementRaw {
    @SerializedName("id")
    String mId;
    @SerializedName("height")
    int mHeight;
    @SerializedName("width")
    int mWidth;
    @SerializedName("url")
    String mUrl;

    public String getId() {
        return mId;
    }
    public int getHeight() {
        return mHeight;
    }
    public int getWidth() {
        return mWidth;
    }
    public String getUrl() {
        return mUrl;
    }
}
