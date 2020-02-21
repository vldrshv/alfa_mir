package ru.alfabank.alfamir.post.presentation.post.post_contract;

import ru.alfabank.alfamir.post.data.dto.PostRaw;
import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface PostContract {
    interface View extends BaseView<Presenter> {
        void showPost();
        void showKeyboard(String input);
        void setTextEditInput(String input);
        void hideKeyboard();
        void disableTextInput();
        void enableTextInput();
        void showCurtain();
        void hideCurtain();
        void scrollToPosition(int position);
        void showFloatingButton(int delay);
        void hideFloatingButton();
        void showLoadingProgress();
        void hideLoadingProgress();
        void openNewsActivityUi(String title, String type, String postUrl);
        void onEmailClicked(String uri);
        void openProfileActivityUi(String id);
        void openPostOptions(int position, int isDeletable, String favoriteStatus, String subscribeStatus);
        void onItemRemoved(int position);
        void onDataSetChanged();
        void onItemInserted(int position);
        // testing
        void showPostHybrid(PostRaw postRaw);
        void showSnackBar(String message);
        // should move to handlers
        void onExternalLinkClicked(String url);
        void onInternalLinkClicked(String url);
    }

    interface Presenter extends BasePresenter<View>, PostAdapterContract.Presenter {
        void onFBClicked();
        void onKeyboardHidden();
        void onPostRefresh();
        void onSendComment(String comment);
        void onViewPause();
    }
}
