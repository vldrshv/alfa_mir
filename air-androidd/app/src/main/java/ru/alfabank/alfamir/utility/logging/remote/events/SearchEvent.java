package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 05.03.2018.
 */

public class SearchEvent extends BaseEvent {

    @SerializedName("eventname")
    String eventType = "search";

    public SearchEvent(String phrase){
        super();
        parameters.add(new BaseEvent.EventParameters("searchphrase", phrase));
    }
}
