package ru.alfabank.alfamir.initialization.data.dto;

import com.google.gson.annotations.SerializedName;

public class ShortUserInfoRaw {
    @SerializedName("login")
    String mId;
    @SerializedName("fullname")
    String mName;
    @SerializedName("jobtitle")
    String mTitle;
    @SerializedName("photobase64")
    String mEncodedImage;
    @SerializedName("city")
    String mCity;

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getEncodedImage() {
        return mEncodedImage;
    }

    public String getCity() {
        return mCity;
    }
}
