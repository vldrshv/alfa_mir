package ru.alfabank.alfamir.favorites.presentation.favorite_post.dummy_view;

import ru.alfabank.alfamir.favorites.presentation.favorite_post.contract.FavoritePostAdapterContract;
import ru.alfabank.alfamir.favorites.presentation.favorite_post.contract.FavoritePostContract;

public class FavoritePostViewDummy implements
        FavoritePostContract.View,
        FavoritePostAdapterContract.Adapter{



    @Override
    public void showFavoriteList() {

    }

    @Override
    public void openPost(String feedId, String postId, String feedUrl, String feedType) {

    }

    @Override
    public void openSearchUi() {

    }

    @Override
    public void showLoadingProgress() {

    }

    @Override
    public void hideLoadingProgress() {

    }


    @Override
    public void onItemInserted(int position) {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void delete(int position) {

    }
}
