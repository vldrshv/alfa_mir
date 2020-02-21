package ru.alfabank.alfamir.main.home_fragment.presentation.dto;

public class TopNews {

    private String mUrl;
    private String mTitle;
    private String mPicUrl;

    private TopNews(String url,
                    String title,
                    String picUrl){
        mUrl = url;
        mTitle = title;
        mPicUrl = picUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPicUrl() {
        return mPicUrl;
    }

    public static class Builder {

        private String mUrl;
        private String mTitle;
        private String mPicUrl;

        public Builder url(String url){
            mUrl = url;
            return this;
        }

        public Builder title(String title){
            mTitle = title;
            return this;
        }

        public Builder picUrl(String picUrl){
            mPicUrl = picUrl;
            return this;
        }

        public TopNews build(){
            return new TopNews(mUrl, mTitle, mPicUrl);
        }

    }

}
