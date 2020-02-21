package ru.alfabank.alfamir.messenger.data.dto;

import com.google.gson.annotations.SerializedName;

public class ChatLightRaw {

    @SerializedName("Type")
    private String mType;

    @SerializedName("Title")
    private String mTitle;

    @SerializedName("PicUrl")
    private String mCorrespondentPicUrl;

    @SerializedName("JobTitle")
    private String mCorrespondentTitle;

    @SerializedName("ChatID")
    private String mId;

    @SerializedName("FirstUnreadMessageID")
    private String mFirstUnreadMessageID;

    @SerializedName("LastMessageID")
    private String mLastMessageID;

    @SerializedName("LastMessageValue")
    private String mLastMessage;

    @SerializedName("LastMessageDate")
    private String mLastMessageDate;

    @SerializedName("LastMessageUserLogin")
    private String mLastMessageUserLogin;

    @SerializedName("LastMessageUserFullName")
    private String mLastMessageUserName;

    @SerializedName("LastMessageKey")
    private String mLastMessageKey;

    @SerializedName("LastMessageVector")
    private String mLastMessageVector;

    @SerializedName("UnreadMessageCount")
    private int mUnreadCount;

    @SerializedName("UserLogin")
    private String mCorrespondentLogin;

    @SerializedName("UserStatus")
    private int mCorrespondentOnlineStatus;

    public String getId() {
        return mId;
    }

    public String getType() {
        return mType;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLastMessage() {
        return mLastMessage;
    }

    public String getLastMessageDate() {
        return mLastMessageDate;
    }

    public String getLastMessageUserLogin() {
        return mLastMessageUserLogin;
    }

    public String getLastMessageUserName() {
        return mLastMessageUserName;
    }

    public String getCorrespondentLogin() {
        return mCorrespondentLogin;
    }

    public String getCorrespondentPicUrl() {
        return mCorrespondentPicUrl;
    }

    public String getCorrespondentTitle() {
        return mCorrespondentTitle;
    }

    public int getCorrespondentOnlineStatus() {
        return mCorrespondentOnlineStatus;
    }

    public int getUnreadCount() {
        return mUnreadCount;
    }

    public String getKey() {
        return mLastMessageKey;
    }

    public String getVector() {
        return mLastMessageVector;
    }

    public void setLastMessage(String message) {
        mLastMessage = message;
    }
}
