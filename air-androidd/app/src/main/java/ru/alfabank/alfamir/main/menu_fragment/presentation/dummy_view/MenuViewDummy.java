package ru.alfabank.alfamir.main.menu_fragment.presentation.dummy_view;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.main.menu_fragment.presentation.contract.MenuFragmentContract;

public class MenuViewDummy implements MenuFragmentContract.View {
    @Override
    public void setUserPic(Bitmap encodedImage, boolean isCached) {

    }

    @Override
    public void setUserName(String name) {

    }

    @Override
    public void setUserPosition(String position) {

    }

    @Override
    public void openProfileActivityUi(String userId) {

    }

    @Override
    public void setAppVersion(String version) {

    }

    @Override
    public void setAppVersionComment(String comment) {

    }

    @Override
    public void openSettingsActivityUi() {

    }
}
