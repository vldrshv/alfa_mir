package ru.alfabank.alfamir.data.dto.comment;

public class Author {
    String mId;
    String mName;
    String mPicLink;
    String mTitle;

    public Author(String id,
            String name,
            String picLink,
            String title){
        mId = id;
        mName = name;
        mPicLink = picLink;
        mTitle = title;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getPicLink() {
        return mPicLink;
    }

    public String getTitle() {
        return mTitle;
    }
}
