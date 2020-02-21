package ru.alfabank.alfamir.profile.presentation.absence;

import com.google.common.base.Strings;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.alfamir.profile.domain.usecase.GetProfile;
import ru.alfabank.alfamir.profile.presentation.dto.Profile;

public class AbsencePresenter implements AbsenceContract.Presenter {

    private GetProfile mGetProfile;
    private AbsenceContract.View mView;
    private Profile mProfile;
    private String mUserLogin;
    private String mDelegateLogin;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Inject
    AbsencePresenter(String profileId,
                     GetProfile getProfile) {
        mUserLogin = profileId;
        mGetProfile = getProfile;
    }

    @Override
    public void takeView(AbsenceContract.View view) {
        mView = view;
        loadAbsenceInfo();
    }

    @Override
    public void dropView() {
        mCompositeDisposable.dispose();
        mView = null;
    }

    @Override
    public AbsenceContract.View getView() {
        if (mView == null) {
            return new AbsenceViewDummy();
        } else {
            return mView;
        }
    }

    private void showAbsenceInfo() {
        String name = mProfile.getName();
        String date = mProfile.getVacation();
        mDelegateLogin = mProfile.getVacationDelegate();

        if (Strings.isNullOrEmpty(name)) {

        } else {
            String promptText = name + " будет отсутствовать:";
            getView().showTitleText(promptText);
        }

        if (Strings.isNullOrEmpty(date)) {

        } else {
            boolean isCurrent = mProfile.isVacationCurrent();
            getView().showDate(date, isCurrent);
        }

        if (Strings.isNullOrEmpty(mDelegateLogin)) {
            getView().showEmptyDelegate();
        } else {
            loadDelegate(mDelegateLogin);
        }

    }

    private void loadAbsenceInfo() {
        mCompositeDisposable.add(
                mGetProfile.execute(mUserLogin)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responseValue -> {
                            mProfile = responseValue;
                            showAbsenceInfo();
                        }, Throwable::printStackTrace)
        );
    }

    private void loadDelegate(String delegateId) {
        mCompositeDisposable.add(
                mGetProfile.execute(delegateId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responseValue -> {
                            String name = responseValue.getName();
                            showDelegate(name);
                        }, Throwable::printStackTrace)
        );
    }

    private void showDelegate(String name) {
        getView().showDelegate(name);
    }

    @Override
    public void openDelegate() {
        getView().showActivityProfileUi(mDelegateLogin);
    }

    @Override
    public void showPrompt() {
        getView().showInfoPrompt();
    }
}
