package ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.contract;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.base_elements.BaseAdapter;
import ru.alfabank.alfamir.base_elements.BaseListPresenter;
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.view_holder.ChatVH;

public interface ChatListAdapterContract {

    interface Presenter extends BaseListPresenter<Adapter>, ChatVH.ViewHolderClickListener {
        void bindListRowChat(int position, ChatRowView rowView);
        long getItemId(int position);
    }

    interface Adapter extends BaseAdapter {
        void onDataSetChanged();
    }

    interface ChatRowView {
        void setChatPic(Bitmap encodedImage, boolean isAnimated);
        void setChatTitle(String title);
        void setOnlineStatus(boolean isOnline);
        void setLastMessageTime(String time);
        void setLastMessage(String message);
        void setNewMessageCount(String count);
        void showNewMessageBadge();
        void hideNewMessageBadge();
    }

}
