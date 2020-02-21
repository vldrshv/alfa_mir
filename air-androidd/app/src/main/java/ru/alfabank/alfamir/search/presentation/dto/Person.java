package ru.alfabank.alfamir.search.presentation.dto;

import java.util.UUID;

import static ru.alfabank.alfamir.Constants.Search.VIEW_TYPE_PERSON;

public class Person implements DisplayableSearchItem{

    private static final int VIEW_TYPE = VIEW_TYPE_PERSON;

    private String mName;
    private String mId;
    private String mImageUrl;
    private String mPosition;
    private long mViewId;

    private Person(String name,
                   String id,
                   String imageUrl,
                   String position,
                   long viewId){
        mName = name;
        mId = id;
        mImageUrl = imageUrl;
        mPosition = position;
        mViewId = viewId;
    }

    public String getName() {
        return mName;
    }

    public String getId() {
        return mId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getPosition() {
        return mPosition;
    }

    @Override
    public int getType() {
        return VIEW_TYPE;
    }

    @Override
    public long getViewId() {
        return mViewId;
    }

    public static class Builder {
        private String mName;
        private String mId;
        private String mImageUrl;
        private String mPosition;
        private long mViewId;


        public Builder name(String name){
            mName = name;
            return this;
        }

        public Builder id(String id){
            mId = id;
            return this;
        }
        public Builder imageUrl(String imageUrl){
            mImageUrl = imageUrl;
            return this;
        }
        public Builder posiiton(String position){
            mPosition = position;
            return this;
        }

        public Person build(){
            mViewId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
            return new Person(mName, mId, mImageUrl, mPosition, mViewId);
        }
    }
}
