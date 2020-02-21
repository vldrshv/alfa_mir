package ru.alfabank.alfamir.feed_new.data.dto;

import com.google.gson.annotations.SerializedName;

public class PostInteractionElementRaw {
    @SerializedName("page_images_array")
    ImageElementRaw[] mImageArray;

    @SerializedName("page_videos_array")
    VideoElementRaw[] mVideoArray;

    @SerializedName("page_references_array")
    UriElementRaw[] mUriArray;

    public ImageElementRaw[] getImageArray() {
        return mImageArray;
    }

    public VideoElementRaw[] getVideoArray() {
        return mVideoArray;
    }

    public UriElementRaw[] getUriArray() {
        return mUriArray;
    }
}
