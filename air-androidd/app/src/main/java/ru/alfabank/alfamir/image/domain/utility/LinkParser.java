package ru.alfabank.alfamir.image.domain.utility;

public interface LinkParser {

    /**
     *  Returns pic url and parameters that match the screen size
     */

    ResponseValue getImageParameters(String url, int targetDp);

    class ResponseValue {
        private String mUrl;
        private int mHeight;
        private int mWidth;
        ResponseValue(String url, int height, int width){
            mUrl = url;
            mHeight = height;
            mWidth = width;
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
    }

}
