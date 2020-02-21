package ru.alfabank.alfamir.settings.presentation.settings;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;

@Module
public abstract class SettingsActivityModule {

    @ActivityScoped
    @Binds
    abstract SettingsContract.Presenter peoplePresenter(SettingsPresenter presenter);
}