package ru.alfabank.alfamir.search.presentation.dagger_module;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.search.presentation.SearchPresenter;
import ru.alfabank.alfamir.search.presentation.contract.SearchFragmentContract;

@Module
public abstract class SearchFragmentModule {

    @FragmentScoped
    @Binds
    abstract SearchFragmentContract.Presenter menuPresenter(SearchPresenter presenter);

}