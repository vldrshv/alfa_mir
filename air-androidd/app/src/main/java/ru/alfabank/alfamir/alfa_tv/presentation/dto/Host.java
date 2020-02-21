package ru.alfabank.alfamir.alfa_tv.presentation.dto;

public class Host {
    private String mLogin;
    private String mName;
    private String mInitials;
    private String mPicLink;
    private String mTitle;

    private Host(String login,
             String name,
                 String initials,
             String picLink,
             String title){
        mLogin = login;
        mName = name;
        mInitials = initials;
        mPicLink = picLink;
        mTitle = title;
    }

    public String getLogin() {
        return mLogin;
    }

    public String getName() {
        return mName;
    }

    public String getInitials() {
        return mInitials;
    }

    public String getPicLink() {
        return mPicLink;
    }

    public String getTitle() {
        return mTitle;
    }

    public static class Builder{
        private String mLogin;
        private String mName;
        private String mInitials;
        private String mPicLink;
        private String mTitle;

        public Builder login(String login){
            mLogin = login;
            return this;
        }

        public Builder name(String name){
            mName = name;
            return this;
        }

        public Builder initials(String initials){
            mInitials = initials;
            return this;
        }

        public Builder picLink(String picLink){
            mPicLink = picLink;
            return this;
        }

        public Builder title(String title){
            mTitle = title;
            return this;
        }

        public Host build(){
            return new Host(mLogin, mName, mInitials, mPicLink, mTitle);
        }
    }
}
