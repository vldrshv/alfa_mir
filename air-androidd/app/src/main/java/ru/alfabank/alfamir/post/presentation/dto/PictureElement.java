package ru.alfabank.alfamir.post.presentation.dto;

import static ru.alfabank.alfamir.Constants.Post.POST_PICTURE;

public class PictureElement implements PostElement {

    private static final int VIEW_TYPE = POST_PICTURE;

    private String mUrl;
    private int mHeight;
    private int mWidth;

    public PictureElement(String url,
                          int height,
                          int width){
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

    public int getViewType() {
        return VIEW_TYPE;
    }
}
