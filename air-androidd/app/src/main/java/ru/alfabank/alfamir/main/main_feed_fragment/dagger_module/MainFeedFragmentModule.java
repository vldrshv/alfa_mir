package ru.alfabank.alfamir.main.main_feed_fragment.dagger_module;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.main.main_feed_fragment.MainFeedListPresenter;
import ru.alfabank.alfamir.main.main_feed_fragment.contract.MainFeedContract;
import ru.alfabank.alfamir.main.main_feed_fragment.MainFeedPresenter;
import ru.alfabank.alfamir.main.main_feed_fragment.MainFeedListAdapter;
import ru.alfabank.alfamir.main.main_feed_fragment.contract.MainFeedListContract;

@Module
public abstract class MainFeedFragmentModule {

    @FragmentScoped
    @Binds
    abstract MainFeedContract.Presenter newsPresenter(MainFeedPresenter presenter);

    @FragmentScoped
    @Binds
    abstract MainFeedListContract.Presenter listPresenter(MainFeedListPresenter presenter);

    @FragmentScoped
    @Binds
    abstract MainFeedListContract.Adapter peopleAdapter(MainFeedListAdapter adapter);

}