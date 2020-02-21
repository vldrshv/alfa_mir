package ru.alfabank.alfamir.profile.presentation.skills;

import com.google.common.base.Strings;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.alfamir.profile.data.source.repository.ProfileRepository;
import ru.alfabank.alfamir.profile.domain.usecase.GetProfile;
import ru.alfabank.alfamir.profile.presentation.dto.Profile;

public class PersonSkillsPresenter implements PersonSkillsContract.Presenter {

    private GetProfile mGetProfile;
    private PersonSkillsContract.View mView;
    private ProfileRepository profileRepository;
    private String mUserLogin;
    private Profile mProfile;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Inject
    PersonSkillsPresenter(String profileId,
                          ProfileRepository profileRepository,
                          GetProfile getProfile) {
        mUserLogin = profileId;
        this.profileRepository = profileRepository;
        mGetProfile = getProfile;
    }

    private void showSkills() {
        String skills = mProfile.getExpertise();
        if (Strings.isNullOrEmpty(skills)) {
            getView().showEmptySkills();
        } else {
            getView().showSkills(skills);
        }
    }

    private void loadSkills() {
        mCompositeDisposable.add(
                mGetProfile.execute(mUserLogin)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responseValue -> {
                            mProfile = responseValue;
                            showSkills();
                        }, Throwable::printStackTrace)
        );
    }

    @Override
    public void openInfoPrompt() {
        getView().showInfoPrompt();
    }

    @Override
    public void takeView(PersonSkillsContract.View view) {
        mView = view;
        loadSkills();
    }

    @Override
    public void dropView() {
        mCompositeDisposable.clear();
        mView = null;
    }

    @Override
    public PersonSkillsContract.View getView() {
        if (mView == null) {
            return new PersonSkillsViewDummy();
        } else {
            return mView;
        }
    }
}
