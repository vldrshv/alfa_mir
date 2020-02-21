package ru.alfabank.alfamir.post.presentation.post.dummy_view;

import ru.alfabank.alfamir.post.data.dto.PostRaw;
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostContract;

public class PostViewDummy implements PostContract.View {
    @Override
    public void showPost() {

    }

    @Override
    public void showKeyboard(String input) {

    }

    @Override
    public void setTextEditInput(String input) {

    }

    @Override
    public void hideKeyboard() {

    }

    @Override
    public void disableTextInput() {

    }

    @Override
    public void enableTextInput() {

    }

    @Override
    public void showCurtain() {

    }

    @Override
    public void hideCurtain() {

    }

    @Override
    public void scrollToPosition(int position) {

    }

    @Override
    public void showFloatingButton(int delay) {

    }

    @Override
    public void hideFloatingButton() {

    }

    @Override
    public void showLoadingProgress() {

    }

    @Override
    public void hideLoadingProgress() {

    }

    @Override
    public void openNewsActivityUi(String title, String type, String postUrl) {

    }

    @Override
    public void onEmailClicked(String uri) {

    }

    @Override
    public void openProfileActivityUi(String id) {

    }

    @Override
    public void openPostOptions(int position, int isDeletable, String favoriteStatus, String subscribeStatus) {

    }

    @Override
    public void onItemRemoved(int position) {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onItemInserted(int position) {

    }

    @Override
    public void showPostHybrid(PostRaw postRaw) {

    }

    @Override
    public void showSnackBar(String message) {

    }

    @Override
    public void onExternalLinkClicked(String url) {

    }

    @Override
    public void onInternalLinkClicked(String url) {

    }
}
