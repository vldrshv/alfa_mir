package ru.alfabank.alfamir.base_elements;

import android.os.Bundle;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.initialization.domain.utilities.InitializationController;

public class BaseActivity extends DaggerAppCompatActivity {

    @Inject
    InitializationController initializationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DefaultTheme);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean checkIfInitialized() {
        return initializationController.checkIfInitialized();
    }
}
