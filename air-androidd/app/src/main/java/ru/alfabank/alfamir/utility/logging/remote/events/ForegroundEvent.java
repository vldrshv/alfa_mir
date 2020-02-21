package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 07.03.2018.
 */

public class ForegroundEvent extends BaseEvent {

    @SerializedName("eventname")
    String eventType = "foreground";

    public ForegroundEvent(){
        super();
    }
}
