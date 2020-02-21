package ru.alfabank.alfamir.profile.presentation.absence;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;

import static ru.alfabank.alfamir.Constants.PROFILE_ID;

/**
 * Created by mshvdvsk on 29/03/2018.
 */

@Module
public abstract class AbsencePresenterModule {
    @ActivityScoped
    @Binds
    abstract AbsenceContract.Presenter absencePresenter(AbsencePresenter presenter);

    @Provides
    @ActivityScoped
    static String provideProfileId(AbsenceActivity activity) {
        return activity.getIntent().getStringExtra(PROFILE_ID);
    }
}