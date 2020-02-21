package ru.alfabank.alfamir.settings.presentation.settings;

import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.data.dto.notifications_settings.NotificationsSettings;
import ru.alfabank.alfamir.notification.data.source.repository.NotificationsDataSource;
import ru.alfabank.alfamir.notification.data.source.repository.NotificationsRepository;

import javax.inject.Inject;

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.View mView;
    private NotificationsRepository mNotificationsRepository;
    private boolean mFirstLoaded;
    private boolean isMainChecked;

    String LOG = this.toString();

    @Inject
    SettingsPresenter(NotificationsRepository notificationsRepository){
        mNotificationsRepository = notificationsRepository;
    }

    private void loadSettings(){
        loadFromRemoteDataSource();
    }

    private void loadFromRemoteDataSource(){
        mNotificationsRepository.getUserSettings(new NotificationsDataSource.LoadSettingsCallback() {
            @Override
            public void onSettingsLoaded(NotificationsSettings settings) {
                mFirstLoaded = true;
                showSettings(settings);
            }

            @Override
            public void onServerNotAvailable() {

            }
        });
    }

    private void showSettings(NotificationsSettings settings){
        int birthdaysSwitchState = settings.getIsBirthdaysActivated();
        int vacationsSwitchState = settings.getIsVacationsActivated();
        int postsSwitchState = settings.getIsPostsActivated();
        int commentsSwitchState = settings.getIsCommentsActivated();

        boolean isBirthdayChecked;
        if(birthdaysSwitchState == 0){
            isBirthdayChecked = false;
        } else {
            isMainChecked = true;
            isBirthdayChecked = true;
        }
        getView().setSwitchStateBirthday(isBirthdayChecked);

        boolean isVacationChecked;
        if(vacationsSwitchState == 0){
            isVacationChecked = false;
        } else {
            isMainChecked = true;
            isVacationChecked = true;
        }
        getView().setSwitchStateVacation(isVacationChecked);

        boolean isPostsChecked;
        if(postsSwitchState == 0){
            isPostsChecked = false;
        } else {
            isMainChecked = true;
            isPostsChecked = true;
        }
        getView().setSwitchStatePosts(isPostsChecked);

        boolean isCommentsChecked;
        if(commentsSwitchState == 0){
            isCommentsChecked = false;
        } else {
            isMainChecked = true;
            isCommentsChecked = true;
        }
        getView().setSwitchStateComments(isCommentsChecked);

        getView().setSwitchStateMain(isMainChecked);
    }

    @Override
    public void takeView(SettingsContract.View view) {
        mView = view;
        if(mFirstLoaded){
            return;
        }
        loadSettings();
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public SettingsContract.View getView() {
        if(mView==null){
            return new SettingViewDummy();
        }
        return mView;
    }

    @Override
    public void clearSettings() {

    }

    @Override
    public void onMainSwitched(boolean isChecked) {
        saveSettings(Constants.NotificationSettings.SWITCH_MAIN, isChecked);
//        mSettingsView.setSwitchStateInactive(isChecked); // may be should change to this logic someday
        getView().setSwitchStateAll(isChecked);
    }

    @Override
    public void onBirthdaySwitched(boolean isChecked) {
        saveSettings(Constants.NotificationSettings.SWITCH_BIRTHDAYS, isChecked);
        setMainSwitchState();
        getView().setSwitchStateBirthday(isChecked);
    }

    @Override
    public void onVacationSwitched(boolean isChecked) {
        saveSettings(Constants.NotificationSettings.SWITCH_VACATIONS, isChecked);
        setMainSwitchState();
        getView().setSwitchStateVacation(isChecked);
    }

    @Override
    public void onCommentsSwitched(boolean isChecked) {
        saveSettings(Constants.NotificationSettings.SWITCH_COMMENTS, isChecked);
        setMainSwitchState();
        getView().setSwitchStateComments(isChecked);
    }

    @Override
    public void onPostsSwitched(boolean isChecked) {
        saveSettings(Constants.NotificationSettings.SWITCH_POSTS, isChecked);
        setMainSwitchState();
        getView().setSwitchStatePosts(isChecked);
    }

    @Override
    public void onInfoFavoriteClicked() {
        getView().showInfoFavoritesDialog();
    }

    @Override
    public void onInfoSubscriptionsClicked() {
        getView().showInfoSubscriptionsDialog();
    }

    private void saveSettings(int switchType, boolean isChecked){
        mNotificationsRepository.getUserSettings(new NotificationsDataSource.LoadSettingsCallback() {
            @Override
            public void onSettingsLoaded(NotificationsSettings settings) {
                int state = isChecked ? 1 : 0;

                switch (switchType) {
                    case Constants.NotificationSettings.SWITCH_MAIN:{
                        settings.setIsBirthdaysActivated(state);
                        settings.setIsVacationsActivated(state);
                        settings.setIsPostsActivated(state);
                        settings.setIsCommentsActivated(state);
                        break;
                    }
                    case Constants.NotificationSettings.SWITCH_BIRTHDAYS:{
                        settings.setIsBirthdaysActivated(state);
                        break;
                    }
                    case Constants.NotificationSettings.SWITCH_VACATIONS:{
                        settings.setIsVacationsActivated(state);
                        break;
                    }
                    case Constants.NotificationSettings.SWITCH_POSTS:{
                        settings.setIsPostsActivated(state);
                        break;
                    }
                    case Constants.NotificationSettings.SWITCH_COMMENTS:{
                        settings.setIsCommentsActivated(state);
                        break;
                    }

                }
                mNotificationsRepository.uploadUserSettings(settings, new NotificationsDataSource.UploadSettingsCallback() {
                    @Override
                    public void onSettingsUploaded() {

                    }

                    @Override
                    public void onServerNotAvailable() {

                    }
                });
            }

            @Override
            public void onServerNotAvailable() {

            }
        });
    }

    private boolean setMainSwitchState(){
        mNotificationsRepository.getUserSettings(new NotificationsDataSource.LoadSettingsCallback() {
            @Override
            public void onSettingsLoaded(NotificationsSettings settings) {
                if(settings.isDisabled()){
                    getView().setSwitchStateMain(false);
                } else {
                    getView().setSwitchStateMain(true);
                }
            }

            @Override
            public void onServerNotAvailable() {

            }
        });

        return false;
    }


}