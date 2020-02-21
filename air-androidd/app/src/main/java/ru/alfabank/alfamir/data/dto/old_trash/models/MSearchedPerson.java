package ru.alfabank.alfamir.data.dto.old_trash.models;

/**
 * Created by U_M0WY5 on 26.09.2017.
 */

public class MSearchedPerson extends ModelSearchedItem{
    String name;
    String account;
    String imageurl;
    String jobtitle;

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account.toLowerCase();
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getJobtitle() {
        return jobtitle;
    }
}
