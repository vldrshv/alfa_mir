package ru.alfabank.alfamir.data.dto.old_trash.models;

/**
 * Created by U_M0WY5 on 26.09.2017.
 */

public class MSearchedPage extends ModelSearchedItem {
    String id;
    String title;
    String siteurl;
    String type;
    String pubdate;
    String imageurl;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSiteurl() {
        return siteurl;
    }

    public String getPubdate() {
        return pubdate;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getType() {
        return type;
    }
}
