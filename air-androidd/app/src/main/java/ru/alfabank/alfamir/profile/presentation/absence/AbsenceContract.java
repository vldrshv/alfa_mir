package ru.alfabank.alfamir.profile.presentation.absence;

import ru.alfabank.alfamir.base_elements.BaseView;
import ru.alfabank.alfamir.base_elements.BasePresenter;

/**
 * Created by U_M0WY5 on 16.03.2018.
 */

public interface AbsenceContract {

    interface View extends BaseView<Presenter> {
        void showTitleText(String text);
        void showDate(String date, boolean isCurrent);
        void showDelegate(String name);
        void showEmptyDelegate();
        void showInfoPrompt();
        void showActivityProfileUi(String id);
    }

    interface Presenter extends BasePresenter<View> {
        void openDelegate();
        void showPrompt();
    }

}
