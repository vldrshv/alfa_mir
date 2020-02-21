package ru.alfabank.alfamir.favorites.presentation.favorite_post.contract;

import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface FavoritePostContract {

    interface View extends BaseView<Presenter> {
        void showFavoriteList();
        void openPost(String feedId, String postId, String feedUrl, String feedType);
        void openSearchUi();
        void showLoadingProgress();
        void hideLoadingProgress();
    }

    interface Presenter extends BasePresenter<View>, FavoritePostAdapterContract.Presenter {
        void onSearchClicked();
        void delete(int position);
    }

}
