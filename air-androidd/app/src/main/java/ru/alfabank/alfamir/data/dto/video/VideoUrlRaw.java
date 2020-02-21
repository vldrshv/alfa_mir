package ru.alfabank.alfamir.data.dto.video;

import com.google.gson.annotations.SerializedName;

public class VideoUrlRaw {
    @SerializedName("url")
    private
    String url;
    @SerializedName("token")
    private
    String token;

    public String getUrl() {
        return url;
    }

    public String getToken() {
        return token;
    }

}
