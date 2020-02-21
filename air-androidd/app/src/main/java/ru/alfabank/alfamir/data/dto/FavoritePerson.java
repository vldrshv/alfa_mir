package ru.alfabank.alfamir.data.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mshvdvsk on 25/02/2018.
 */

public class FavoritePerson implements Comparable<FavoritePerson> {
    @SerializedName("email")
    private String email;
    @SerializedName("fullname")
    private String name;
    @SerializedName("id")
    private String id;
    @SerializedName("jobtitle")
    private String title;
    @SerializedName("photobase64")
    private String imageUrl;
    @SerializedName("workphone")
    private String phone;
    @SerializedName("ishead")
    private int isManager;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id.toLowerCase();
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsManager() {
        return isManager;
    }

    public void setIsManager(int isManager) {
        this.isManager = isManager;
    }

    @Override
    public int compareTo(@NonNull FavoritePerson o) {
        return name.compareTo(o.name);
    }
}