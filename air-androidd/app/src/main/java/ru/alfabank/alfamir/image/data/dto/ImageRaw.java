package ru.alfabank.alfamir.image.data.dto;

public class ImageRaw {
    private String mPicUrl;
    private int mPicWidth;
    private int mPicHeight;
    private int mIsOriginal;
    private String mEncodedImage;
    private boolean mIsCached;

    private ImageRaw(String url,
                     int weight,
                     int height,
                     int isOriginal,
                     String encodedImage){
        mPicUrl = url;
        mPicWidth = weight;
        mPicHeight = height;
        mIsOriginal = isOriginal;
        mEncodedImage = encodedImage;
    }

    public String getEncodedImage() {
        return mEncodedImage;
    }

    public String getImageUrl() {
        return mPicUrl;
    }

    public int getImageWidth() {
        return mPicWidth;
    }

    public int getImageHeight() {
        return mPicHeight;
    }

    public int getIsOriginal() {
        return mIsOriginal;
    }

    public boolean getIsCached() {
        return mIsCached;
    }

    public void setIsCached(boolean mIsCached) {
        this.mIsCached = mIsCached;
    }

    public static class Builder {
        private String mPicUrl;
        private int mPicWidth;
        private int mPicHeight;
        private int mIsOriginal;
        private int mIsProfile;
        private String mEncodedImage;

        public Builder picUrl(String picUrl){
            mPicUrl = picUrl;
            return this;
        }

        public Builder picWidth(int picWidth){
            mPicWidth = picWidth;
            return this;
        }

        public Builder picHeight(int picHeight){
            mPicHeight = picHeight;
            return this;
        }

        public Builder isOriginal(int isOriginal){
            mIsOriginal = isOriginal;
            return this;
        }

        public Builder encodedImage(String encodedImage){
            mEncodedImage = encodedImage;
            return this;
        }



        public ImageRaw build(){
            return new ImageRaw(mPicUrl,
                    mPicWidth,
                    mPicHeight,
                    mIsOriginal,
                    mEncodedImage);
        }

    }

}
