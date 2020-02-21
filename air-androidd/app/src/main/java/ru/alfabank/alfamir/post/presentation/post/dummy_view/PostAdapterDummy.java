package ru.alfabank.alfamir.post.presentation.post.dummy_view;

import ru.alfabank.alfamir.post.presentation.post.post_contract.PostAdapterContract;

public class PostAdapterDummy implements PostAdapterContract.Adapter {

    @Override
    public void onPostImageInject(int position, String javaScript) {

    }

    @Override
    public void onPostVideoUrlInject(int position, String javaScript) {

    }

    @Override
    public void onPostVideoPosterInject(int position, String javaScript) {

    }

    @Override
    public void openPostOptions(int isDeletable, String favoriteStatus, String subscribeStatus) {

    }

    @Override
    public void setCommentLikeState(int position, String likesCount, int currentUserLike) {

    }

    @Override
    public void setPostLikeState(int position, String likesCount, int currentUserLike) {

    }

    @Override
    public void setWebViewOnPause(int position) {

    }

    @Override
    public void setWebViewOnResume(int position) {

    }
}
