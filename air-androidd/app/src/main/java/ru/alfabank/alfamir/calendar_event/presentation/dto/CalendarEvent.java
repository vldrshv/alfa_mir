package ru.alfabank.alfamir.calendar_event.presentation.dto;

public class CalendarEvent {

    private int mId;
    private String mTitle;
    private String mLocation;
    private String mDate;
    private String mTime;
    private String mDescription;
    private String mPicUrl;
    private int tapable;
    private String isSliDo;

    private CalendarEvent(int id,
                          String title,
                          String location,
                          String date,
                          String time,
                          String description,
                          String picUrl,
                          int tapable,
                          String isSliDo) {
        mId = id;
        mTitle = title;
        mLocation = location;
        mDate = date;
        mTime = time;
        mDescription = description;
        mPicUrl = picUrl;
        this.tapable = tapable;
        this.isSliDo = isSliDo;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getPicUrl() {
        return mPicUrl;
    }

    public int getTapable() {
        return tapable;
    }

    public String isSliDo() {
        return isSliDo;
    }

    public static class Builder {
        private int mId;
        private String mTitle;
        private String mLocation;
        private String mDate;
        private String mTime;
        private String mDescription;
        private String mPicUrl;
        private int tapable;
        private String isSliDo;

        public Builder id(int id) {
            mId = id;
            return this;
        }

        public Builder title(String title) {
            mTitle = title;
            return this;
        }

        public Builder location(String location) {
            mLocation = location;
            return this;
        }

        public Builder date(String date) {
            mDate = date;
            return this;
        }

        public Builder time(String time) {
            mTime = time;
            return this;
        }

        public Builder description(String description) {
            mDescription = description;
            return this;
        }

        public Builder picUrl(String picUrl) {
            mPicUrl = picUrl;
            return this;
        }

        public Builder tapable(int tapable) {
            this.tapable = tapable;
            return this;
        }

        public Builder isSliDo(String isSliDo) {
            this.isSliDo = isSliDo;
            return this;
        }

        public CalendarEvent build() {
            return new CalendarEvent(mId, mTitle, mLocation, mDate, mTime, mDescription, mPicUrl, tapable, isSliDo);
        }
    }
}
