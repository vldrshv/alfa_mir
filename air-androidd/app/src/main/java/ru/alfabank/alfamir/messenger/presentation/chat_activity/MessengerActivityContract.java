package ru.alfabank.alfamir.messenger.presentation.chat_activity;

import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface MessengerActivityContract {

    interface View extends BaseView<Presenter> {
        void showChatUi(String userId);
        void showChatList();
    }

    interface Presenter extends BasePresenter<View> {

    }

}
