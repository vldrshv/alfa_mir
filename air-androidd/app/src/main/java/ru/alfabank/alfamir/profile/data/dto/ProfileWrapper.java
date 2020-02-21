package ru.alfabank.alfamir.profile.data.dto;

import com.google.gson.annotations.SerializedName;

public class ProfileWrapper {
    @SerializedName("userdata")
    ProfileRaw mProfileRaw;

    public ProfileRaw getProfileRaw() {
        return mProfileRaw;
    }
}
