package ru.alfabank.alfamir.feed_new.presentation.dto;

public class ImageElement {
    private String mId;
    private String mUrl;
    private int mHeight;
    private int mWidth;

    private ImageElement(String id,
                         String url,
                         int height,
                         int width){
        mId = id;
        mUrl = url;
        mHeight = height;
        mWidth = width;
    }

    public String getId() {
        return mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    public static class Builder {
        private String mId;
        private String mUrl;
        private int mHeight;
        private int mWidth;

        public Builder id(String id){
            mId = id;
            return this;
        }

        public Builder url(String url){
            mUrl = url;
            return this;
        }

        public Builder height(int height){
            mHeight = height;
            return this;
        }

        public Builder width(int width){
            mWidth = width;
            return this;
        }

        public ImageElement build(){
            return new ImageElement(mId, mUrl, mHeight, mWidth);
        }
    }

}
