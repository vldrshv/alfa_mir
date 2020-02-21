package ru.alfabank.alfamir.messenger.presentation.chat_fragment.dummy_view;


import android.graphics.Bitmap;
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatContract;

public class ChatViewDummy implements ChatContract.View {

    @Override
    public void showMessages() {

    }

    @Override
    public void scrollToPosition(int position) {

    }

    @Override
    public void clearTextInput() {

    }

    @Override
    public void openActivityProfileUi(String id) {

    }

    @Override
    public void setAuthorAvatar(Bitmap encodedImage, boolean isAnimated) {

    }

    @Override
    public void setAuthorPlaceholder(String initials) {

    }

    @Override
    public void setAuthorName(String name) {

    }

    @Override
    public void setAuthorInitials(String initials) {

    }

    @Override
    public void setLastSeen(String time) {

    }

    @Override
    public void setLoadingState(boolean isLoading) {

    }
}
