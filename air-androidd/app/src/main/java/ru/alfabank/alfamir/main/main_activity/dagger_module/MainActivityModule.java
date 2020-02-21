package ru.alfabank.alfamir.main.main_activity.dagger_module;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;
import ru.alfabank.alfamir.main.main_activity.MainPresenter;
import ru.alfabank.alfamir.main.main_activity.contract.MainActivityContract;

@Module
public abstract class MainActivityModule {
    @ActivityScoped
    @Binds
    abstract MainActivityContract.Presenter mainPresenter(MainPresenter presenter);
}
