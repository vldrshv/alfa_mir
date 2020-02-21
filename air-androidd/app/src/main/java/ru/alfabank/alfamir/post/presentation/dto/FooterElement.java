package ru.alfabank.alfamir.post.presentation.dto;

import static ru.alfabank.alfamir.Constants.Post.POST_FOOTER;

public class FooterElement implements PostElement {

    private static final int VIEW_TYPE = POST_FOOTER;

    private int mLikes;
    private int mCurrentUserLike;
    private int mComments;
    private int mLikesEnabled;
    private int mCommentsEnabled;

    public FooterElement(int likes,
                         int currentUserLike,
                         int comments,
                         int likesEnabled,
                         int commentsEnabled){
        mLikes = likes;
        mCurrentUserLike = currentUserLike;
        mComments = comments;

        mLikesEnabled = likesEnabled;
        mCommentsEnabled = commentsEnabled;
    }

    public int getLikes() {
        return mLikes;
    }

    public int getCurrentUserLike() {
        return mCurrentUserLike;
    }

    public int getComments() {
        return mComments;
    }

    public int getLikesEnabled() {
        return mLikesEnabled;
    }

    public int getCommentsEnabled() {
        return mCommentsEnabled;
    }

    public int getViewType() {
        return VIEW_TYPE;
    }

}
