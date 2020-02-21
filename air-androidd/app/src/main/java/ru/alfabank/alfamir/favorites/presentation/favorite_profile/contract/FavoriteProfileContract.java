package ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract;

import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface FavoriteProfileContract {

    interface View extends BaseView<Presenter> {
        void showFavoriteList();

        void openProfileActivityUi(String userLogin);

        void openSearchUi();

        void showLoadingProgress();

        void hideLoadingProgress();
    }

    interface Presenter extends BasePresenter<View>, FavoriteProfileAdapterContract.Presenter {
        void onSearchClicked();
        void delete(int position);
    }
}
