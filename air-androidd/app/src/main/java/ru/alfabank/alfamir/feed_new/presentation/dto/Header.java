package ru.alfabank.alfamir.feed_new.presentation.dto;

import static ru.alfabank.alfamir.Constants.Feed_element.FEED_HEADER;

public class Header implements DisplayableFeedItem {

    private static final int VIEW_TYPE = FEED_HEADER;

    private String mTitle;
    private String mDescription;
    private int mIsPersonal;
    private String mImageUrl;
    private String mPlaceholderImage;
    private String mAuthorImageUrl;
    private String mPubTime;
    private String mFeedType;
    private String mFeedUrl;
    private int mIsSubscribed;
    private int mCanPublish;

    private Header(String title,
                   String description,
                   int isPersonal,
                   String imageUrl,
                   String placeholderImage,
                   String authorImageUrl,
                   String pubTime,
                   String feedType,
                   String feedUrl,
                   int isSubscribed,
                   int canPublish){
        mTitle = title;
        mDescription = description;
        mIsPersonal = isPersonal;
        mImageUrl = imageUrl;
        mPlaceholderImage = placeholderImage;
        mAuthorImageUrl = authorImageUrl;
        mPubTime = pubTime;
        mFeedType = feedType;
        mFeedUrl = feedUrl;
        mIsSubscribed = isSubscribed;
        mCanPublish = canPublish;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getIsPersonal() {
        return mIsPersonal;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getPlaceholderImage() {
        return mPlaceholderImage;
    }

    public String getAuthorImageUrl() {
        return mAuthorImageUrl;
    }

    public String getPubTime() {
        return mPubTime;
    }

    public String getFeedType() {
        return mFeedType;
    }

    public String getFeedUrl() {
        return mFeedUrl;
    }

    public int getIsSubscribed() {
        return mIsSubscribed;
    }

    public int getCanPublish() {
        return mCanPublish;
    }

    @Override
    public int getType() {
        return VIEW_TYPE;
    }

    @Override
    public long getViewId() {
        return 0;
    }

    public static class Builder {
        private String mTitle;
        private String mDescription;
        private int mIsPersonal;
        private String mImageUrl;
        private String mPlaceholderImage;
        private String mAuthorImageUrl;
        private String mPubTime;
        private String mFeedType;
        private String mFeedUrl;
        private int mIsSubscribed;
        private int mCanPublish;

        public Builder title(String title){
            mTitle = title;
            return this;
        }

        public Builder description(String description){
            mDescription = description;
            return this;
        }

        public Builder isPersonal(int isPersonal){
            mIsPersonal = isPersonal;
            return this;
        }

        public Builder imageUrl(String imageUrl){
            mImageUrl = imageUrl;
            return this;
        }

        public Builder placeholderImage(String placeholderImage){
            mPlaceholderImage = placeholderImage;
            return this;
        }

        public Builder authorImageUrl(String authorImageUrl){
            mAuthorImageUrl = authorImageUrl;
            return this;
        }

        public Builder pubTime(String pubTime){
            mPubTime = pubTime;
            return this;
        }

        public Builder feedType(String feedType){
            mFeedType = feedType;
            return this;
        }

        public Builder feedUrl(String feedUrl){
            mFeedUrl = feedUrl;
            return this;
        }

        public Builder isSubscribed(int isSubscribed){
            mIsSubscribed = isSubscribed;
            return this;
        }

        public Builder canPublish(int canPublish){
            mCanPublish = canPublish;
            return this;
        }

        public Header build(){
            return new Header( mTitle,
                    mDescription,
                    mIsPersonal,
                    mImageUrl,
                    mPlaceholderImage,
                    mAuthorImageUrl,
                    mPubTime,
                    mFeedType,
                    mFeedUrl,
                    mIsSubscribed,
                    mCanPublish);
        }
    }
}
