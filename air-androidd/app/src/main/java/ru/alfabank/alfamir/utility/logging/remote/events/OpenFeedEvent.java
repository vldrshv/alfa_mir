package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 05.03.2018.
 */

public class OpenFeedEvent extends BaseEvent {

    @SerializedName("eventname")
    String eventType = "openfeed";

    public OpenFeedEvent(String id, String name){
        super();
        parameters.add(new EventParameters("id", id));
        parameters.add(new EventParameters("name", name));
    }
}
