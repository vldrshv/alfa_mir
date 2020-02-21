package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 06.03.2018.
 */

public class OpenPostEvent extends BaseEvent {

    @SerializedName("eventname")
    String eventType = "openarticle";

    public OpenPostEvent(String id, String name){
        super();
        parameters.add(new EventParameters("id", id));
        parameters.add(new EventParameters("name", name));
    }
}
