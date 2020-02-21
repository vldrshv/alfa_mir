package ru.alfabank.alfamir.profile.presentation.profile;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;

import static ru.alfabank.alfamir.Constants.PROFILE_ID;


/**
 * Created by mshvdvsk on 29/03/2018.
 */
@Module
public abstract class ProfilePresenterModule {
    @ActivityScoped
    @Binds
    abstract ProfileContract.Presenter profilePresenter(ProfilePresenter presenter);

    @Provides
    @ActivityScoped
    static String provideProfileId(ProfileActivity activity) {
        return activity.getIntent().getStringExtra(PROFILE_ID);
    }
}
