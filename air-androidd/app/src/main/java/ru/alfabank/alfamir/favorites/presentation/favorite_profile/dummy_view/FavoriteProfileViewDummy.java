package ru.alfabank.alfamir.favorites.presentation.favorite_profile.dummy_view;

import ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract.FavoriteProfileAdapterContract;
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract.FavoriteProfileContract;

public class FavoriteProfileViewDummy implements FavoriteProfileContract.View, FavoriteProfileAdapterContract.Adapter {
    @Override
    public void onItemInserted(int position) {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void delete(int position) {

    }

    @Override
    public void showFavoriteList() {

    }

    @Override
    public void openProfileActivityUi(String userLogin) {

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
}
