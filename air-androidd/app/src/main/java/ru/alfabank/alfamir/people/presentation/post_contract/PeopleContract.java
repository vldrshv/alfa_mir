package ru.alfabank.alfamir.people.presentation.post_contract;

import android.graphics.Bitmap;
import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;
import ru.alfabank.alfamir.data.dto.ShortProfile;

import java.util.List;

/**
 * Created by U_M0WY5 on 12.04.2018.
 */

public interface PeopleContract {
    interface View extends BaseView<Presenter> {
        void showProfiles(List<ShortProfile> profiles);
        void dismiss();
        void showEmptyState(boolean isMyProfile, Bitmap binaryImage);
        void showProgressBar();
        void hideProgressBar();
    }

    interface Presenter extends BasePresenter<View> {
        void refreshList();
        void like();
    }
}
