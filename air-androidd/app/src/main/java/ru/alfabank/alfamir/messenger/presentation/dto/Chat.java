package ru.alfabank.alfamir.messenger.presentation.dto;


import java.util.List;

public class Chat {
    private String mId;
    private String mType;
    private long mFirstUnreadMessageId;
    private long mFirstMessageId;
    private long mLastMessageId;
    private List<User> mUsers;
    private List<DisplayableMessageItem> mDisplayableMessageItemList;

    private Chat(String id,
                 String type,
                 long firstUnreadMessageId,
                 long firstMessageId,
                 long lastMessageId,
                 List<User> users,
                 List<DisplayableMessageItem> displayableMessageItems) {
        mId = id;
        mType = type;
        mFirstUnreadMessageId = firstUnreadMessageId;
        mFirstMessageId = firstMessageId;
        mLastMessageId = lastMessageId;
        mUsers = users;
        mDisplayableMessageItemList = displayableMessageItems;
    }

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

    public List<User> getUsers() {
        return mUsers;
    }

    public List<DisplayableMessageItem> getDisplayableMessageItemList() {
        return mDisplayableMessageItemList;
    }

    public static class Builder {
        private String mId;
        private String mType;
        private long mFirstUnreadMessageId;
        private long mFirstMessageId;
        private long mLastMessageId;
        private List<User> mUsers;
        private List<DisplayableMessageItem> mDisplayableMessageItemList;

        public Builder id(String id) {
            mId = id;
            return this;
        }

        public Builder type(String type) {
            mType = type;
            return this;
        }

        public Builder firstUnreadMessageId(long firstUnreadMessageId) {
            mFirstUnreadMessageId = firstUnreadMessageId;
            return this;
        }

        public Builder firstMessageId(long firstMessageId) {
            mFirstMessageId = firstMessageId;
            return this;
        }

        public Builder lastMessageId(long lastMessageId) {
            mLastMessageId = lastMessageId;
            return this;
        }

        public Builder users(List<User> users) {
            mUsers = users;
            return this;
        }

        public Builder displayableMessageItemList(List<DisplayableMessageItem> displayableMessageItems) {
            mDisplayableMessageItemList = displayableMessageItems;
            return this;
        }

        public Chat build() {
            return new Chat(mId, mType, mFirstUnreadMessageId, mFirstMessageId, mLastMessageId, mUsers, mDisplayableMessageItemList);
        }
    }
}
