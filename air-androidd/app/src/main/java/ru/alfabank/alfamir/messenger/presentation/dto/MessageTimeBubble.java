package ru.alfabank.alfamir.messenger.presentation.dto;

import java.util.UUID;

import static ru.alfabank.alfamir.Constants.Messenger.VIEW_TYPE_TIME_BUBBLE;

public class MessageTimeBubble extends DisplayableMessageItem {
    private static final int VIEW_TYPE = VIEW_TYPE_TIME_BUBBLE;
    private long mViewId;
    private String mDate;

    MessageTimeBubble(String date, long viewId){
        mViewId = viewId;
        mDate = date;
    }

    @Override
    public int getType() {
        return VIEW_TYPE;
    }

    @Override
    public long getViewId() {
        return mViewId;
    }

    public String getDate() {
        return mDate;
    }

    public static class Builder {
        private String mDate;
        private long mViewId;

        public Builder date(String date){
            mDate = date;
            return this;
        }

        public MessageTimeBubble build(){
            mViewId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
            return new MessageTimeBubble(mDate, mViewId);
        }
    }
}
