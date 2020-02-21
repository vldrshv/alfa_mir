package ru.alfabank.alfamir.initialization.domain.utilities;

import android.content.Context;
import android.content.Intent;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.alfabank.alfamir.di.qualifiers.AppContext;
import ru.alfabank.alfamir.initialization.presentation.initialization.InitializationActivity;


@Singleton
public class InitializationController {

    private AtomicBoolean mIsInitialized = new AtomicBoolean(false);
    private AtomicBoolean mIsInitializing = new AtomicBoolean(false);
    private Context mContext;

    @Inject
    public InitializationController(@AppContext Context context) {
        mContext = context;
    }

    public boolean checkIfInitialized() {
        boolean isInitialized = mIsInitialized.get();
        boolean isInitializing = mIsInitializing.get();
        if (!isInitialized && !isInitializing) {
            initialize();
            return false;
        }
        return true;
    }

    public void setIsInitialized(boolean isInitialized) {
        mIsInitialized.set(isInitialized);
    }

    private void initialize() {
        mIsInitializing.set(true);
        Intent intent = new Intent(mContext, InitializationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
