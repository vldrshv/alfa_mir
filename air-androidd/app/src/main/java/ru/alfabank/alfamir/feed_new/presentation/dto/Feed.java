package ru.alfabank.alfamir.feed_new.presentation.dto;

import java.util.ArrayList;
import java.util.List;

public class Feed {
    private Header mHeader;
    private List<Post> mPosts;

    private Feed(Header header,
                 List<Post> posts){
        mHeader = header;
        mPosts = posts;
    }

    public Header getHeader() {
        return mHeader;
    }

    public List<Post> getPosts() {
        return mPosts;
    }

    public List<DisplayableFeedItem> getDisplayableItemList(){
        List<DisplayableFeedItem> displayableFeedItems = new ArrayList<>();
        if (mHeader!=null) displayableFeedItems.add(mHeader);
        displayableFeedItems.addAll(mPosts);
        return displayableFeedItems;
    }

    public static class Builder {
        private Header mHeader;
        private List<Post> mPosts;

        public Builder header(Header header){
            mHeader = header;
            return this;
        }

        public Builder posts(List<Post> posts){
            mPosts = posts;
            return this;
        }

        public Feed build(){
            return new Feed(mHeader, mPosts);
        }

    }

}
