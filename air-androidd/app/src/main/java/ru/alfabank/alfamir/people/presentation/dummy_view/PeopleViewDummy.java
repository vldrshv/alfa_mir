package ru.alfabank.alfamir.people.presentation.dummy_view;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.data.dto.ShortProfile;
import ru.alfabank.alfamir.people.presentation.post_contract.PeopleContract;

import java.util.List;

/**
 * Created by U_M0WY5 on 12.04.2018.
 */

public class PeopleViewDummy implements PeopleContract.View {

    @Override
    public void showProfiles(List<ShortProfile> profiles) {

    }

    @Override
    public void dismiss() {

    }

    @Override
    public void showEmptyState(boolean isMyProfile, Bitmap binaryImage) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}
