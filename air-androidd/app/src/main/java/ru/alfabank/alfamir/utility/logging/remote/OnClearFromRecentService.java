package ru.alfabank.alfamir.utility.logging.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by U_M0WY5 on 07.03.2018.
 */

public class OnClearFromRecentService extends Service implements LoggerContract.Client.AppDestroyedTracker {
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void logEndSession() {
//
//    }
//    @Inject
//    public LoggerContract.Provider logger;

    public OnClearFromRecentService(){
//        logger = Logger.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        logEndSession();
        //Code here
        stopSelf();
    }

    @Override
    public void logEndSession() {
//        logger.endSession();
    }
}
