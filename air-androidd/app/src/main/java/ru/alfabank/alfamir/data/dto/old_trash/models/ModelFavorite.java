package ru.alfabank.alfamir.data.dto.old_trash.models;

import androidx.annotation.NonNull;

/**
 * Created by U_M0WY5 on 07.08.2017.
 */

public class ModelFavorite implements Comparable<ModelFavorite> {
    private String email;
    private String fullname;
    private String id;
    private String jobTitle;
    private String photobase64;
    private String workphone;
    private int ishead;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getId() {
        return id.toLowerCase();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPhotobase64() {
        return photobase64;
    }

    public void setPhotobase64(String photobase64) {
        this.photobase64 = photobase64;
    }

    public String getWorkphone() {
        return workphone;
    }

    public void setWorkphone(String workphone) {
        this.workphone = workphone;
    }

    public int getIshead() {
        return ishead;
    }

    public void setIshead(int ishead) {
        this.ishead = ishead;
    }

    @Override
    public int compareTo(@NonNull ModelFavorite o) {
        return fullname.compareTo(o.fullname);
    }
}
