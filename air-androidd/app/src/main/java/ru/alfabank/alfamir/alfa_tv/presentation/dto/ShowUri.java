package ru.alfabank.alfamir.alfa_tv.presentation.dto;

public class ShowUri {
    private String mUri;

    ShowUri(String uri){
        mUri = uri;
    }

    public String getUri() {
        return mUri;
    }

    public static class Builder {
        private String mUri;

        public Builder uri(String uri){
            mUri = uri;
            return this;
        }

        public ShowUri build(){
            return new ShowUri(mUri);
        }
    }
}
