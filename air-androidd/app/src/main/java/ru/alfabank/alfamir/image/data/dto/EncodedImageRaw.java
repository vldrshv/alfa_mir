package ru.alfabank.alfamir.image.data.dto;

import com.google.gson.annotations.SerializedName;

public class EncodedImageRaw {
    @SerializedName("imgbasecode")
    String mEncodedImage;
    @SerializedName("status")
    String mStatus; // wtf is this for?!

    public String getEncodedImage() {
        return mEncodedImage;
    }

    public String getStatus() {
        return mStatus;
    }
}