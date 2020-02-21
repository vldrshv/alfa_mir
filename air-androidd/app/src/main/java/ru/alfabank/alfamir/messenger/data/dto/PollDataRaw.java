package ru.alfabank.alfamir.messenger.data.dto;

import com.google.gson.annotations.SerializedName;

public class PollDataRaw {
    @SerializedName("Login")
    String mLogin;
    @SerializedName("Type")
    String mType;
    @SerializedName("CreationDate")
    String mDate;
    @SerializedName("Guid")
    String mId;
    @SerializedName("Value")
    PollDataValue mValue;

    public PollDataRaw(String login,
                       String type,
                       PollDataValue value,
                       String date,
                       String id){
        mLogin = login;
        mType = type;
        mValue = value;
        mDate = date;
        mId = id;
    }

    public String getLogin() {
        return mLogin;
    }

    public String getType() {
        return mType;
    }

    public String getDate() {
        return mDate;
    }

    public String getId() {
        return mId;
    }

    public PollDataValue getValue() {
        return mValue;
    }

    public void setValue(PollDataValue mValue) {
        this.mValue = mValue;
    }

    public interface PollDataValue {}
}
