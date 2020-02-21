package ru.alfabank.alfamir.survey.data.dto;

import com.google.gson.annotations.SerializedName;
import ru.alfabank.alfamir.utility.enums.FormatElement;
import ru.alfabank.alfamir.utility.static_utilities.DateConverter;

import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_0;
import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_2;
import static ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_MOSCOW;

/**
 * Created by U_M0WY5 on 15.05.2018.
 */

public class SurveyCover {
    @SerializedName("Entity")
    String type;
    @SerializedName("ID")
    int id;
    @SerializedName("Title")
    String title;
    @SerializedName("StartDate")
    String startDate;
    @SerializedName("EndDate")
    String endDate;
    @SerializedName("Pin")
    String pin;
    @SerializedName("Description")
    String description;
    @SerializedName("BackgroundImgUrl")
    String imgUrl;
    @SerializedName("AuthorLogin")
    String authorId;
    @SerializedName("CreationDate")
    String createdDate;
    @SerializedName("EndAnswersCount")
    int requiredAnswerCount;
    @SerializedName("HaveAudience")
    int isTargeted;

    @SerializedName("WhatShow")
    ViewFlags viewFlags;

    @SerializedName("RespondQuestionsCount")
    int answeredQuestionsCount;
    @SerializedName("LocalizedEntityName")
    String localizedName;
    @SerializedName("Passed")
    int completed;

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        String result = DateConverter.formatDate(endDate,
                DATE_PATTERN_0, DATE_PATTERN_2, TIME_ZONE_MOSCOW, FormatElement.TV_SHOW_CURRENT_START_DATE);
        return result;
    }

    public String getPin() {
        return pin;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public int getRequiredAnswerCount() {
        return requiredAnswerCount;
    }

    public int getIsTargeted() {
        return isTargeted;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public int getCompleted() {
        return completed;
    }

    public ViewFlags getViewFlags() {
        return viewFlags;
    }

    public int getAnsweredQuestionsCount() {
        return answeredQuestionsCount;
    }

    public class ViewFlags {
        @SerializedName("RightAnswersCount")
        int quizResult;
        @SerializedName("FinalMessage")
        int message;
        @SerializedName("ResultAfterAnswer")
        int answerStatisticsNumber;
        @SerializedName("Percents")
        int answerStatisticsPercent;
        @SerializedName("OverallResult")
        int questionsWithRightAnswers;
        @SerializedName("RightAnswers")
        int rightAnswer;
        @SerializedName("Description")
        int answerComment;
        @SerializedName("Infoscreen")
        int introScreen;

        public int getQuizResult() {
            return quizResult;
        }

        public int getMessage() {
            return message;
        }

        public int getAnswerStatisticsNumber() {
            return answerStatisticsNumber;
        }

        public int getAnswerStatisticsPercent() {
            return answerStatisticsPercent;
        }

        public int getQuestionsWithRightAnswers() {
            return questionsWithRightAnswers;
        }

        public int getRightAnswer() {
            return rightAnswer;
        }

        public int getAnswerComment() {
            return answerComment;
        }

        public int getIntroScreen() {
            return introScreen;
        }
    }

}
