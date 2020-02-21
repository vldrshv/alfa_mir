package ru.alfabank.alfamir.main.home_fragment.presentation.dummy_view;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.main.home_fragment.presentation.contract.HomeFragmentContract;
import ru.alfabank.alfamir.main.home_fragment.presentation.contract.TopNewsAdapterContract;

public class HomeFragmentViewDummy implements HomeFragmentContract.View, TopNewsAdapterContract.Adapter{
    @Override
    public void setUserPic(Bitmap encodedImage, boolean isCached) {

    }

    @Override
    public void openProfileUi(String userId) {

    }

    @Override
    public void openNotificationUi() {

    }

    @Override
    public void openSearchUi() {

    }

    @Override
    public void showTopNews() {

    }

    @Override
    public void showCalendarEventListCards() {

    }

    @Override
    public void openPost(String postUrl) {

    }

    @Override
    public void makeCall(String phone) {

    }

    @Override
    public void hideTempBackground() {

    }

    @Override
    public void setSantaIcon() {

    }

    @Override
    public boolean checkCallPermission(){
        return false;
    }

    @Override
    public void requestCallPermission(int phoneType){

    }

    @Override
    public void showWarningOnExtraCharge(){

    }

    @Override
    public void callHH() {

    }

    @Override
    public void callHD() {

    }

    @Override
    public void update(boolean show) {

    }

}
