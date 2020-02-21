package ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.dummy_view;

import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.contract.ChatListAdapterContract;
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.contract.ChatListContract;

public class ChatListViewDummy implements ChatListContract.View, ChatListAdapterContract.Adapter {
    @Override
    public void showChatList() {

    }

    @Override
    public void showChatUi(String userId, String chatId, String type) {

    }

    public void showChatUi(String chatId) {

    }

    @Override
    public void setRefreshState(boolean isRefreshing) {

    }

    @Override
    public void setEnabledState(boolean isActive) {

    }

    @Override
    public void showEmptyMessage() {

    }

    @Override
    public void hideEmptyMessage() {

    }

    @Override
    public void onDataSetChanged() {

    }
}
