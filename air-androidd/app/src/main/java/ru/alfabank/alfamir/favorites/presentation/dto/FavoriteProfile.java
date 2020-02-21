package ru.alfabank.alfamir.favorites.presentation.dto;

public class FavoriteProfile {
    private String mEmail;
    private String mName;
    private String mLogin;
    private String mTitle;
    private String mImageUrl;
    private String mWorkPhone;

    private FavoriteProfile(
            String email,
            String name,
            String login,
            String title,
            String imageUrl,
            String workPhone){
        mEmail = email;
        mName = name;
        mLogin = login;
        mTitle = title;
        mImageUrl = imageUrl;
        mWorkPhone = workPhone;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getName() {
        return mName;
    }

    public String getLogin() {
        return mLogin;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getWorkPhone() {
        return mWorkPhone;
    }

    public static class Builder {
        private String mEmail;
        private String mName;
        private String mLogin;
        private String mTitle;
        private String mImageUrl;
        private String mWorkPhone;

        public Builder email(String email){
            mEmail = email;
            return this;
        }

        public Builder name(String name){
            mName = name;
            return this;
        }

        public Builder login(String login){
            mLogin = login;
            return this;
        }

        public Builder title(String title){
            mTitle = title;
            return this;
        }

        public Builder imageUrl(String imageUrl){
            mImageUrl = imageUrl;
            return this;
        }

        public Builder workPhone(String workPhone){
            mWorkPhone = workPhone;
            return this;
        }

        public FavoriteProfile build(){
            return new FavoriteProfile(
                    mEmail,
                    mName,
                    mLogin,
                    mTitle,
                    mImageUrl,
                    mWorkPhone);
        }

    }

}
