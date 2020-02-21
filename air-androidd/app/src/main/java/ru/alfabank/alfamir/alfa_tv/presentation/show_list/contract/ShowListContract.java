package ru.alfabank.alfamir.alfa_tv.presentation.show_list.contract;

import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface ShowListContract {

    interface View extends BaseView<Presenter> {
        void showShows();
        void openShowUi(int showPosition, int id);
        void enableRefresh();
        void showProgressBar();
        void hideProgressBar();
        void showSnackBar(String text);

        void disableRefresh();
    }

    interface Presenter extends BasePresenter<View>, ShowListAdapterContract.Presenter {
        void onListRefresh();
    }

}
