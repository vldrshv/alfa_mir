package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 06.03.2018.
 */

public class OpenMenuEvent extends BaseEvent {

    @SerializedName("eventname")
    String eventType = "openmenu";

    public OpenMenuEvent(){
        super();
    }
}
