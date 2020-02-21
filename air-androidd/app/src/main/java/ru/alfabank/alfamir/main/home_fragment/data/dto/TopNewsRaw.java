package ru.alfabank.alfamir.main.home_fragment.data.dto;

import com.google.gson.annotations.SerializedName;

public class TopNewsRaw {
    @SerializedName("URL")
    String mUrl;
    @SerializedName("Title")
    String mTitle;
    @SerializedName("PicURL")
    String mPicUrl;

    public String getUrl() {
        return mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPicUrl() {
        return mPicUrl;
    }
}
