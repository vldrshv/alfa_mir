package ru.alfabank.alfamir.initialization.presentation.initialization.dagger_module;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;
import ru.alfabank.alfamir.initialization.presentation.initialization.InitializationPresenter;
import ru.alfabank.alfamir.initialization.presentation.initialization.contract.InitializationContract;

@Module
public abstract class InitializationActivityModule {

    @ActivityScoped
    @Binds
    abstract InitializationContract.Presenter peoplePresenter(InitializationPresenter presenter);
}