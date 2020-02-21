package ru.alfabank.alfamir.profile.presentation.skills;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;

import static ru.alfabank.alfamir.Constants.PROFILE_ID;

/**
 * Created by mshvdvsk on 29/03/2018.
 */


@Module
public abstract class PersonSkillsPresenterModule {
    @ActivityScoped
    @Binds
    abstract PersonSkillsContract.Presenter personSkillsPresenter(PersonSkillsPresenter presenter);

    @Provides
    @ActivityScoped
    static String provideProfileId(PersonSkillsActivity activity) {
        return activity.getIntent().getStringExtra(PROFILE_ID);
    }
}