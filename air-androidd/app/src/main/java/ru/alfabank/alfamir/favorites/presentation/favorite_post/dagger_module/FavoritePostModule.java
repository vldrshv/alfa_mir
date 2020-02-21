package ru.alfabank.alfamir.favorites.presentation.favorite_post.dagger_module;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.favorites.presentation.favorite_post.FavoritePostPresenter;
import ru.alfabank.alfamir.favorites.presentation.favorite_post.contract.FavoritePostContract;

@Module
public abstract class FavoritePostModule {

    @FragmentScoped
    @Binds
    abstract FavoritePostContract.Presenter peoplePresenter(FavoritePostPresenter presenter);

}