package ru.alfabank.alfamir.feed_new.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedRaw {
    @SerializedName("header")
    HeaderRaw mHeader;

    @SerializedName("news")
    List<PostRaw> mPosts;

    public HeaderRaw getHeader() {
        return mHeader;
    }

    public List<PostRaw> getPosts() {
        return mPosts;
    }
}
