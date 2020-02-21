package ru.alfabank.alfamir.feed.presentation.feed;

import android.os.Bundle;

import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.feed.presentation.feed_with_header.FeedWithHeaderFragment;
import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils;

import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_ADD;

public class FeedActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_holder_activity);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                new FeedWithHeaderFragment(), R.id.contentFrame, FRAGMENT_TRANSITION_ADD, false);
    }
}
