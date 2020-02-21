package ru.alfabank.alfamir.profile.presentation.skills;

import ru.alfabank.alfamir.base_elements.BaseView;
import ru.alfabank.alfamir.base_elements.BasePresenter;

/**
 * Created by U_M0WY5 on 16.03.2018.
 */

public interface PersonSkillsContract {
    interface View extends BaseView<Presenter> {
        void showSkills(String skills);
        void showEmptySkills();
        void showInfoPrompt();

    }

    interface Presenter extends BasePresenter<View> {
        void openInfoPrompt();
    }
}
