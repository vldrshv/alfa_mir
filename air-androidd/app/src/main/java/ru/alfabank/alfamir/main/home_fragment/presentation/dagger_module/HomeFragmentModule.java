package ru.alfabank.alfamir.main.home_fragment.presentation.dagger_module;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.main.home_fragment.presentation.HomePresenter;
import ru.alfabank.alfamir.main.home_fragment.presentation.contract.HomeFragmentContract;
import ru.alfabank.alfamir.main.home_fragment.presentation.contract.TopNewsAdapterContract;

@Module
public abstract class HomeFragmentModule {
    @FragmentScoped
    @Binds
    abstract HomeFragmentContract.Presenter peoplePresenter(HomePresenter presenter);

    @FragmentScoped
    @Binds
    abstract TopNewsAdapterContract.Presenter topNewsPresenter(HomeFragmentContract.Presenter presenter);

}