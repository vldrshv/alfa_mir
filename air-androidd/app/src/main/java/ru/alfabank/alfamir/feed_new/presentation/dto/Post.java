package ru.alfabank.alfamir.feed_new.presentation.dto;

import static ru.alfabank.alfamir.Constants.Feed_element.FEED_POST;

public class Post implements DisplayableFeedItem {

    private static final int VIEW_TYPE = FEED_POST;

    private String mId;
    private String mTitle;
    private String mPubTime;
    private String mUpdateTime;
    private int mLikeCount;
    private int mCurrentUserLike;
    private int mCommentCount;
    private String mHtmlContent;
    private String mImageUrl;
    private Author mAuthor;
    private String mFeedUrl;
    private String mThreadId;
    private String mFeedType;
    private int mCommentEnabled;
    private String mHeading;
    private int mDeleteEnabled;
    private int mIsFavorite;
    private int mIsSubscribed;
    private PostInteractionElement mInteractionElement;
    private String mContentPreviewText;
    private PostParameters mPostParameters;

    private Post(String id,
                 String title,
                 String pubTime,
                 String updateTime,
                 int likeCount,
                 int currentUserLike,
                 int commentCount,
                 String htmlContent,
                 String imageUrl,
                 Author author,
                 String feedUrl,
                 String threadId,
                 String feedType,
                 int commentEnabled,
                 String heading,
                 int deleteEnabled,
                 int isFavorite,
                 int isSubscribed,
                 PostInteractionElement interactionElement,
                 String contentPreviewText,
                 PostParameters postParameters){
        mId = id;
        mTitle = title;
        mPubTime = pubTime;
        mUpdateTime = updateTime;
        mLikeCount = likeCount;
        mCurrentUserLike = currentUserLike;
        mCommentCount = commentCount;
        mHtmlContent = htmlContent;
        mImageUrl = imageUrl;
        mAuthor = author;
        mFeedUrl = feedUrl;
        mThreadId = threadId;
        mFeedType = feedType;
        mCommentEnabled = commentEnabled;
        mHeading = heading;
        mDeleteEnabled = deleteEnabled;
        mIsFavorite = isFavorite;
        mIsSubscribed = isSubscribed;
        mInteractionElement = interactionElement;
        mContentPreviewText = contentPreviewText;
        mPostParameters = postParameters;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPubTime() {
        return mPubTime;
    }

    public String getUpdateTime() {
        return mUpdateTime;
    }

    public int getLikeCount() {
        return mLikeCount;
    }

    public int getCurrentUserLike() {
        return mCurrentUserLike;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public String getHtmlContent() {
        return mHtmlContent;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public Author getAuthor() {
        return mAuthor;
    }

    public String getFeedUrl() {
        return mFeedUrl;
    }

    public String getThreadId() {
        return mThreadId;
    }

    public String getFeedType() {
        return mFeedType;
    }

    public int getCommentEnabled() {
        return mCommentEnabled;
    }

    public String getHeading() {
        return mHeading;
    }

    public int getDeleteEnabled() {
        return mDeleteEnabled;
    }

    public int getIsFavorite() {
        return mIsFavorite;
    }

    public int getIsSubscribed() {
        return mIsSubscribed;
    }

    public PostInteractionElement getInteractionElement() {
        return mInteractionElement;
    }

    public String getContentPreviewText() {
        return mContentPreviewText;
    }

    public PostParameters getPostParameters() {
        return mPostParameters;
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
        private String mId;
        private String mTitle;
        private String mPubTime;
        private String mUpdateTime;
        private int mLikeCount;
        private int mCurrentUserLike;
        private int mCommentCount;
        private String mHtmlContent;
        private String mImageUrl;
        private Author mAuthor;
        private String mFeedUrl;
        private String mThreadId;
        private String mFeedType;
        private int mCommentEnabled;
        private String mHeading;
        private int mDeleteEnabled;
        private int mIsFavorite;
        private int mIsSubscribed;
        private PostInteractionElement mInteractionElement;
        private String mContentPreviewText;
        private PostParameters mPostParameters;

        public Builder id(String id){
            mId = id;
            return this;
        }

        public Builder title(String title){
            mTitle = title;
            return this;
        }

        public Builder pubTime(String pubTime){
            mPubTime = pubTime;
            return this;
        }

        public Builder updateTime(String updateTime){
            mUpdateTime = updateTime;
            return this;
        }

        public Builder likeCount(int likeCount){
            mLikeCount = likeCount;
            return this;
        }

        public Builder currentUserLike(int currentUserLike){
            mCurrentUserLike = currentUserLike;
            return this;
        }

        public Builder commentCount(int commentCount){
            mCommentCount = commentCount;
            return this;
        }

        public Builder htmlContent(String htmlContent){
            mHtmlContent = htmlContent;
            return this;
        }

        public Builder imageUrl(String imageUrl){
            mImageUrl = imageUrl;
            return this;
        }

        public Builder author(Author author){
            mAuthor = author;
            return this;
        }

        public Builder feedUrl(String feedUrl){
            mFeedUrl = feedUrl;
            return this;
        }

        public Builder threadId(String treadId){
            mThreadId = treadId;
            return this;
        }

        public Builder feedType(String feedType){
            mFeedType = feedType;
            return this;
        }

        public Builder commentEnabled(int commentEnabled){
            mCommentEnabled = commentEnabled;
            return this;
        }

        public Builder heading(String heading){
            mHeading = heading;
            return this;
        }

        public Builder deleteEnabled(int deleteEnabled){
            mDeleteEnabled = deleteEnabled;
            return this;
        }

        public Builder isFavorite(int isFavorite){
            mIsFavorite = isFavorite;
            return this;
        }

        public Builder isSubscribed(int isSubscribed){
            mIsSubscribed = isSubscribed;
            return this;
        }

        public Builder interactionElement(PostInteractionElement interactionElement){
            mInteractionElement = interactionElement;
            return this;
        }

        public Builder contentPreviewText(String contentPreviewText){
            mContentPreviewText = contentPreviewText;
            return this;
        }

        public Builder postParameters(PostParameters postParameters){
            mPostParameters = postParameters;
            return this;
        }


        public Post build(){
            return new Post(mId,
                    mTitle,
                    mPubTime,
                    mUpdateTime,
                    mLikeCount,
                    mCurrentUserLike,
                    mCommentCount,
                    mHtmlContent,
                    mImageUrl,
                    mAuthor,
                    mFeedUrl,
                    mThreadId,
                    mFeedType,
                    mCommentEnabled,
                    mHeading,
                    mDeleteEnabled,
                    mIsFavorite,
                    mIsSubscribed,
                    mInteractionElement,
                    mContentPreviewText,
                    mPostParameters);
        }
    }

}
