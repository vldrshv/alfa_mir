package ru.alfabank.alfamir.data.dto.old_trash.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 06.02.2018.
 */

public class FeedHeader {
    @SerializedName("title")
    String title;
    @SerializedName("description")
    String description;
    @SerializedName("ispersonal")
    int isPersonal;
    @SerializedName("coverimage")
    String cover;
    @SerializedName("coverimagebase64")
    String tempCover;
    @SerializedName("authorphoto")
    String profilePic;
    @SerializedName("type")
    String type;
    @SerializedName("id")
    String id;
    @SerializedName("issubscribed")
    int isSubscribed;


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getIsPersonal() {
        return isPersonal;
    }

    public String getCover() {
        return cover;
    }

    public String getTempCover() {
        return tempCover;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getIsSubscribed() {
        return isSubscribed;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}