package ru.alfabank.alfamir.messenger.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class StatusRawTest implements PollDataRaw.PollDataValue {
    @SerializedName("ChatID")
    String mChatId;
    @SerializedName("Status")
    String mState;
    @SerializedName("UserLogin")
    String mUserLogin;
    @SerializedName("MessageID")
    long mMsgId;
    @SerializedName("TimeStamp")
    Date mDate;

    public String getChatId() {
        return mChatId;
    }

    public String getState() {
        return mState;
    }

    public String getUserLogin() {
        return mUserLogin;
    }

    public long getMsgId() {
        return mMsgId;
    }

    public Date getDate() {
        return mDate;
    }
}
