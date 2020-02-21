package ru.alfabank.alfamir.data.dto.push_notification;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 20.04.2018.
 */

public class NotificationPost {
    @SerializedName("type")
    String type;
    @SerializedName("postid")
    String id;
    @SerializedName("posturl")
    String url;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
