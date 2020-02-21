package ru.alfabank.alfamir.data.dto.notifications_settings;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 23.05.2018.
 */

public class NotificationsSettings {

    @SerializedName("birthdays")
    int isBirthdaysActivated;
    @SerializedName("vacations")
    int isVacationsActivated;
    @SerializedName("news_posts")
    int isPostsActivated;
    @SerializedName("news_comments")
    int isCommentsActivated;

    public int getIsBirthdaysActivated() {
        return isBirthdaysActivated;
    }

    public void setIsBirthdaysActivated(int isBirthdaysActivated) {
        this.isBirthdaysActivated = isBirthdaysActivated;
    }

    public int getIsVacationsActivated() {
        return isVacationsActivated;
    }

    public void setIsVacationsActivated(int isVacationsActivated) {
        this.isVacationsActivated = isVacationsActivated;
    }

    public int getIsPostsActivated() {
        return isPostsActivated;
    }

    public void setIsPostsActivated(int isPostsActivated) {
        this.isPostsActivated = isPostsActivated;
    }

    public int getIsCommentsActivated() {
        return isCommentsActivated;
    }

    public void setIsCommentsActivated(int isCommentsActivated) {
        this.isCommentsActivated = isCommentsActivated;
    }

    public boolean isDisabled(){
        if(isBirthdaysActivated==0 && isVacationsActivated==0 && isPostsActivated==0 && isCommentsActivated==0){
            return true;
        }
        return false;
    }

}
