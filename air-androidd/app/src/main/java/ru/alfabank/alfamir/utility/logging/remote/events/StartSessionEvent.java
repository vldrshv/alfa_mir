package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 07.03.2018.
 */

public class StartSessionEvent extends BaseEvent {

    @SerializedName("eventname")
    String eventType = "startsession";

    public StartSessionEvent(String applicationid, String appversion, String model,
                             String certUserName, String login, String platform,
                             String timezone){
        super();
        parameters.add(new EventParameters("applicationid", applicationid));
        parameters.add(new EventParameters("appversion", appversion));
        parameters.add(new EventParameters("model", model));
        parameters.add(new EventParameters("certusername", certUserName));
        parameters.add(new EventParameters("login", login));
        parameters.add(new EventParameters("platform", platform));
        parameters.add(new EventParameters("timezone", timezone));

    }
}
