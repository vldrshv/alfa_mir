package ru.alfabank.alfamir.messenger.presentation.dto;

import static ru.alfabank.alfamir.Constants.Messenger.MESSAGE_STATUS_DELIVERED;
import static ru.alfabank.alfamir.Constants.Messenger.MESSAGE_STATUS_PENDING;
import static ru.alfabank.alfamir.Constants.Messenger.MESSAGE_STATUS_READ;
import static ru.alfabank.alfamir.Constants.Messenger.MESSAGE_STATUS_SENT;

public class Status {

    private String mChatId;
    private String mUserLogin;
    private String mSState;
    private long mMsgId;
    private long mDate;
    private int mState;

    private Status(String chatId,
                   String userLogin,
                   String sState,
                   long msgId,
                   long date,
                   int state){
        mChatId = chatId;
        mUserLogin = userLogin;
        mSState = sState;
        mMsgId = msgId;
        mState = state;
        mDate = date;
    }

    public String getChatId() {
        return mChatId;
    }
    public String getUserLogin() {
        return mUserLogin;
    }
    public String getSState() {
        return mSState;
    }
    public long getMsgId() {
        return mMsgId;
    }
    public int getState() {
        return mState;
    }
    public long getDate() {
        return mDate;
    }

    public static class Builder {
        private String mChatId;
        private String mUserLogin;
        private String mSState;
        private long mMsgId;
        private long mDate;
        private int mState;

        public Builder chatId(String chatId){
            mChatId = chatId;
            return this;
        }

        public Builder msgId(long msgId){
            mMsgId = msgId;
            return this;
        }

        public Builder state(String state){
            mSState = state;
            switch (state){
                case "pending":{
                    mState = MESSAGE_STATUS_PENDING;
                    break;
                }
                case "sent": {
                    mState = MESSAGE_STATUS_SENT;
                    break;
                }
                case "delivered": {
                    mState = MESSAGE_STATUS_DELIVERED;
                    break;
                }
                case "read": {
                    mState = MESSAGE_STATUS_READ;
                    break;
                }
            }
            return this;
        }

        public Builder userLogin(String userLogin){
            mUserLogin = userLogin;
            return this;
        }

        public Builder date(long date){
            mDate = date;
            return this;
        }

        public Status build(){
            return new Status(mChatId,
                    mUserLogin,
                    mSState,
                    mMsgId,
                    mDate,
                    mState);
        }
    }
}
