package ru.alfabank.alfamir.favorites.data.dto;

import com.google.gson.annotations.SerializedName;

public class FavoriteProfileRaw {
    @SerializedName("email")
    String mEmail;
    @SerializedName("fullname")
    String mName;
    @SerializedName("id")
    String mLogin;
    @SerializedName("jobTitle")
    String mTitle;
    @SerializedName("photobase64")
    String mImageUrl;
    @SerializedName("workphone")
    String mWorkPhone;

    public String getEmail() {
        return mEmail;
    }

    public String getName() {
        return mName;
    }

    public String getLogin() {
        return mLogin;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getWorkPhone() {
        return mWorkPhone;
    }
}
