package ru.alfabank.alfamir.data.dto.push_notification;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 20.04.2018.
 */

public class NotificationProfile {
    @SerializedName("type")
    String eventType;
    @SerializedName("userid")
    String id;

    public String getEventType() {
        return eventType;
    }

    public String getId() {
        return id.toLowerCase();
    }
}
