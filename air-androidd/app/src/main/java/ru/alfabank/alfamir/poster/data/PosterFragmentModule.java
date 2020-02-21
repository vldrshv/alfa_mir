package ru.alfabank.alfamir.poster.data;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.poster.presentation.PosterPresenter;

@Module
abstract public class PosterFragmentModule {

    @Binds
    @Singleton
    abstract PosterFragmentContract.Presenter presenter(PosterPresenter presenter);
}
