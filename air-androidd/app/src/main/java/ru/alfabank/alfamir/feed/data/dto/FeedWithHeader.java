package ru.alfabank.alfamir.feed.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.alfabank.alfamir.post.data.dto.PostRaw;

public class FeedWithHeader {

    @SerializedName("header")
    public HeaderRawOld header;

    @SerializedName("news")
    public List<PostRaw> newsItems;

    public HeaderRawOld getHeader() {
        return header;
    }

    public List<PostRaw> getPosts() {
        return newsItems;
    }
}
