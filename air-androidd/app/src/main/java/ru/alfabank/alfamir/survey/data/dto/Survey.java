package ru.alfabank.alfamir.survey.data.dto;

import com.google.gson.annotations.SerializedName;

import ru.alfabank.alfamir.utility.enums.FormatElement;
import ru.alfabank.alfamir.utility.static_utilities.DateConverter;

import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_0;
import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_2;
import static ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_MOSCOW;

public class Survey {

    @SerializedName("Entity")
    String type;
    @SerializedName("ID")
    int ID;
    @SerializedName("Header")
    Cover cover;
    @SerializedName("Questions")
    Question [] questions;
    @SerializedName("ResultScreen")
    Result result;

    public String getType() {
        return type;
    }

    public int getID() {
        return ID;
    }

    public Cover getCover() {
        return cover;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public Result getResult() {
        return result;
    }

    public class Cover {
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
        String author;
        @SerializedName("CreationDate")
        String createdDate;
        @SerializedName("ResponderLogin")
        String respondent;
        @SerializedName("RespondQuestionsCount")
        int respondentAnswersCount;
        @SerializedName("RespondDate")
        String answersDate;
        @SerializedName("EndAnswersCount")
        int requredAnswersCount;
        @SerializedName("HaveAudience")
        int isTargeted;
        @SerializedName("RespUsersCount")
        int participantsCount;
        @SerializedName("Passed")
        int isFinished;
        @SerializedName("LocalizedEntityName")
        String surveyName;

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

        public String getAuthor() {
            return author;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public String getRespondent() {
            return respondent;
        }

        public int getRespondentAnswersCount() {
            return respondentAnswersCount;
        }

        public String getAnswersDate() {
            return answersDate;
        }

        public int getRequredAnswersCount() {
            return requredAnswersCount;
        }

        public int getIsTargeted() {
            return isTargeted;
        }

        public int getParticipantsCount() {
            return participantsCount;
        }

        public int getIsFinished() {
            return isFinished;
        }

        public String getSurveyName() {
            return surveyName;
        }
    }

    public class Question {
        @SerializedName("ID")
        int id;
        @SerializedName("QuestionBody")
        QuestionBody body;
        @SerializedName("Answers")
        Answer [] answers;

        public int getId() {
            return id;
        }

        public QuestionBody getBody() {
            return body;
        }

        public Answer[] getAnswers() {
            return answers;
        }

        public class QuestionBody {
            @SerializedName("Entity")
            String type;
            @SerializedName("Text")
            String text;
            @SerializedName("PicURL")
            String imgUrl;
            @SerializedName("LogicQuestion")
            int logicQuestion;
            @SerializedName("RightAnswersCount")
            int rightAnswersCount;

            public String getType() {
                return type;
            }

            public String getText() {
                return text;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public int getLogicQuestion() {
                return logicQuestion;
            }

            public int getRightAnswersCount() {
                return rightAnswersCount;
            }
        }

        public class Answer {
            @SerializedName("ID")
            int ID;
            @SerializedName("AnswerBody")
            AnswerBody body;
            @SerializedName("UserContent")
            UserContent userContent;

            public int getID() {
                return ID;
            }

            public AnswerBody getBody() {
                return body;
            }

            public UserContent getUserContent() {
                return userContent;
            }

            public class AnswerBody {
                @SerializedName("Text")
                String text;
                @SerializedName("PicURL")
                String imgUrl;
                @SerializedName("NextQuestionId")
                int nextQuestion;
                @SerializedName("CorrectAnswer")
                int correctAnswer;
                @SerializedName("Description")
                String commentOnAnswer;
                @SerializedName("NeedTextBox")
                int textInput;
                @SerializedName("Points")
                int value;

                public String getText() {
                    return text;
                }

                public String getImgUrl() {
                    return imgUrl;
                }

                public int getNextQuestion() {
                    return nextQuestion;
                }

                public int getCorrectAnswer() {
                    return correctAnswer;
                }

                public int getValue() {
                    return value;
                }

                public String getCommentOnAnswer() { return commentOnAnswer; }

                public boolean hastTextInput() {
                    if(textInput==0){
                        return false;
                    } else if (textInput==1){
                        return true;
                    }
                    return false;
                }
            }

            public class UserContent {
                @SerializedName("TextAnswer")
                String answer;
                @SerializedName("Rating")
                int rating;
                @SerializedName("Checked")
                int isChecked;

                public String getAnswer() {
                    return answer;
                }

                public int getRating() {
                    return rating;
                }

                public int getIsChecked() {
                    return isChecked;
                }
            }
        }
    }

    public class Result {
        @SerializedName("RightAnswers")
        String quizResult;
        @SerializedName("TextMessage")
        String message;
        @SerializedName("WhatShow")
        ViewFlags viewFlags;

        public String getQuizResult() {
            return quizResult;
        }

        public String getMessage() {
            return message;
        }

        public ViewFlags getViewFlags() {
            return viewFlags;
        }

        public class ViewFlags {
            @SerializedName("RightAnswersCount")
            int quizResult;
            @SerializedName("FinalMessage")
            int message;
            @SerializedName("ResultAfterAnswer")
            int showResultAfterAnswer;
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

            public int getShowResultAfterAnswer() {
                return showResultAfterAnswer;
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

}
