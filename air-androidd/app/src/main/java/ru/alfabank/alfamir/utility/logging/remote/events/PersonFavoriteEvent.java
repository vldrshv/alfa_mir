package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 05.03.2018.
 */

public class PersonFavoriteEvent extends BaseEvent {
    @SerializedName("eventname")
    String eventType = "personfavorite";

    public PersonFavoriteEvent(String id, String name, boolean isFavoured) {
        super();
        parameters.add(new BaseEvent.EventParameters("id", id));
        parameters.add(new BaseEvent.EventParameters("name", name));
        parameters.add(new BaseEvent.EventParameters("isfavoured", ""+isFavoured));
    }
}
