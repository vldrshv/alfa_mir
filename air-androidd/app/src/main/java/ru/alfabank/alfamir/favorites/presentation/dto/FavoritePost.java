package ru.alfabank.alfamir.favorites.presentation.dto;

public class FavoritePost {
    private String mTitle;
    private String mImageUrl;
    private String mFeedType;
    private String mPostId;
    private String mPubDate;
    private String mFeedUrl;

    private FavoritePost(String title,
                         String imageUrl,
                         String feedType,
                         String postId,
                         String pubDate,
                         String feedUrl){
        mTitle = title;
        mImageUrl = imageUrl;
        mFeedType = feedType;
        mPostId = postId;
        mPubDate = pubDate;
        mFeedUrl = feedUrl;
    }

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

    public static class Builder {
        private String mTitle;
        private String mImageUrl;
        private String mFeedType;
        private String mPostId;
        private String mPubDate;
        private String mFeedUrl;

        public Builder title(String title){
            mTitle = title;
            return this;
        }

        public Builder imageUrl(String imageUrl){
            mImageUrl = imageUrl;
            return this;
        }
        public Builder feedType(String feedType){
            mFeedType = feedType;
            return this;
        }
        public Builder postId(String postId){
            mPostId = postId;
            return this;
        }
        public Builder pubDate(String pubDate){
            mPubDate = pubDate;
            return this;
        }
        public Builder feedUrl(String feedUrl){
            mFeedUrl = feedUrl;
            return this;
        }

        public FavoritePost build(){
            return new FavoritePost(
                    mTitle,
                    mImageUrl,
                    mFeedType,
                    mPostId,
                    mPubDate,
                    mFeedUrl);
        }

    }

}
