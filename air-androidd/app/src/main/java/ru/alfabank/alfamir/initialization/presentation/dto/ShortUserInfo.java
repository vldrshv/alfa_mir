package ru.alfabank.alfamir.initialization.presentation.dto;

public class ShortUserInfo {

    private String mId;
    private String mName;
    private String mTitle;
    private String mEncodedImage;
    private String mCity;

    private ShortUserInfo(String id, String name, String title, String encodedImage, String city){
        mId = id;
        mName = name;
        mTitle = title;
        mEncodedImage = encodedImage;
        mCity = city;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getEncodedImage() {
        return mEncodedImage;
    }

    public String getCity() {
        return mCity;
    }

    public static class Builder {
        private String mId;
        private String mName;
        private String mTitle;
        private String mEncodedImage;
        private String mCity;

        public Builder id(String id){
            mId = id;
            return this;
        }

        public Builder name(String name){
            mName = name;
            return this;
        }

        public Builder title(String title){
            mTitle = title;
            return this;
        }

        public Builder encodedImage(String encodedImage){
            mEncodedImage = encodedImage;
            return this;
        }

        public Builder city(String city){
            mCity = city;
            return this;
        }

        public ShortUserInfo build(){
            return new ShortUserInfo(mId, mName, mTitle, mEncodedImage, mCity);
        }
    }

}
