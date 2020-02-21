package ru.alfabank.alfamir.profile.data.dto;

import com.google.gson.annotations.SerializedName;

public class UserLikeStatusRaw {
    @SerializedName("state")
    int mIsLiked;
    @SerializedName("likescount")
    int mCurrentLikes;

    public int isLiked() {
        return mIsLiked;
    }

    public int getCurrentLikes() {
        return mCurrentLikes;
    }
}
