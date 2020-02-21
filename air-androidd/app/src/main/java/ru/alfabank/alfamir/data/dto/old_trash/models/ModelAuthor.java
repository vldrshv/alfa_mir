package ru.alfabank.alfamir.data.dto.old_trash.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 28.07.2017.
 */

public class ModelAuthor {
    String id;
    @SerializedName("fullname")
    String name;
    @SerializedName("photobase64")
    String picLink;
    @SerializedName("jobtitle")
    String title;

    public String getPicLink() {
        return picLink;
    }

    public void setPicLink(String picLink) {
        this.picLink = picLink;
    }

    public String getId() {
        if(id == null) {
            return null;
        }
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
}
