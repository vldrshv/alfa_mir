package ru.alfabank.alfamir.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mshvdvsk on 26/02/2018.
 */

public class ProfileFullId {
    @SerializedName("login")
    String id;
    @SerializedName("fullname")
    String name;
    @SerializedName("jobtitle")
    String title;
    @SerializedName("photobase64")
    String photobase64;
    @SerializedName("city")
    String city;

    public String getId() {
        return id.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getPhotobase64() {
        return photobase64;
    }

    public String getCity() {
        return city;
    }
}
