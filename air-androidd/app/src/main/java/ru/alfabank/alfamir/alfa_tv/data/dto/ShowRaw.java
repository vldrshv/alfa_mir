package ru.alfabank.alfamir.alfa_tv.data.dto;

import com.google.gson.annotations.SerializedName;

public class ShowRaw {
    @SerializedName("id")
    int mId;
    @SerializedName("start")
    String mStartDate;
    @SerializedName("end")
    String mEndDate;
    @SerializedName("title")
    String mTitle;
    @SerializedName("description")
    String mDescription;
    @SerializedName("author")
    HostRaw mHost;
    @SerializedName("room")
    String mRoom;
    @SerializedName("passwordexist")
    int mIsPasswordRequired;
    @SerializedName("password")
    String mPassword;
    @SerializedName("streaming")
    int mIsOnAir;

    public int getId() {
        return mId;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
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

    public HostRaw getHost() {
        return mHost;
    }
}
