package ru.alfabank.alfamir.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 12.04.2018.
 */

public class ShortProfile {
    @SerializedName("id")
    String id;
    @SerializedName("fullname")
    String name;
    @SerializedName("jobTitle")
    String title;
    @SerializedName("photobase64")
    String imageUrl;
    @SerializedName("ishead")
    int isManager;

    String binaryImage;

    public String getBinaryImage() {
        return binaryImage;
    }

    public void setBinaryImage(String binaryImage) {
        this.binaryImage = binaryImage;
    }

    public String getId() {
        return id.toLowerCase();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsManager() {
        return isManager;
    }

    public void setIsManager(int isManager) {
        this.isManager = isManager;
    }
}
