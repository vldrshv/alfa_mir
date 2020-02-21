package ru.alfabank.alfamir.people.presentation.dummy_view;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.people.presentation.post_contract.PeopleListContract;

/**
 * Created by U_M0WY5 on 13.04.2018.
 */

public class PeopleAdapterDummy implements PeopleListContract.ListAdapter {

    @Override
    public void onImageDownloaded(int position, Bitmap binaryImage, boolean isNew) {

    }

    @Override
    public void openActivityProfileUi(String id) {

    }
}
