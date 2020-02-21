package ru.alfabank.alfamir.main.menu_fragment.presentation.dagger_module;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.main.menu_fragment.presentation.MenuPresenter;
import ru.alfabank.alfamir.main.menu_fragment.presentation.contract.MenuFragmentContract;

@Module
public abstract class MenuFragmentModule {
    @FragmentScoped
    @Binds
    abstract MenuFragmentContract.Presenter menuPresenter(MenuPresenter presenter);

}