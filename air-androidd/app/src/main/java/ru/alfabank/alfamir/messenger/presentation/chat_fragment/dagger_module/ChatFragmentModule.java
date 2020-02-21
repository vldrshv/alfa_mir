package ru.alfabank.alfamir.messenger.presentation.chat_fragment.dagger_module;

import com.google.common.base.Strings;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.di.qualifiers.ID;
import ru.alfabank.alfamir.di.qualifiers.messenger.ChatId;
import ru.alfabank.alfamir.di.qualifiers.messenger.Type;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.ChatFragment;
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.ChatPresenter;
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatContract;

import static ru.alfabank.alfamir.Constants.Messenger.*;

@Module
public abstract class ChatFragmentModule {

    @FragmentScoped
    @Binds
    abstract ChatContract.Presenter peoplePresenter(ChatPresenter presenter);

    @Provides
    @FragmentScoped
    @ID
    static String provideUserId(ChatFragment fragment) {
        String userId = fragment.getActivity().getIntent().getStringExtra(USER_ID);
        if (Strings.isNullOrEmpty(userId)) return "";
        return userId;
    }

    @Provides
    @FragmentScoped
    @ChatId
    static String provideChatId(ChatFragment fragment) {
        String postId = fragment.getActivity().getIntent().getStringExtra(CHAT_ID);
        if (Strings.isNullOrEmpty(postId)) return "";
        return postId;
    }

    @Provides
    @FragmentScoped
    @Type
    static String provideChatType(ChatFragment fragment) {
        String postId = fragment.getActivity().getIntent().getStringExtra(CHAT_TYPE);
        if (Strings.isNullOrEmpty(postId)) return "";
        return postId;
    }

}
