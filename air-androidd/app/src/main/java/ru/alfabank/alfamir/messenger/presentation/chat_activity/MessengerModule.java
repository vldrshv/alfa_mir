package ru.alfabank.alfamir.messenger.presentation.chat_activity;

import android.content.Intent;

import com.google.common.base.Strings;

import java.util.Set;

import dagger.Module;
import dagger.Provides;
import ru.alfabank.alfamir.di.qualifiers.messenger.ChatId;
import ru.alfabank.alfamir.di.qualifiers.ui.FragmentToBackStack;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;

import static ru.alfabank.alfamir.Constants.Messenger.CHAT_ID;
import static ru.alfabank.alfamir.Constants.UI.FRAGMENT_TO_BACKSTACK;

@Module
public abstract class MessengerModule {

    @Provides
    @ActivityScoped
    @FragmentToBackStack
    static boolean provideBackStack(MessengerActivity messengerActivity) {
        Intent test = messengerActivity.getIntent();
        Set<String> keySet = test.getExtras().keySet();
        return messengerActivity.getIntent()
                .getBooleanExtra(FRAGMENT_TO_BACKSTACK, false);
    }

    @Provides
    @ActivityScoped
    @ChatId
    static String provideChatId(MessengerActivity messengerActivity) {
        Intent test = messengerActivity.getIntent();
        Set<String> keySet = test.getExtras().keySet();
        String postId = messengerActivity.getIntent().getStringExtra(CHAT_ID);
        if(Strings.isNullOrEmpty(postId)) return "";
        return postId;
    }

}