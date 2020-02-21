package ru.alfabank.alfamir.notification.presentation.dto;

public class OpenInfo {
    private String mNotificationType;
    private String mCommentId;
    private int mIsComment;
    private String mUserLogin;
    private String mPostId;
    private String mFeedUrl;

    private OpenInfo(String notificationType,
                     String commentId,
                     int isComment,
                     String userLogin,
                     String postId,
                     String feedUrl){
        mNotificationType = notificationType;
        mCommentId = commentId;
        mIsComment = isComment;
        mUserLogin = userLogin;
        mPostId = postId;
        mFeedUrl = feedUrl;
    }

    public String getNotificationType() {
        return mNotificationType;
    }

    public String getCommentId() {
        return mCommentId;
    }

    public int getIsComment() {
        return mIsComment;
    }

    public String getUserLogin() {
        return mUserLogin;
    }

    public String getPostId() {
        return mPostId;
    }

    public String getFeedUrl() {
        return mFeedUrl;
    }

    public static class Builder{
        private String mNotificationType;
        private String mCommentId;
        private int mIsComment;
        private String mUserLogin;
        private String mPostId;
        private String mFeedUrl;

        public Builder notificationType(String notificationType){
            mNotificationType = notificationType;
            return this;
        }

        public Builder commentId(String commentId){
            mCommentId = commentId;
            return this;
        }

        public Builder isComment(int isComment){
            mIsComment = isComment;
            return this;
        }

        public Builder userLogin(String userLogin){
            mUserLogin = userLogin;
            return this;
        }

        public Builder postId(String postId){
            mPostId = postId;
            return this;
        }

        public Builder feedUrl(String feedUrl){
            mFeedUrl = feedUrl;
            return this;
        }

        public OpenInfo build(){
            return new OpenInfo(mNotificationType,
                    mCommentId,
                    mIsComment,
                    mUserLogin,
                    mPostId,
                    mFeedUrl);
        }

    }

}
