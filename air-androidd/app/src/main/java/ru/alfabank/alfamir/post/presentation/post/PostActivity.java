package ru.alfabank.alfamir.post.presentation.post;

import android.os.Bundle;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils;

import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_ADD;

/**
 * Created by U_M0WY5 on 20.04.2018.
 */

public class PostActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_holder_activity);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), new PostFragment(), R.id.contentFrame, FRAGMENT_TRANSITION_ADD, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!checkIfInitialized()) finish();
    }
}