package ru.alfabank.alfamir.main.main_activity.contract;


import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface MainActivityContract {

    interface View extends BaseView<Presenter> {
        void showMainFeed();
        void showHome();
        void showBottomNavBar();
        void hideBottomNavBar();
    }

    interface Presenter extends BasePresenter<View> {

    }

}
