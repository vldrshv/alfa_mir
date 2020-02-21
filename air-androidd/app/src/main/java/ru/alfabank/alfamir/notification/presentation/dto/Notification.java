package ru.alfabank.alfamir.notification.presentation.dto;

import java.util.List;
import java.util.UUID;

public class Notification {
    private long mViewId;
    private long mId;
    private int mNotificationType;
    private String mText;
    private String mImageUrl;
    private String mDate;
    private Author mAuthor;
    private OpenInfo mOpenInfo;
    private List<DisplayInfo> mDisplayInfo;

    private Notification(long viewId,
                         long id,
                         int notificationType,
                         String text,
                         String imageUrl,
                         String date,
                         Author author,
                         OpenInfo openInfo,
                         List<DisplayInfo> displayInfo){
        mViewId = viewId;
        mId = id;
        mNotificationType = notificationType;
        mText = text;
        mImageUrl = imageUrl;
        mDate = date;
        mAuthor = author;
        mOpenInfo = openInfo;
        mDisplayInfo = displayInfo;
    }

    public long getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getDate() {
        return mDate;
    }

    public Author getAuthor() {
        return mAuthor;
    }

    public OpenInfo getOpenInfo() {
        return mOpenInfo;
    }

    public List<DisplayInfo> getDisplayInfo() {
        return mDisplayInfo;
    }

    public int getNotificationType() {
        return mNotificationType;
    }

    public long getViewId() {
        return mViewId;
    }

    public static class Builder{
        private long mViewId;
        private long mId;
        private int mNotificationType;
        private String mText;
        private String mImageUrl;
        private String mDate;
        private Author mAuthor;
        private OpenInfo mOpenInfo;
        private List<DisplayInfo> mDisplayInfo;

        public Builder id(long id){
            mId = id;
            return this;
        }

        public Builder notificationType(int notificationType){
            mNotificationType = notificationType;
            return this;
        }

        public Builder text(String text){
            mText = text;
            return this;
        }

        public Builder imageUrl(String imageUrl){
            mImageUrl = imageUrl;
            return this;
        }

        public Builder date(String date){
            mDate = date;
            return this;
        }

        public Builder author(Author author){
            mAuthor = author;
            return this;
        }

        public Builder openInfo(OpenInfo openInfo){
            mOpenInfo = openInfo;
            return this;
        }
        public Builder displayInfo(List<DisplayInfo> displayInfo){
            mDisplayInfo = displayInfo;
            return this;
        }

        public Notification build(){
            mViewId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
            return new Notification(
                    mViewId,
                    mId,
                    mNotificationType,
                    mText,
                    mImageUrl,
                    mDate,
                    mAuthor,
                    mOpenInfo,
                    mDisplayInfo);
        }

    }

}
