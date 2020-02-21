package ru.alfabank.alfamir.notification.data.dto;

import com.google.gson.annotations.SerializedName;

public class DisplayInfoRaw {
    @SerializedName("type")
    String mType;
    @SerializedName("content")
    String mContent;

    public String getType() {
        return mType;
    }

    public String getContent() {
        return mContent;
    }
}
