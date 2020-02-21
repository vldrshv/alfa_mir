package ru.alfabank.alfamir.initialization.presentation.initialization.contract;

import org.json.JSONObject;

import ru.alfabank.alfamir.base_elements.BasePresenter;
import ru.alfabank.alfamir.base_elements.BaseView;

public interface InitializationContract {

    interface View extends BaseView<Presenter> {
        void close();
    }

    interface Presenter extends BasePresenter<View> {
        void onSapResponse(JSONObject object);
        void makeSapRequest();
    }
}
