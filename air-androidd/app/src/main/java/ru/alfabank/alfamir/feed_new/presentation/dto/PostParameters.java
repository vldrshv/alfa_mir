package ru.alfabank.alfamir.feed_new.presentation.dto;

public class PostParameters {
    private int mLikeEnabled;
    private int mCommentEnabled;
    private int mMenuEnabled;
    private int mHeadingVisible;
    private int mTitleVisible;

    private PostParameters(int likeEnabled,
                           int commentEnabled,
                           int menuEnabled,
                           int headingVisible,
                           int titleVisible){
        mLikeEnabled = likeEnabled;
        mCommentEnabled = commentEnabled;
        mMenuEnabled = menuEnabled;
        mHeadingVisible = headingVisible;
        mTitleVisible = titleVisible;
    }

    public int getLikeEnabled() {
        return mLikeEnabled;
    }

    public int getCommentEnabled() {
        return mCommentEnabled;
    }

    public int getMenuEnabled() {
        return mMenuEnabled;
    }

    public int getHeadingVisible() {
        return mHeadingVisible;
    }

    public int getTitleVisible() {
        return mTitleVisible;
    }

    public static class Builder {
        private int mLikeEnabled;
        private int mCommentEnabled;
        private int mMenuEnabled;
        private int mHeadingVisible;
        private int mTitleVisible;

        public Builder likeEnabled(int likeEnabled){
            mLikeEnabled = likeEnabled;
            return this;
        }

        public Builder commentEnabled(int commentEnabled){
            mCommentEnabled = commentEnabled;
            return this;
        }

        public Builder menuEnabled(int menuEnabled){
            mMenuEnabled = menuEnabled;
            return this;
        }

        public Builder headingVisible(int headingVisible){
            mHeadingVisible = headingVisible;
            return this;
        }

        public Builder titleVisible(int titleVisible){
            mTitleVisible = titleVisible;
            return this;
        }

        public PostParameters build(){
            return new PostParameters(
                    mLikeEnabled,
                    mCommentEnabled,
                    mMenuEnabled,
                    mHeadingVisible,
                    mTitleVisible);
        }
    }
}
