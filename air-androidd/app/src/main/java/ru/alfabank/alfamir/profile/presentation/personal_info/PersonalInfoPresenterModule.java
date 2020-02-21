package ru.alfabank.alfamir.profile.presentation.personal_info;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;

import static ru.alfabank.alfamir.Constants.PROFILE_ID;

/**
 * Created by mshvdvsk on 26/03/2018.
 */

@Module
public abstract class PersonalInfoPresenterModule {
    @ActivityScoped
    @Binds
    abstract PersonalInfoContract.Presenter taskPresenter(PersonalInfoPresenter presenter);

    @Provides
    @ActivityScoped
    static String provideProfileId(PersonalInfoActivity activity) {
        return activity.getIntent().getStringExtra(PROFILE_ID);
    }

}
