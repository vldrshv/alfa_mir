package ru.alfabank.alfamir.data.dto.push_notification;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 14.05.2018.
 */

public class NotificationSurvey {
    @SerializedName("type")
    String type;
    @SerializedName("surveyid")
    String id;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
