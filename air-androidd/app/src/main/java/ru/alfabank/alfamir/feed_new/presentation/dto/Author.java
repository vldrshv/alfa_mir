package ru.alfabank.alfamir.feed_new.presentation.dto;

public class Author {
    private String mLogin;
    private String mName;
    private String mImageUrl;
    private String mTitle;

    private Author(String login,
                   String name,
                   String imageUrl,
                   String title) {
        mLogin = login;
        mName = name;
        mImageUrl = imageUrl;
        mTitle = title;
    }

    public String getLogin() {
        return mLogin;
    }

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public static class Builder {
        private String mLogin;
        private String mName;
        private String mImageUrl;
        private String mTitle;

        public Builder login(String login){
            mLogin = login;
            return this;
        }

        public Builder name(String name){
            mName = name;
            return this;
        }

        public Builder imageUrl(String imageUrl){
            mImageUrl = imageUrl;
            return this;
        }

        public Builder title(String title){
            mTitle = title;
            return this;
        }

        public Author build(){
            return new Author(mLogin, mName, mImageUrl, mTitle);
        }
    }
}
