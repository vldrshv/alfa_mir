package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 06.03.2018.
 */

public class CreatePostEvent extends BaseEvent {

    @SerializedName("eventname")
    String eventType = "send";

    public CreatePostEvent(String feedId, String name, String title){
        super();
        parameters.add(new EventParameters("feedid", feedId));
        parameters.add(new EventParameters("name", name));
        parameters.add(new EventParameters("title", title));
    }
}
