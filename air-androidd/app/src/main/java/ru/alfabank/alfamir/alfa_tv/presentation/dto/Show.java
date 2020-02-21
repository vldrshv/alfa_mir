package ru.alfabank.alfamir.alfa_tv.presentation.dto;

public class Show implements ShowListElement {

    private int mViewType;
    private boolean mIsPasswordEntered;

    private int mId;
    private String mStartDate;
    private String mEndDate;
    private long mLongStartDate;
    private long mLongEndDate;
    private String mTitle;
    private String mDescription;
    private Host mHost;
    private String mRoom;
    private int mIsPasswordRequired;
    private String mPassword;
    private int mIsOnAir;

    private Show(int id,
                 String startDate,
                 String endDate,
                 long longStartDate,
                 long longEndDate,
                 String title,
                 String description,
                 Host host,
                 String room,
                 int isPasswordRequired,
                 String password,
                 int isOnAir){
       mId = id;
       mStartDate = startDate;
       mEndDate = endDate;
        mLongStartDate = longStartDate;
        mLongEndDate = longEndDate;
       mTitle = title;
       mDescription = description;
       mHost = host;
       mRoom = room;
       mIsPasswordRequired = isPasswordRequired;
       mPassword = password;
       mIsOnAir = isOnAir;
    }

    public void setIsOnAir(int isOnAor){
        mIsOnAir = isOnAor;
    }

    public void setIsPasswordEntered(boolean isPasswordEntered) {
        mIsPasswordEntered = isPasswordEntered;
    }

    public int getId() {
        return mId;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public long getLongStartDate() {
        return mLongStartDate;
    }

    public long getLongEndDate() {
        return mLongEndDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public Host getHost() {
        return mHost;
    }

    public String getRoom() {
        return mRoom;
    }

    public int getIsPasswordRequired() {
        return mIsPasswordRequired;
    }

    public String getPassword() {
        return mPassword;
    }

    public int getIsOnAir() {
        return mIsOnAir;
    }

    public boolean getIsPasswordEntered() {
        return mIsPasswordEntered;
    }

    public void setViewType(int viewType) {
        mViewType = viewType;
    }


    @Override
    public int getViewType() {
        return mViewType;
    }

    public static class Builder {
        private int mId;
        private String mStartDate;
        private String mEndDate;
        private long mLongStartDate;
        private long mLongEndDate;
        private String mTitle;
        private String mDescription;
        private Host mHost;
        private String mRoom;
        private int mIsPasswordRequired;
        private String mPassword;
        private int mIsOnAir;

        public Builder id(int id){
            mId = id;
            return this;
        }
        public Builder startDate(String startDate){
            mStartDate = startDate;
            return this;
        }

        public Builder endDate(String endDate){
            mEndDate = endDate;
            return this;
        }

        public Builder startDate(long longStartDate){
            mLongStartDate = longStartDate;
            return this;
        }

        public Builder endDate(long longEndDate){
            mLongEndDate = longEndDate;
            return this;
        }

        public Builder title(String title){
            mTitle = title;
            return this;
        }

        public Builder description(String description){
            mDescription = description;
            return this;
        }

        public Builder host(Host host){
            mHost = host;
            return this;
        }

        public Builder room(String room){
            mRoom = room;
            return this;
        }

        public Builder isPasswordRequired(int isPasswordRequired){
            mIsPasswordRequired = isPasswordRequired;
            return this;
        }

        public Builder password(String password){
            mPassword = password;
            return this;
        }

        public Builder isOnAir(int isOnAir){
            mIsOnAir = isOnAir;
            return this;
        }

        public Show build(){
            return new Show(mId,
                    mStartDate,
                    mEndDate,
                    mLongStartDate,
                    mLongEndDate,
                    mTitle,
                    mDescription,
                    mHost,
                    mRoom,
                    mIsPasswordRequired,
                    mPassword,
                    mIsOnAir);
        }

    }

}
