package ru.alfabank.alfamir.settings.presentation.settings;

import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public class SettingsContract {
    interface View extends BaseView<Presenter> {
        void setSwitchStateInactive(boolean isActive);
        void setSwitchStateAll(boolean isChecked);
        void setSwitchStateMain(boolean isChecked);
        void setSwitchStateBirthday(boolean isChecked);
        void setSwitchStateVacation(boolean isChecked);
        void setSwitchStateComments(boolean isChecked);
        void setSwitchStatePosts(boolean isChecked);
        void showInfoFavoritesDialog();
        void showInfoSubscriptionsDialog();
    }

    interface Presenter extends BasePresenter<View> {
      void clearSettings();
      void onMainSwitched(boolean isChecked);
      void onBirthdaySwitched(boolean isChecked);
      void onVacationSwitched(boolean isChecked);
      void onCommentsSwitched(boolean isChecked);
      void onPostsSwitched(boolean isChecked);
      void onInfoFavoriteClicked();
      void onInfoSubscriptionsClicked();
    }
}
