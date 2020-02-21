package ru.alfabank.alfamir.post.presentation.dto;

import static ru.alfabank.alfamir.Constants.Post.POST_EXTRA_SPACE;

public class SpaceElement implements PostElement {

    private static final int VIEW_TYPE = POST_EXTRA_SPACE;

    public int getViewType() {
        return VIEW_TYPE;
    }

}
