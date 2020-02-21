package ru.alfabank.alfamir.post.presentation.dto;

import static ru.alfabank.alfamir.Constants.Post.POST_HTML;

public class HtmlElement implements PostElement {

    private static final int VIEW_TYPE = POST_HTML;

    private String mHtml;

    public HtmlElement(String html){
        mHtml = html;
    }

    public String getHtml() {
        return mHtml;
    }

    public int getViewType() {
        return VIEW_TYPE;
    }
}
