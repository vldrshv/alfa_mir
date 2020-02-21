package ru.alfabank.alfamir.messenger.data.dto;

import com.google.gson.annotations.SerializedName;

public class StatusRaw implements PollDataRaw.PollDataValue {
    @SerializedName("ChatID")
    String mChatId;
    @SerializedName("Status")
    String mState;
    @SerializedName("UserLogin")
    String mUserLogin;
    @SerializedName("TimeStamp")
    String mDate;
    @SerializedName("MessageID")
    long mMsgId;

    public String getChatId() {
        return mChatId;
    }

    public String getState() {
        return mState;
    }

    public String getUserLogin() {
        return mUserLogin;
    }

    public String getDate() {
        return mDate;
    }

    public long getMsgId() {
        return mMsgId;
    }
}
