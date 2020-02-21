package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 05.03.2018.
 */

public class FeedSubscribeEvent extends BaseEvent {
    @SerializedName("eventname")
    String eventType = "feedsubscribe";

    public FeedSubscribeEvent(String id, String name, boolean isSubscribed) {
        super();
        parameters.add(new BaseEvent.EventParameters("id", id));
        parameters.add(new BaseEvent.EventParameters("name", name));
        parameters.add(new BaseEvent.EventParameters("issubscribed", ""+isSubscribed));
    }
}