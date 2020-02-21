package ru.alfabank.alfamir.people.presentation.dagger_module;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.people.presentation.PeopleActivity;
import ru.alfabank.alfamir.people.presentation.post_contract.PeopleContract;
import ru.alfabank.alfamir.people.presentation.PeoplePresenter;
import ru.alfabank.alfamir.people.presentation.PeopleListAdapter;
import ru.alfabank.alfamir.people.presentation.post_contract.PeopleListContract;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;
import ru.alfabank.alfamir.people.presentation.PeopleListPresenter;

import static ru.alfabank.alfamir.Constants.PROFILE_ID;

/**
 * Created by U_M0WY5 on 12.04.2018.
 */

@Module
public abstract class PeopleActivityModule {

    @Provides
    @ActivityScoped
    static String provideProfileId(PeopleActivity activity) {
        return activity.getIntent().getStringExtra(PROFILE_ID);
    }

    @ActivityScoped
    @Binds
    abstract PeopleContract.Presenter peoplePresenter(PeoplePresenter presenter);

    @ActivityScoped
    @Binds
    abstract PeopleListContract.ListPresenter listPresenter(PeopleListPresenter presenter);

    @ActivityScoped
    @Binds
    abstract PeopleListContract.ListAdapter peopleAdapter(PeopleListAdapter adapter);

}