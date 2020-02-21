package ru.alfabank.alfamir.profile.presentation.personal_info;

import com.google.common.base.Strings;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.data.source.rxbus.ProfileModule;
import ru.alfabank.alfamir.profile.data.source.repository.ProfileRepository;
import ru.alfabank.alfamir.profile.domain.usecase.GetProfile;
import ru.alfabank.alfamir.profile.domain.usecase.SaveAboutMe;
import ru.alfabank.alfamir.profile.presentation.dto.Profile;
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract;

final class PersonalInfoPresenter implements
        PersonalInfoContract.Presenter,
        LoggerContract.Client.PersonalInfo {

    private PersonalInfoContract.View mView;
    private GetProfile mGetProfile;
    private SaveAboutMe mSaveAboutMe;
    private ProfileRepository profileRepository;
    private LoggerContract.Provider mLogger;
    private String mUserLogin;
    private String mNewAboutMe;
    private Profile mProfile;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Inject
    PersonalInfoPresenter(String profileId,
                                 ProfileRepository profileRepository,
                                 LoggerContract.Provider logger,
                                 GetProfile getProfile,
                                 SaveAboutMe saveAboutMe) {
        mUserLogin = profileId;
        this.profileRepository = profileRepository;
        mLogger = logger;
        mGetProfile = getProfile;
        mSaveAboutMe = saveAboutMe;

    }

    private void loadInfo() {
        mCompositeDisposable.add(
                mGetProfile.execute(mUserLogin)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responseValue -> {
                            mProfile = responseValue;
                            String id = mProfile.getLogin();
                            String about = mProfile.getAboutMe();
                            String birthday = mProfile.getBirthday();

                            if (Constants.Initialization.INSTANCE.getUSER_LOGIN() == null) {
                                logError("login is null", this.toString() + " loadInfo");
                                return;
                            }

                            if (id.equals(Constants.Initialization.INSTANCE.getUSER_LOGIN())) {
                                if (Strings.isNullOrEmpty(about)) {
                                    getView().showAddInfoButton("+ Добавить информацию о себе");
                                    getView().hideAboutMe();
                                    getView().hideEditInfoIcon();
                                } else {
                                    getView().showAboutMe(about);
                                    getView().showEditInfoIcon();
                                }
                            } else {
                                if (Strings.isNullOrEmpty(about)) {
                                    getView().showEmptyAboutMe("Сотрудник не добавил информацию о себе");
                                } else {
                                    getView().showAboutMe(about);
                                }
                            }

                            if (Strings.isNullOrEmpty(birthday)) {
                                getView().hideBirthday();
                            } else {
                                getView().showBirthday(birthday);
                            }
                        }, Throwable::printStackTrace)
        );
    }


    @Override
    public void takeView(PersonalInfoContract.View view) {
        mView = view;
        loadInfo();
    }

    @Override
    public void dropView() {
        mCompositeDisposable.clear();
        mView = null;
    }

    @Override
    public PersonalInfoContract.View getView() {
        if (mView == null) {
            return new PersonalInfoViewDummy();
        } else {
            return mView;
        }
    }

    @Override
    public void uploadAboutMe(String text) {
        saveAboutMe(text);
    }

    @Override
    public void updateAboutMe(String text) {
        mNewAboutMe = text;
    }

    @Override
    public void addAboutMe() {
        getView().hideAddInfoButton();
        getView().showAboutMe("");
        getView().focusOnEditText();
    }

    private void saveAboutMe(String aboutMeText) {
        mCompositeDisposable.add(
                mSaveAboutMe.execute(new SaveAboutMe.RequestValues(aboutMeText))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responseValue -> {
                            profileRepository.refreshProfile();
                            loadInfo();
                            ProfileModule.getInstance().updateProfile();
                        }, throwable -> {

                        })
        );
    }

    @Override
    public void logError(String message, String stackTrace) {
        mLogger.error(message, stackTrace);
    }
}
