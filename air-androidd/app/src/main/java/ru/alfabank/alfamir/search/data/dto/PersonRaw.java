package ru.alfabank.alfamir.search.data.dto;

import com.google.gson.annotations.SerializedName;

public class PersonRaw {
    @SerializedName("name")
    String mName;
    @SerializedName("account")
    String mId;
    @SerializedName("imageurl")
    String mImageUrl;
    @SerializedName("jobtitle")
    String mPosition;

    public String getName() {
        return mName;
    }

    public String getId() {
        return mId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getPosition() {
        return mPosition;
    }
}
