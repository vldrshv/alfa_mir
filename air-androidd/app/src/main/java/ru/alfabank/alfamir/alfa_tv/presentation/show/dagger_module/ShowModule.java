package ru.alfabank.alfamir.alfa_tv.presentation.show.dagger_module;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.alfa_tv.presentation.show.ShowActivity;
import ru.alfabank.alfamir.alfa_tv.presentation.show.ShowPresenter;
import ru.alfabank.alfamir.alfa_tv.presentation.show.contract.ShowContract;
import ru.alfabank.alfamir.di.qualifiers.ShowId;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;

import static ru.alfabank.alfamir.Constants.Show.SHOW_ID;

@Module
public abstract class ShowModule {

    @ActivityScoped
    @Binds
    abstract ShowContract.Presenter peoplePresenter(ShowPresenter presenter);

    @Provides
    @ShowId
    @ActivityScoped
    static int provideUserId(ShowActivity activityHalfMonster) {
        int showId = activityHalfMonster.getIntent().getIntExtra(SHOW_ID, 0);
        return showId;
    }

}