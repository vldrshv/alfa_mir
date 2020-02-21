package ru.alfabank.alfamir.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 22.02.2018.
 */

public class LikesStatus {
    @SerializedName("state")
    int isLiked;
    @SerializedName("likescount")
    int currentCount;

    public int getIsLiked() {
        return isLiked;
    }

    public int getCurrentCount() {
        return currentCount;
    }
}
