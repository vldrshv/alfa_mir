package ru.alfabank.alfamir.profile.presentation.dto;

import com.google.common.base.Strings;

public class FunctionalManager {
    private String mLogin;
    private String mName;
    private String mPicUrl;
    private String mTitle;

    private FunctionalManager(String login,
                                  String name,
                                  String picUrl,
                                  String title){
        mLogin = login;
        mName = name;
        mPicUrl = picUrl;
        mTitle = title;
    }

    public String getLogin() {
        return mLogin;
    }

    public String getName() {
        return mName;
    }

    public String getPicUrl() {
        return mPicUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public static class Builder {
        private String mLogin;
        private String mName;
        private String mPicUrl;
        private String mTitle;

        public Builder login(String login){
            mLogin = login;
            if(Strings.isNullOrEmpty(mLogin)) mLogin = "";
            return this;
        }

        public Builder name(String name){
            mName = name;
            if(Strings.isNullOrEmpty(mName)) mName = "";
            return this;
        }

        public Builder picUrl(String picUrl){
            mPicUrl = picUrl;
            if(Strings.isNullOrEmpty(mPicUrl)) mPicUrl = "";
            return this;
        }

        public Builder title(String title){
            mTitle = title;
            if(Strings.isNullOrEmpty(mTitle)) mTitle = "";
            return this;
        }

        public FunctionalManager build(){
            return new FunctionalManager(mLogin,
                    mName,
                    mPicUrl,
                    mTitle);
        }

    }
}
