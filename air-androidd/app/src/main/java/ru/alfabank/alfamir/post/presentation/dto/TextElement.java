package ru.alfabank.alfamir.post.presentation.dto;


import static ru.alfabank.alfamir.Constants.Post.POST_TEXT;


public class TextElement implements PostElement {

    private int VIEW_TYPE;

    private String mText;

    public TextElement(String text){
        mText = text;
        VIEW_TYPE = POST_TEXT;
    }
    public TextElement(String text, int type) {
        mText = text;
        VIEW_TYPE = type;
    }

    public String getText() {
        return mText;
    }

    public int getViewType() {
        return this.VIEW_TYPE;
    }
}
