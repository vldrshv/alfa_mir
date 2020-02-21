package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 18.05.2018.
 */

public class HideSurveyEvent extends BaseEvent {
    @SerializedName("eventname")
    String eventType = "hidesurvey";

    public HideSurveyEvent(String type, int surveyId){
        super();
        parameters.add(new EventParameters("entity", type));
        parameters.add(new EventParameters("surveyid", ""+surveyId));
    }
}
