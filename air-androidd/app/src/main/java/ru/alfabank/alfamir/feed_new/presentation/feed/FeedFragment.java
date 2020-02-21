package ru.alfabank.alfamir.feed_new.presentation.feed;

import javax.inject.Inject;

import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.feed_new.presentation.feed.contract.FeedFragmentContract;

public class FeedFragment extends BaseFragment implements FeedFragmentContract.View {

    @Inject
    FeedFragmentContract.Presenter mPresenter;

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }
}
