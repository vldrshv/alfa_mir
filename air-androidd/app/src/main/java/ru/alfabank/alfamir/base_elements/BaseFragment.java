package ru.alfabank.alfamir.base_elements;


import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import ru.alfabank.alfamir.initialization.domain.utilities.InitializationController;

public class BaseFragment extends DaggerFragment  {

    @Inject
    InitializationController initializationController;

    public boolean checkIfInitialized() {
        return initializationController.checkIfInitialized();
    }

}
