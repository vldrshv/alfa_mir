package ru.alfabank.alfamir.survey.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 26.04.2018.
 */

public class Answer {
    @SerializedName("questionid")
    int questionId;
    @SerializedName("answerid")
    int answerId;

    String text;

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getQuestionId() {
        return questionId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public String getText() {
        return text;
    }
}
