package ru.alfabank.alfamir.utility.logging.remote.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 18.05.2018.
 */

public class ShowSurveyQuestionEvent extends BaseEvent {
    @SerializedName("eventname")
    String eventType = "showsurveyquestion";

    public ShowSurveyQuestionEvent(String type, int surveyId, int questionId){
        super();
        parameters.add(new EventParameters("entity", type));
        parameters.add(new EventParameters("surveyid", ""+surveyId));
        parameters.add(new EventParameters("questionId", ""+questionId));
    }
}

