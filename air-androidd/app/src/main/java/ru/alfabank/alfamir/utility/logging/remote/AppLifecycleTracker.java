package ru.alfabank.alfamir.utility.logging.remote;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import javax.inject.Inject;

import ru.alfabank.alfamir.utility.logging.local.LogWrapper;

/**
 * Created by U_M0WY5 on 07.03.2018.
 */

public class AppLifecycleTracker implements Application.ActivityLifecycleCallbacks, LoggerContract.Client.LifecycleTracker {

    private int numStarted = 0;
    @Inject
    LoggerContract.Provider mLogger;
    private LogWrapper mLog;

    @Inject
    public AppLifecycleTracker(LoggerContract.Provider logger,
                               LogWrapper logWrapper){
        mLogger = logger;
        mLog = logWrapper;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        mLog.debug("kek","lel");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (numStarted == 0) {
            logForeground();
            // app went to foreground
        }
        numStarted++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mLog.debug("kek","lel");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mLog.debug("kek","lel");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        numStarted--;
        if (numStarted == 0) {
            logBackground();
            // app went to background
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mLog.debug("kek","lel");
    }

    @Override
    public void logBackground() {
        mLogger.background();
    }

    @Override
    public void logForeground() {
        mLogger.foreground();
    }
}
