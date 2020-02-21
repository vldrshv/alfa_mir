package ru.alfabank.alfamir.messenger.data.dto;

import com.google.gson.annotations.SerializedName;

public class UserRaw implements PollDataRaw.PollDataValue {
    @SerializedName("Login")
    private
    String mId;
    @SerializedName("ConnDate")
    private
    String mLastOnline;
    @SerializedName("FullName")
    private
    String mName;
    @SerializedName("JobTitle")
    private
    String mPosition;
    @SerializedName("ProfilePicURL")
    private
    String mPicLink;
    @SerializedName("PublicKey")
    private
    String mPubKey;
    @SerializedName("Status")
    private
    int mOnlineStatus;

    public int getOnlineStatus() {
        return mOnlineStatus;
    }

    public String getLastOnline() {
        return mLastOnline;
    }

    public String getName() {
        return mName;
    }

    public String getPosition() {
        return mPosition;
    }

    public String getPicLink() {
        return mPicLink;
    }

    public String getId() {
        return mId;
    }
}
