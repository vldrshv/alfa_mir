package ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract;

import android.content.Intent;
import android.graphics.Bitmap;
import org.jetbrains.annotations.Nullable;
import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface ChatContract {

    interface View extends BaseView<Presenter> {
        void setAuthorAvatar(Bitmap bitmap, boolean isAnimated);

        void setAuthorPlaceholder(String initials);

        void setAuthorName(String name);

        void setAuthorInitials(String initials);

        void setLastSeen(String time);

        void setLoadingState(boolean isLoading);

        void showMessages();

        void scrollToPosition(int position);

        void clearTextInput();

        void openActivityProfileUi(String id);
    }

    interface Presenter extends BasePresenter<View>, ChatAdapterContract.Presenter {
        void onCorrespondentProfileClicked();

        void onTextInputClicked();

        void onSendMessageClicked(String message);

        void test();

        void onActivityResult(@Nullable Intent intent, int requestCode);

        android.view.View.OnClickListener getPhotoListener();
        android.view.View.OnClickListener getItemListener();
    }
}