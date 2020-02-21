package ru.alfabank.alfamir.feed_new.presentation.dto;

public class PostInteractionElement {
    private ImageElement[] mImageArray;
    private VideoElement[] mVideoArray;
    private UriElement[] mUriArray;

    private PostInteractionElement(ImageElement[] imageArray,
                                   VideoElement[] videoArray,
                                   UriElement[] uriArray){
        mImageArray = imageArray;
        mVideoArray = videoArray;
        mUriArray = uriArray;
    }

    public ImageElement[] getImageArray() {
        return mImageArray;
    }

    public VideoElement[] getVideoArray() {
        return mVideoArray;
    }

    public UriElement[] getUriArray() {
        return mUriArray;
    }

    public static class Builder {
        private ImageElement[] mImageArray;
        private VideoElement[] mVideoArray;
        private UriElement[] mUriArray;

        public Builder imageArray(ImageElement[] imageArray){
            mImageArray = imageArray;
            return this;
        }

        public Builder videoArray(VideoElement[] videoArray){
            mVideoArray = videoArray;
            return this;
        }

        public Builder uriArray(UriElement[] uriArray){
            mUriArray = uriArray;
            return this;
        }

        public PostInteractionElement build(){
            return new PostInteractionElement(mImageArray, mVideoArray, mUriArray);
        }
    }

}
