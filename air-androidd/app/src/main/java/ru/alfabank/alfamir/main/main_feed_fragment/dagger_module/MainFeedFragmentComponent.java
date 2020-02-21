package ru.alfabank.alfamir.main.main_feed_fragment.dagger_module;

import dagger.Subcomponent;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;

@FragmentScoped
@Subcomponent(modules = MainFeedFragmentModule.class)
public interface  MainFeedFragmentComponent {

}