package ru.alfabank.alfamir.messenger.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageRaw implements PollDataRaw.PollDataValue {

    @SerializedName("Attachment")
    private List<Attachment> attachments;

    @SerializedName("ChatID")
    private String mChatId;

    @SerializedName("ToUser")
    private String mRecipientId;

    @SerializedName("Value")
    private String mText;

    @SerializedName("CreationDate")
    private String mDate;

    @SerializedName("CodeKey")
    private String mCodeKey;

    @SerializedName("CodeVector")
    private String mCodeVector;

    @SerializedName("ID")
    private long mId;

    @SerializedName("FromUser")
    private UserRaw mSenderUserRaw;

    @SerializedName("Status")
    private List<StatusRaw> mStatusRaw;

    public long getId() {
        return mId;
    }

    public String getChatId() {
        return mChatId;
    }

    public String getRecipientId() {
        return mRecipientId;
    }

    public String getText() {
        return mText;
    }

    public String getDate() {
        return mDate;
    }

    public String getCodeKey() {
        return mCodeKey;
    }

    public String getCodeVector() {
        return mCodeVector;
    }

    public UserRaw getSenderUserRaw() {
        return mSenderUserRaw;
    }

    public List<StatusRaw> getStatusRaw() {
        return mStatusRaw;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setText(String mText) {
        this.mText = mText;
    }
}
