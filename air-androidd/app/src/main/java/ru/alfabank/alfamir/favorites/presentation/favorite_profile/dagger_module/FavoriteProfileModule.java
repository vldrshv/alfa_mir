package ru.alfabank.alfamir.favorites.presentation.favorite_profile.dagger_module;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.FavoriteProfilePresenter;
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract.FavoriteProfileContract;

@Module
public abstract class FavoriteProfileModule {

    @FragmentScoped
    @Binds
    abstract FavoriteProfileContract.Presenter peoplePresenter(FavoriteProfilePresenter presenter);

}