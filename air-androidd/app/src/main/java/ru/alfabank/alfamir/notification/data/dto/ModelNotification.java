package ru.alfabank.alfamir.notification.data.dto;

import com.google.gson.annotations.SerializedName;

import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.data.dto.old_trash.models.ModelAuthor;

/**
 * Created by U_M0WY5 on 21.12.2017.
 */

public class ModelNotification {

    @SerializedName("notificationid")
    private int id;
    @SerializedName("createtime")
    private String createTime;
    @SerializedName("openparams")
    private OpenParameters openParams;
    @SerializedName("messageparams")
    private MessageParameters [] messageParameters;
    @SerializedName("author")
    private ModelAuthor author;
    private String title;
    private String img;
    private int viewed;

    public String getTitle() {
        return title;
    }

    public String getImg() {

        return img;
    }

    public boolean checkIfThankYou(){
        if(openParams.getPosturl().equals("http://alfa/Info/Thanks")){
            return true;
        } else {
            return false;
        }
    }

    public String getCreateTime() {
        return createTime;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public int getNotificationid() {
        return id;
    }

    public OpenParameters getOpenParams() {
        return openParams;
    }

    public ModelAuthor getAuthor() {
        return author;
    }

    public int getType(){
        String notificationsType = openParams.getType();
        if(notificationsType.equals("birthday")){
            return Constants.Notification.NOTIFICATION_TYPE_BIRTHDAY;
        } else if(notificationsType.equals("vacation")) {
            return Constants.Notification.NOTIFICATION_TYPE_VACATION;
        } else if (notificationsType.equals("news")){
            if(openParams.getIscomment()==0){
                return Constants.Notification.NOTIFICATION_TYPE_NEWS;
            } else return Constants.Notification.NOTIFICATION_TYPE_NEWS_COMMENT;
        } else if (notificationsType.equals("blog")){
            if(openParams.getIscomment()==0){
                if(openParams.getPosturl().equals("http://alfa/Info/photostream")){
                    return Constants.Notification.NOTIFICATION_TYPE_MEDIA;
                } else {
                    return Constants.Notification.NOTIFICATION_TYPE_BLOG;
                }
            } else {
                if(openParams.getPosturl().equals("http://alfa/Info/photostream")){
                    return Constants.Notification.NOTIFICATION_TYPE_MEDIA_COMMENT;
                } else {
                    return Constants.Notification.NOTIFICATION_TYPE_BLOG_COMMENT;
                }
            }
        } else if (notificationsType.equals("community")){
            if(openParams.getIscomment()==0){
                return Constants.Notification.NOTIFICATION_TYPE_COMMUNITY;
            } return Constants.Notification.NOTIFICATION_TYPE_COMMUNITY_COMMENT;
        } else if (notificationsType.equals("survey")){
            return Constants.Notification.NOTIFICATION_TYPE_SURVEY;
        } else {
            return -1;
        }
    }

    public String getDepartment(){
        if(messageParameters!=null&&messageParameters.length!=0){
            for (MessageParameters msgparams : messageParameters){
                if (msgparams.getType().equals("sitename")){
                    return msgparams.getContent();
                }
            }
        }
        return null;
    }

    public String getPostTitle(){
        if(messageParameters!=null&&messageParameters.length!=0){
            for (MessageParameters msgparams : messageParameters){
                if (msgparams.getType().equals("postname")){
                    return msgparams.getContent();
                }
            }
        }
        return null;
    }

    public String getBirthday(){
        if(messageParameters!=null&&messageParameters.length!=0){
            for (MessageParameters msgparams : messageParameters){
                if (msgparams.getType().equals("birthdate")){
                    return msgparams.getContent();
                }
            }
        }
        return null;
    }

    public MessageParameters[] getMessageParameters() {
        return messageParameters;
    }

    public class OpenParameters {
        private String type;
        private String commentid;
        private String userid;
        private String postid;
        private String posturl;
        private int iscomment;

        public String getType() {
            return type;
        }

        public String getCommentid() {
            return commentid;
        }

        public String getUserId() {
            return userid;
        }

        public String getPostid() {
            return postid;
        }

        public String getPosturl() {
            return posturl;
        }

        public int getIscomment() {
            return iscomment;
        }

        public int getNotificationOpenType(){

            return 0;
        }

    }

    public class MessageParameters {
        private String type;
        private String content;

        public String getType() {
            return type;
        }

        public String getContent() {
            return content;
        }
    }

}
