package ru.alfabank.alfamir.data.dto.image;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 28.04.2018.
 */

public class ImageModel {
    @SerializedName("imgbasecode")
    String encodedImage;
    @SerializedName("status")
    String status;

    public String getEncodedImage() {
        return encodedImage;
    }

    public String getStatus() {
        return status;
    }
}
