package ru.alfabank.alfamir.messenger.presentation.dto;

import java.util.UUID;

public class ChatLight {
    private String mId;
    private String mType;
    private String mTitle;
    private String mLastMessage;
    private String mLastMessageDate;
    private String mLastMessageUserLogin;
    private String mLastMessageUserName;
    private String mCorrespondentLogin;
    private String mCorrespondentPicUrl;
    private String mCorrespondentTitle;
    private int mUnreadCount;
    private int mCorrespondentOnlineStatus;
    private long mViewId;

    private ChatLight(String id,
                      String type,
                      String title,
                      String lastMessage,
                      String lastMessageDate,
                      String lastMessageUserLogin,
                      String lastMessageUserName,
                      String correspondentLogin,
                      String correspondentPicUrl,
                      String correspondentTitle,
                      int unreadCount,
                      int correspondentOnlineStatus,
                      long viewId){
        mId = id;
        mType = type;
        mTitle = title;
        mLastMessage = lastMessage;
        mLastMessageDate = lastMessageDate;
        mLastMessageUserLogin = lastMessageUserLogin;
        mLastMessageUserName = lastMessageUserName;
        mCorrespondentLogin = correspondentLogin;
        mCorrespondentPicUrl = correspondentPicUrl;
        mCorrespondentTitle = correspondentTitle;
        mUnreadCount = unreadCount;
        mCorrespondentOnlineStatus = correspondentOnlineStatus;
        mViewId = viewId;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setUnreadCount(int mUnreadCount) {
        this.mUnreadCount = mUnreadCount;
    }

    public void setLastMessage(String mLastMessage) {
        this.mLastMessage = mLastMessage;
    }

    public void setLastMessageDate(String mLastMessageDate) {
        this.mLastMessageDate = mLastMessageDate;
    }

    public void setLastMessageUserLogin(String mLastMessageUserLogin) {
        this.mLastMessageUserLogin = mLastMessageUserLogin;
    }

    public void setLastMessageUserName(String mLastMessageUserName) {
        this.mLastMessageUserName = mLastMessageUserName;
    }

    public void setCorrespondentLogin(String mCorrespondentLogin) {
        this.mCorrespondentLogin = mCorrespondentLogin;
    }

    public void setCorrespondentPicUrl(String mCorrespondentPicUrl) {
        this.mCorrespondentPicUrl = mCorrespondentPicUrl;
    }

    public void setCorrespondentOnlineStatus(int mCorrespondentOnlineStatus) {
        this.mCorrespondentOnlineStatus = mCorrespondentOnlineStatus;
    }

    public String getId() {
        return mId;
    }

    public String getType() {
        return mType;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getUnreadCount() {
        return mUnreadCount;
    }

    public String getLastMessage() {
        return mLastMessage;
    }

    public String getLastMessageDate() {
        return mLastMessageDate;
    }

    public String getLastMessageUserLogin() {
        return mLastMessageUserLogin;
    }

    public String getLastMessageUserName() {
        return mLastMessageUserName;
    }

    public String getCorrespondentLogin() {
        return mCorrespondentLogin;
    }

    public String getCorrespondentPicUrl() {
        return mCorrespondentPicUrl;
    }

    public String getCorrespondentTitle() {
        return mCorrespondentTitle;
    }

    public int getСorrespondentOnlineStatus() {
        return mCorrespondentOnlineStatus;
    }

    public long getViewId() {
        return mViewId;
    }

    public static class Builder {
        private String mId;
        private String mType;
        private String mTitle;
        private String mLastMessage;
        private String mLastMessageDate;
        private String mLastMessageUserLogin;
        private String mLastMessageUserName;
        private String mCorrespondentLogin;
        private String mCorrespondentPicUrl;
        private String mCorrespondentTitle;
        private int mUnreadCount;
        private int mCorrespondentOnlineStatus;
        private long mViewId;

        public Builder id(String id){
            mId = id;
            return this;
        }

        public Builder type(String type){
            mType = type;
            return this;
        }

        public Builder title(String title){
            mTitle = title;
            return this;
        }

        public Builder lastMessage (String lastMessage){
            mLastMessage = lastMessage;
            return this;
        }

        public Builder lastMessageDate (String lastMessageDate){
            mLastMessageDate = lastMessageDate;
            return this;
        }

        public Builder lastMessageUserLogin (String lastMessageUserLogin){
            mLastMessageUserLogin = lastMessageUserLogin;
            return this;
        }

        public Builder lastMessageUserName (String lastMessageUserName){
            mLastMessageUserName = lastMessageUserName;
            return this;
        }

        public Builder correspondentLogin (String correspondentLogin){
            mCorrespondentLogin = correspondentLogin;
            return this;
        }

        public Builder correspondentPicUrl (String correspondentPicUrl){
            mCorrespondentPicUrl = correspondentPicUrl;
            return this;
        }

        public Builder correspondentTitle (String correspondentTitle){
            mCorrespondentTitle = correspondentTitle;
            return this;
        }

        public Builder unreadCount(int unreadCount){
            mUnreadCount = unreadCount;
            return this;
        }
        
        public Builder correspondentOnlineStatus (int correspondentOnlineStatus){
            mCorrespondentOnlineStatus = correspondentOnlineStatus;
            return this;
        }
        
        public ChatLight build(){
            if(mId==null) mId = "";
            if(mType==null) mType = "";
            if(mTitle==null) mTitle = "";
            if(mLastMessage==null) mLastMessage = "";
            if(mLastMessageDate==null) mLastMessageDate = "";
            if(mLastMessageUserLogin==null) mLastMessageUserLogin = "";
            if(mLastMessageUserName==null) mLastMessageUserName = "";
            if(mCorrespondentLogin==null) mCorrespondentLogin = "";
            if(mCorrespondentPicUrl==null) mCorrespondentPicUrl = "";
            if(mCorrespondentTitle==null) mCorrespondentTitle = "";
            mViewId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
            ChatLight chatLight = new ChatLight(mId,
                    mType,
                    mTitle,
                    mLastMessage,
                    mLastMessageDate,
                    mLastMessageUserLogin,
                    mLastMessageUserName,
                    mCorrespondentLogin,
                    mCorrespondentPicUrl,
                    mCorrespondentTitle,
                    mUnreadCount,
                    mCorrespondentOnlineStatus,
                    mViewId);
            return chatLight;
        }

    }
}
