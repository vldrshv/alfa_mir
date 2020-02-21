package ru.alfabank.alfamir.notification.data.dto;

import com.google.gson.annotations.SerializedName;

public class NotificationRaw {

    @SerializedName("notificationid")
    long mId;
    @SerializedName("title")
    String mText;
    @SerializedName("img")
    String mImageUrl;
    @SerializedName("createtime")
    String mDate;
    @SerializedName("author")
    AuthorRaw mAuthor;
    @SerializedName("openparams")
    OpenInfoRaw mOpenInfo;
    @SerializedName("messageparams")
    DisplayInfoRaw [] mDisplayInfo;


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

    public AuthorRaw getAuthor() {
        return mAuthor;
    }

    public OpenInfoRaw getOpenInfo() {
        return mOpenInfo;
    }

    public DisplayInfoRaw[] getDisplayInfo() {
        return mDisplayInfo;
    }
}
