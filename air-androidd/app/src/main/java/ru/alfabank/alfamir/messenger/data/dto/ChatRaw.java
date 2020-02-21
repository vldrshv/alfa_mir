package ru.alfabank.alfamir.messenger.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatRaw implements PollDataRaw.PollDataValue {

    @SerializedName("ChatID")
    private String mId;

    @SerializedName("FirstMessageID")
    private long mFirstMessageId;

    @SerializedName("FirstUnreadMessageID")
    private long mFirstUnreadMessageId;

    @SerializedName("LastMessageID")
    private long mLastMessageId;

    @SerializedName("Messages")
    private List<MessageRaw> mMessages;

    @SerializedName("Type")
    private String mType;

    @SerializedName("Users")
    private List<UserRaw> mUsers;

    public String getId() {
        return mId;
    }

    public String getType() {
        return mType;
    }

    public long getFirstUnreadMessageId() {
        return mFirstUnreadMessageId;
    }

    public long getFirstMessageId() {
        return mFirstMessageId;
    }

    public long getLastMessageId() {
        return mLastMessageId;
    }

    public List<UserRaw> getUsers() {
        return mUsers;
    }

    public List<MessageRaw> getMessages() {
        return mMessages;
    }

    public void setMessages(List<MessageRaw> mMessages) {
        this.mMessages = mMessages;
    }
}
