package ru.alfabank.alfamir.service.dto;

import com.google.gson.annotations.SerializedName;

public class ChatMessage {
    @SerializedName("type")
    private String mType;
    @SerializedName("chatid")
    private String mChatId;

    public String getType() {
        return mType;
    }

    public String getChatId() {
        return mChatId;
    }
}
