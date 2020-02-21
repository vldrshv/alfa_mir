package ru.alfabank.alfamir.search.presentation.dummy_view;

import ru.alfabank.alfamir.search.presentation.contract.SearchAdapterContract;
import ru.alfabank.alfamir.search.presentation.contract.SearchFragmentContract;

public class SearchViewDummy implements SearchFragmentContract.View, SearchAdapterContract.Adapter {
    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onProfileImageLoaded(int position, long viewId, String encodedImage, boolean isAnimated) {

    }

    @Override
    public void onPostImageLoaded(int position, long viewId, String encodedImage, boolean isAnimated) {

    }

    @Override
    public void openPostUi(String feedId, String postId, String feedUrl, String feedType) {

    }

    @Override
    public void showResults() {

    }

    @Override
    public void showKeyboard() {

    }

    @Override
    public void hideKeyboard() {

    }

    @Override
    public void openProfileUi(String id) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public String getUserInput() {
        return null;
    }
}
