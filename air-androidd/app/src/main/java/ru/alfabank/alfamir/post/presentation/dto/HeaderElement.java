package ru.alfabank.alfamir.post.presentation.dto;

import static ru.alfabank.alfamir.Constants.Post.POST_HEADER;

public class HeaderElement implements PostElement{

    private static final int VIEW_TYPE = POST_HEADER;

    private String mAuthorId;
    private String mAuthorName;
    private String mAuthorInitials;
    private String mUnformattedAvatarUrl;
    private String mDate;
    private String mHeadingTitle;
    private String mTitle;
    private int mOptionsMenuEnabled;
    private int mHeadingVisible;
    private int mTitleVisible;

    public HeaderElement(String authorId,
                         String authorName,
                         String authorInitials,
                         String unformattedAvatarUrl,
                         String date,
                         String headingTitle,
                         String title,
                         int optionsMenuEnabled,
                         int headingVisible,
                         int titleVisible){
        mAuthorId = authorId;
        mAuthorName = authorName;
        mAuthorInitials = authorInitials;
        mUnformattedAvatarUrl = unformattedAvatarUrl;
        mDate = date;
        mHeadingTitle = headingTitle;
        mTitle = title;
        mOptionsMenuEnabled = optionsMenuEnabled;
        mHeadingVisible = headingVisible;
        mTitleVisible = titleVisible;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public String getAuthorInitials() {
        return mAuthorInitials;
    }

    public String getUnformattedAvatarUrl() {
        return mUnformattedAvatarUrl;
    }

    public String getDate() {
        return mDate;
    }

    public String getHeadingTitle() {
        return mHeadingTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getViewType() {
        return VIEW_TYPE;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public int getOptionsMenuEnabled() {
        return mOptionsMenuEnabled;
    }

    public int getHeadingVisible() {
        return mHeadingVisible;
    }

    public int getTitleVisible() {
        return mTitleVisible;
    }
}
