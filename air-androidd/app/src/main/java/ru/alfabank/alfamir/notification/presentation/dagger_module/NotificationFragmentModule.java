package ru.alfabank.alfamir.notification.presentation.dagger_module;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.notification.presentation.NotificationPresenter;
import ru.alfabank.alfamir.notification.presentation.contract.NotificationContract;

@Module
public abstract class NotificationFragmentModule {

    @FragmentScoped
    @Binds
    abstract NotificationContract.Presenter menuPresenter(NotificationPresenter presenter);

}