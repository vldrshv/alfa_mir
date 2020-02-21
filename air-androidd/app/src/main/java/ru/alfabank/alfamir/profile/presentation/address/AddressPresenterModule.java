package ru.alfabank.alfamir.profile.presentation.address;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;

import static ru.alfabank.alfamir.Constants.PROFILE_ID;

/**
 * Created by mshvdvsk on 29/03/2018.
 */

@Module
public abstract class AddressPresenterModule {
    @ActivityScoped
    @Binds
    abstract AddressContract.Presenter adressPresenter(AddressPresenter presenter);

    @Provides
    @ActivityScoped
    static String provideProfileId(AddressActivity activity) {
        return activity.getIntent().getStringExtra(PROFILE_ID);
    }
}