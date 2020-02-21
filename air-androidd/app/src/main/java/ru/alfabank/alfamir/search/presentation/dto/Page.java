package ru.alfabank.alfamir.search.presentation.dto;

import java.util.UUID;

import static ru.alfabank.alfamir.Constants.Search.VIEW_TYPE_PAGE;

public class Page implements DisplayableSearchItem {

    private static final int VIEW_TYPE = VIEW_TYPE_PAGE;

    private String mId;
    private String mTitle;
    private String mFeedId;
    private String mFeedType;
    private String mPublishedDate;
    private String mImageUrl;
    private long mViewId;

    private Page(String id,
                 String title,
                 String feedId,
                 String feedType,
                 String publishedDate,
                 String imageUrl,
                 long viewId){
        mId = id;
        mTitle = title;
        mFeedId = feedId;
        mFeedType = feedType;
        mPublishedDate = publishedDate;
        mImageUrl = imageUrl;
        mViewId = viewId;
    }

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

    @Override
    public int getType() {
        return VIEW_TYPE;
    }

    @Override
    public long getViewId() {
        return mViewId;
    }

    public static class Builder {
        private String mId;
        private String mTitle;
        private String mFeedId;
        private String mFeedType;
        private String mPublishedDate;
        private String mImageUrl;
        private long mViewId;

        public Builder id(String id){
            mId = id;
            return this;
        }

        public Builder title(String title){
            mTitle = title;
            return this;
        }
        public Builder feedId(String feedId){
            mFeedId = feedId;
            return this;
        }
        public Builder feedType(String feedType){
            mFeedType = feedType;
            return this;
        }
        public Builder publishedDate(String publishedDate){
            mPublishedDate = publishedDate;
            return this;
        }
        public Builder imageUrl(String imageUrl){
            mImageUrl = imageUrl;
            return this;
        }
        public Page build(){
            mViewId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
            return new Page(mId, mTitle, mFeedId, mFeedType, mPublishedDate, mImageUrl, mViewId);
        }
    }
}