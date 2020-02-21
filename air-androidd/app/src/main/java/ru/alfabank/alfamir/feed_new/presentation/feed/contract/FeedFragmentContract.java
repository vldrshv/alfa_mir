package ru.alfabank.alfamir.feed_new.presentation.feed.contract;

import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface FeedFragmentContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {

    }

}
