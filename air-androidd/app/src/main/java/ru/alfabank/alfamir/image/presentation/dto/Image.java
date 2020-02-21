package ru.alfabank.alfamir.image.presentation.dto;

public class Image {
    private String mPicUrl;
    private String mEncodedImage;
    private boolean mIsCached;

    private Image(String url,
                  int weight,
                  int height,
                  int isOriginal,
                  String encodedImage,
                  boolean isCached) {
        mPicUrl = url;
        mEncodedImage = encodedImage;
        mIsCached = isCached;
    }

    public String getEncodedImage() {
        return mEncodedImage;
    }

    public String getPicUrl() {
        return mPicUrl;
    }

    public boolean getIsCached() {
        return mIsCached;
    }

    public static class Builder {
        private String mPicUrl;
        private int mPicWidth;
        private int mPicHeight;
        private int mIsOriginal;
        private int mIsProfile;
        private String mEncodedImage;
        private boolean mIsCached;

        public Builder picUrl(String picUrl) {
            mPicUrl = picUrl;
            return this;
        }

        public Builder picWidth(int picWidth) {
            mPicWidth = picWidth;
            return this;
        }

        public Builder picHeight(int picHeight) {
            mPicHeight = picHeight;
            return this;
        }

        public Builder isOriginal(int isOriginal) {
            mIsOriginal = isOriginal;
            return this;
        }

        public Builder isCached(boolean isCached) {
            mIsCached = isCached;
            return this;
        }

        public Builder encodedImage(String encodedImage) {
            mEncodedImage = encodedImage;
            return this;
        }

        public Image build() {
            return new Image(mPicUrl,
                    mPicWidth,
                    mPicHeight,
                    mIsOriginal,
                    mEncodedImage,
                    mIsCached);
        }
    }
}
