package ru.alfabank.alfamir.feed_new.presentation.dto;

public class UriElement {
    private String mId;
    private String mReferenceType;
    private String mUri;

    private UriElement(String id,
                       String referenceType,
                       String uri){
        mId = id;
        mReferenceType = referenceType;
        mUri = uri;
    }

    public String getId() {
        return mId;
    }

    public String getReferenceType() {
        return mReferenceType;
    }

    public String getUri() {
        return mUri;
    }

    public static class Builder {
        private String mId;
        private String mReferenceType;
        private String mUri;

        public Builder id(String id){
            mId = id;
            return this;
        }

        public Builder referenceType(String referenceType){
            mReferenceType = referenceType;
            return this;
        }

        public Builder uri(String uri){
            mUri = uri;
            return this;
        }

        public UriElement build(){
            return new UriElement(mId, mReferenceType, mUri);
        }
    }

}
