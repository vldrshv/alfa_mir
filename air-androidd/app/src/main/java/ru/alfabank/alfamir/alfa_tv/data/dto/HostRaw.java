package ru.alfabank.alfamir.alfa_tv.data.dto;

import com.google.gson.annotations.SerializedName;

public class HostRaw {
    @SerializedName("id")
    String mLogin;
    @SerializedName("fullname")
    String mName;
    @SerializedName("photobase64")
    String mPicLink;
    @SerializedName("jobtitle")
    String mTitle;

    public String getLogin() {
        return mLogin;
    }

    public String getName() {
        return mName;
    }

    public String getPicLink() {
        return mPicLink;
    }

    public String getTitle() {
        return mTitle;
    }
}
