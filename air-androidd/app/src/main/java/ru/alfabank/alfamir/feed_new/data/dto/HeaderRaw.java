package ru.alfabank.alfamir.feed_new.data.dto;

import com.google.gson.annotations.SerializedName;

public class HeaderRaw {
    @SerializedName("title")
    String mTitle;
    @SerializedName("description")
    String mDescription;
    @SerializedName("ispersonal")
    int mIsPersonal;
    @SerializedName("coverimage")
    String mImageUrl;
    @SerializedName("coverimagebase64")
    String mPlaceholderImage;
    @SerializedName("authorphoto")
    String mAuthorImageUrl;
    @SerializedName("pubtime")
    String mPubTime;
    @SerializedName("type")
    String mFeedType;
    @SerializedName("id")
    String mFeedUrl;
    @SerializedName("issubscribed")
    int mIsSubscribed;
    @SerializedName("checkrights")
    int mCanPublish;

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getIsPersonal() {
        return mIsPersonal;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getPlaceholderImage() {
        return mPlaceholderImage;
    }

    public String getAuthorImageUrl() {
        return mAuthorImageUrl;
    }

    public String getPubTime() {
        return mPubTime;
    }

    public String getFeedType() {
        return mFeedType;
    }

    public String getFeedUrl() {
        return mFeedUrl;
    }

    public int getIsSubscribed() {
        return mIsSubscribed;
    }

    public int getCanPublish() {
        return mCanPublish;
    }
}
