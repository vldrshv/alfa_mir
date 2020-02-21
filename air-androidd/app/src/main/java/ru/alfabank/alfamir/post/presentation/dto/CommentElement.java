package ru.alfabank.alfamir.post.presentation.dto;

import ru.alfabank.alfamir.data.dto.comment.Author;

import static ru.alfabank.alfamir.Constants.Post.POST_COMMENT;

public class CommentElement implements PostElement {

    private static final int VIEW_TYPE = POST_COMMENT;

    private String mText;
    private String mId;
    private String mParentId;
    private String mDate;
    private int mDeletable;
    private int mUserLike;
    private int mLikes;
    private Author mAuthor;

    public CommentElement(String text,
                          String date,
                          String id,
                          String parentId,
                          int deletable,
                          int userLike,
                          int likes,
                          Author author){
        mText = text;
        mDate = date;
        mId = id;
        mParentId = parentId;
        mDeletable = deletable;
        mUserLike = userLike;
        mLikes = likes;
        mAuthor = author;
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE;
    }

    public String getText() {
        return mText;
    }

    public String getId() {
        return mId;
    }

    public String getParentId() {
        return mParentId;
    }

    public String getDate() {
        return mDate;
    }

    public int getDeletable() {
        return mDeletable;
    }

    public int getUserLike() {
        return mUserLike;
    }

    public int getLikes() {
        return mLikes;
    }

    public Author getAuthor() {
        return mAuthor;
    }

    public void setUserLike(int mUserLike) {
        this.mUserLike = mUserLike;
    }

    public void setLikes(int mLikes) {
        this.mLikes = mLikes;
    }

    public boolean getIsSecondLevel(){
        if(mParentId.equals("0")){
            return false;
        } else return true;
    }

}
