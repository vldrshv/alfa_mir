package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 05.03.2018.
 */

public class LoadMoreEvent extends BaseEvent {

    @SerializedName("eventname")
    String eventType = "nextitems";

    public LoadMoreEvent(int amount, int current){
        super();
        parameters.add(new BaseEvent.EventParameters("amount", "" + amount));
        parameters.add(new BaseEvent.EventParameters("current", "" + current));
    }
}
