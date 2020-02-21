package ru.alfabank.alfamir.feed_new.data.dto;

import com.google.gson.annotations.SerializedName;

public class UriElementRaw {
    @SerializedName("id")
    String mId;
    @SerializedName("ref_type")
    String mReferenceType;
    @SerializedName("content")
    String mValue;

    public String getId() {
        return mId;
    }

    public String getRefferenceType() {
        return mReferenceType;
    }

    public String getValue() {
        return mValue;
    }
}
