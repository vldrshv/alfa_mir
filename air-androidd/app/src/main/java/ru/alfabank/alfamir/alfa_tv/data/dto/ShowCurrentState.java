package ru.alfabank.alfamir.alfa_tv.data.dto;

import com.google.gson.annotations.SerializedName;

public class ShowCurrentState {
    @SerializedName("isstreaming")
    boolean mIsOnAir;

    public boolean isOnAir() {
        return mIsOnAir;
    }
}
