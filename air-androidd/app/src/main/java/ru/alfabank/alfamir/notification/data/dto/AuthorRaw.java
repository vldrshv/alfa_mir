package ru.alfabank.alfamir.notification.data.dto;

import com.google.gson.annotations.SerializedName;

public class AuthorRaw {
    @SerializedName("id")
    String mLogin;
    @SerializedName("fullname")
    String mName;
    @SerializedName("photobase64")
    String mImageUrl;
    @SerializedName("jobtitle")
    String mTitle;

    public String getLogin() {
        return mLogin;
    }

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getTitle() {
        return mTitle;
    }
}
