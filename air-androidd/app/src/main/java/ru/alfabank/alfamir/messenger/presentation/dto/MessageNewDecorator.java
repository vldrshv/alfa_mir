package ru.alfabank.alfamir.messenger.presentation.dto;

import java.util.UUID;

import static ru.alfabank.alfamir.Constants.Messenger.VIEW_TYPE_MESSAGE_NEW_DECORATOR;

public class MessageNewDecorator extends DisplayableMessageItem {
    private static final int VIEW_TYPE = VIEW_TYPE_MESSAGE_NEW_DECORATOR;
    private long mViewId;

    private MessageNewDecorator(long viewId){
        mViewId = viewId;
    }

    @Override
    public int getType() {
        return VIEW_TYPE_MESSAGE_NEW_DECORATOR;
    }

    @Override
    public long getViewId() {
        return mViewId;
    }

    public static class Builder {
        private long mViewId;

        public MessageNewDecorator build(){
            mViewId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
            return new MessageNewDecorator(mViewId);
        }
    }
}
