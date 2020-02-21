package ru.alfabank.alfamir.data.dto.old_trash.models;

/**
 * Created by U_M0WY5 on 31.08.2017.
 */

public class ResponseUserInfo {
    String login;
    String fulname;
    String jobtitle;
    String photobase64;
    String city;

    public String getLogin() {
        return login;
    }

    public String getFulname() {
        return fulname;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public String getPhotobase64() {
        return photobase64;
    }

    public String getCity() {
        return city;
    }
}
