package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

public class ErrorEvent extends BaseEvent{

    @SerializedName("eventname")
    String eventType = "error";

    public ErrorEvent(String message, String stackTrace){
        super();
        parameters.add(new EventParameters("message", message));
        parameters.add(new EventParameters("stackTrace", stackTrace));
    }

}
