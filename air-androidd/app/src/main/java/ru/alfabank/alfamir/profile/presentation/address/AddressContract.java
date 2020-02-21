package ru.alfabank.alfamir.profile.presentation.address;

import ru.alfabank.alfamir.base_elements.BaseView;
import ru.alfabank.alfamir.base_elements.BasePresenter;

/**
 * Created by U_M0WY5 on 21.02.2018.
 */

public interface AddressContract {
    interface View extends BaseView<Presenter> {
        void showPhysicalAddress(String physicalAddress);
        void showWorkSpaceFull(String workSpaceFull);
        void showWorkSpaceShort(String workSpaceShort);
        void hidePhysicalAddress();
        void hideWorkSpaceFull();
        void hideWorkSpaceShort();
    }

    interface Presenter extends BasePresenter<View> {

    }
}
