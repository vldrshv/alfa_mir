package ru.alfabank.alfamir.post.presentation.dto;

import static ru.alfabank.alfamir.Constants.Post.POST_SINGLE_HTML;

public class SingleHtmlElement implements PostElement {

    private static final int VIEW_TYPE = POST_SINGLE_HTML;

    private String mHtml;

    public SingleHtmlElement(String html){
        mHtml = html;
    }

    public String getHtml() {
        return mHtml;
    }

    public int getViewType() {
        return VIEW_TYPE;
    }
}
