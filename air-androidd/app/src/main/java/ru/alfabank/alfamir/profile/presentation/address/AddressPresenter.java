package ru.alfabank.alfamir.profile.presentation.address;

import com.google.common.base.Strings;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.alfamir.profile.domain.usecase.GetProfile;
import ru.alfabank.alfamir.profile.presentation.dto.Profile;

public class AddressPresenter implements AddressContract.Presenter {

    private GetProfile mGetProfile;
    private AddressContract.View mView;
    private Profile mProfile;
    private String mUserLogin;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Inject
    AddressPresenter(String profileId,
                     GetProfile getProfile) {
        mUserLogin = profileId;
        mGetProfile = getProfile;
    }

    private void loadAddress() {
        mCompositeDisposable.add(
                mGetProfile.execute(mUserLogin)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responseValue -> {
                            mProfile = responseValue;
                            showAddress();
                        }, Throwable::printStackTrace)
        );
    }

    private void showAddress() {
        String workSpaceAddress = mProfile.getWorkSpaceAddress();
        String fullAddress = mProfile.getFullAddress();
        String shortAddress = mProfile.getShortAddress();

        if (Strings.isNullOrEmpty(workSpaceAddress)) {
            workSpaceAddress = "";
        } else {
            workSpaceAddress = workSpaceAddress.replace("<br>", " > ");
        }

        if (Strings.isNullOrEmpty(workSpaceAddress)) {
            getView().hidePhysicalAddress();
        } else {
            getView().showPhysicalAddress(workSpaceAddress);
        }

        if (Strings.isNullOrEmpty(fullAddress)) {
            getView().hideWorkSpaceFull();
        } else {
            getView().showWorkSpaceFull(fullAddress);
        }

        if (Strings.isNullOrEmpty(shortAddress)) {
            getView().hideWorkSpaceShort();
        } else {
            getView().showWorkSpaceShort(shortAddress);
        }
    }

    @Override
    public void takeView(AddressContract.View view) {
        mView = view;
        loadAddress();
    }

    @Override
    public void dropView() {
        mCompositeDisposable.dispose();
        mView = null;
    }

    @Override
    public AddressContract.View getView() {
        if (mView == null) {
            return new AdressViewDummy();
        } else {
            return mView;
        }
    }
}
