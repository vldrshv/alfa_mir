package ru.alfabank.alfamir.feed.data.dto;

import com.google.gson.annotations.SerializedName;

import ru.alfabank.alfamir.feed.presentation.dto.FeedElement;

import static ru.alfabank.alfamir.Constants.Feed_element.FEED_HEADER;

public class HeaderRawOld implements FeedElement {

    private static final int VIEW_TYPE = FEED_HEADER;

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

    @Override
    public int getViewType() {
        return VIEW_TYPE;
    }
}
