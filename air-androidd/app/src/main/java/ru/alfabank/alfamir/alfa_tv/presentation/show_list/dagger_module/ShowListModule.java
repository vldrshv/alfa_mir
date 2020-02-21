package ru.alfabank.alfamir.alfa_tv.presentation.show_list.dagger_module;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.ShowListPresenter;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.contract.ShowListContract;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;

@Module
public abstract class ShowListModule {
    @FragmentScoped
    @Binds
    abstract ShowListContract.Presenter showListPresenter (ShowListPresenter presenter);

}
