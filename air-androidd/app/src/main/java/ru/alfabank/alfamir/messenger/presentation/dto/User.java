package ru.alfabank.alfamir.messenger.presentation.dto;

public class User {
    private String mId;
    private String mLastOnline;
    private String mName;
    private String mPosition;
    private String mPicLink;
    private int mOnlineStatus;

    private User(String id,
                 String lastOnline,
                 String name,
                 String position,
                 String picLink,
                 int onlineStatus){
        mId = id;
        mLastOnline = lastOnline;
        mName = name;
        mPosition = position;
        mPicLink = picLink;
        mOnlineStatus = onlineStatus;
    }

    public String getId() {
        return mId;
    }

    public int getOnlineStatus() {
        return mOnlineStatus;
    }

    public String getLastOnline() {
        return mLastOnline;
    }

    public static class Builder {
        private String mId;
        private String mLastOnline;
        private String mName;
        private String mPosition;
        private String mPicLink;
        private int mOnlineStatus;

        public Builder id(String id){
            mId = id;
            return this;
        }

        public Builder onlineStatus(int onlineStatus){
            mOnlineStatus = onlineStatus;
            return this;
        }

        public Builder lastOnline(String lastOnline){
            mLastOnline = lastOnline;
            return this;
        }

        public Builder name(String name){
            mName = name;
            return this;
        }

        public Builder position(String position){
            mPosition = position;
            return this;
        }

        public Builder picLink(String picLink){
            mPicLink = picLink;
            return this;
        }

        public User build(){
            if (mId == null) mId = "";
            if (mLastOnline == null) mLastOnline = "";
            if (mName == null) mName = "";
            if (mPosition == null) mPosition = "";
            if (mPicLink == null) mPicLink = "";

            return new User(mId,
                    mLastOnline,
                    mName,
                    mPosition,
                    mPicLink,
                    mOnlineStatus);
        }
    }

}
