package ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.contract;

import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface ChatListContract {

    interface View extends BaseView<Presenter> {
        void showChatList();
        void showChatUi(String userId, String chatId, String type);
        void setRefreshState(boolean isRefreshing);
        void setEnabledState(boolean isActive);
        void showEmptyMessage();
        void hideEmptyMessage();
    }

    interface Presenter extends BasePresenter<View>,
            ChatListAdapterContract.Presenter {
        void onRefresh();
    }

}
