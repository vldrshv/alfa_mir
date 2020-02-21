package ru.alfabank.alfamir.search.presentation.contract;

import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface SearchFragmentContract {

    interface View extends BaseView<Presenter> {
        void showResults();
        void showKeyboard();
        void hideKeyboard();
        void openProfileUi(String id);
        void showLoading();
        void hideLoading();
        String getUserInput();
    }

    interface Presenter extends BasePresenter<View>, SearchAdapterContract.Presenter {
        void onSearchInput(String input);
        void onSearchMore(String input, boolean morePeople, boolean morePages);
    }
}
