package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.utility.static_utilities.DateConverter;

import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_8;
import static ru.alfabank.alfamir.Constants.Logger.SENDING_STATUS_STORED;

/**
 * Created by U_M0WY5 on 05.03.2018.
 */


public class BaseEvent implements Event {
    @SerializedName("date")
    String date;
    @SerializedName("user")
    String id;
    @SerializedName("platform")
    String platform;
    @SerializedName("idsession")
    String sessionId;
    @SerializedName("parameters")
    List<EventParameters> parameters;
    transient int state; // transient means hidden during serialization

    public BaseEvent() {
        date = DateConverter.convertToUtcDate(new Date().getTime(), DATE_PATTERN_8);
        id = Constants.Initialization.INSTANCE.getUSER_ID(); // TODO check for null may be?
        platform = "android";
        sessionId = Constants.Initialization.INSTANCE.getSESSION_ID(); // TODO check for null may be?
        parameters = new ArrayList<>();
        state = SENDING_STATUS_STORED;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int getState() {
        return state;
    }

    public class EventParameters {
        @SerializedName("paramname")
        String name;
        @SerializedName("paramvalue")
        String value;

        public EventParameters (String name, String value){
            this.name = name;
            this.value = value;
        }
    }

}
