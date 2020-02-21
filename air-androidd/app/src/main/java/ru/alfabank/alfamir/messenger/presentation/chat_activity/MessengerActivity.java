package ru.alfabank.alfamir.messenger.presentation.chat_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.common.base.Strings;

import java.util.Set;

import javax.inject.Inject;

import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.di.qualifiers.messenger.ChatId;
import ru.alfabank.alfamir.di.qualifiers.ui.FragmentToBackStack;
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.ChatFragment;
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.ChatListFragment;
import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils;

import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_ADD;
import static ru.alfabank.alfamir.Constants.Messenger.USER_ID;

public class MessengerActivity extends BaseActivity implements MessengerActivityContract.View {
    private String mCorrespondentId;
    final FragmentManager mFragmentManager = getSupportFragmentManager();

    @Inject
    @FragmentToBackStack
    boolean mIsBackStacked;

    @Inject
    @ChatId
    String chatId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState,@Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Intent test = getIntent();
        Set<String> keySet = test.getExtras().keySet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_holder_activity);

        mCorrespondentId = getIntent().getStringExtra(USER_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int attachedFragmentsCount = mFragmentManager.getFragments().size();
        if (attachedFragmentsCount != 0 ) return;
        if(!Strings.isNullOrEmpty(mCorrespondentId) || !Strings.isNullOrEmpty(chatId)){
            showChatUi(mCorrespondentId);
        } else {
            showChatList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showChatUi(String userId) {
        getIntent().putExtra(USER_ID, userId);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                new ChatFragment(), R.id.contentFrame, FRAGMENT_TRANSITION_ADD, mIsBackStacked);
    }

    @Override
    public void showChatList() {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                new ChatListFragment(), R.id.contentFrame, FRAGMENT_TRANSITION_ADD, false);
    }

    @Override
    public void onBackPressed() {
        if(mFragmentManager.getBackStackEntryCount() == 0){
            finish();
        }else{
            super.onBackPressed();
        }
    }
}
