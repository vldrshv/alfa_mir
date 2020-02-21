package ru.alfabank.alfamir.notification.presentation.dto;

public class DisplayInfo {

    private String mType;
    private String mContent;

    private DisplayInfo(String type,
                        String content){
        mType = type;
        mContent = content;
    }

    public String getType() {
        return mType;
    }

    public String getContent() {
        return mContent;
    }

    public static class Builder{
        private String mType;
        private String mContent;

        public Builder type(String type){
            mType = type;
            return this;
        }

        public Builder content(String content){
            mContent = content;
            return this;
        }

        public DisplayInfo build(){
            return new DisplayInfo(mType, mContent);
        }

    }
}
