package ru.alfabank.alfamir.alfa_tv.data.dto;

import com.google.gson.annotations.SerializedName;

public class ShowUriRaw {
    @SerializedName("url")
    private String mUrl;
    @SerializedName("token")
    private String mToken;

    public String getUrl() {
        return mUrl;
    }

    public String getToken() {
        return mToken;
    }
}
