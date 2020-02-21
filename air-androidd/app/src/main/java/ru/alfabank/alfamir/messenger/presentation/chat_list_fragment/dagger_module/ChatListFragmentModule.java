package ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.dagger_module;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.ChatListPresenter;
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.contract.ChatListContract;

@Module
public abstract class ChatListFragmentModule {
    @FragmentScoped
    @Binds
    abstract ChatListContract.Presenter peoplePresenter(ChatListPresenter presenter);

}
