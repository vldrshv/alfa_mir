package ru.alfabank.alfamir.profile.presentation.edit_info;


import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;

import static ru.alfabank.alfamir.Constants.PROFILE_ID;


/**
 * Created by mshvdvsk on 29/03/2018.
 */

@Module
public abstract class EditInfoPresenterModule {
    @ActivityScoped
    @Binds
    abstract EditInfoContract.Presenter taskPresenter(EditInfoPresenter presenter);

    @Provides
    @ActivityScoped
    static String provideProfileId(EditInfoActivity activity) {
        return activity.getIntent().getStringExtra(PROFILE_ID);
    }
}