package ru.alfabank.alfamir.data.source.broadcast_receiver.networkBR;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import ru.alfabank.alfamir.data.source.broadcast_receiver.сonnectionTracker.ConnectionTracker;

import javax.inject.Inject;

import static android.net.NetworkInfo.DetailedState.CONNECTED;
import static android.net.NetworkInfo.DetailedState.DISCONNECTED;

public class NetworkBRImp extends BroadcastReceiver implements NetworkBR {
    private ConnectionTracker mConnectionTracker;

    @Inject
    NetworkBRImp(ConnectionTracker connectionTracker) {
        mConnectionTracker = connectionTracker;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        if (networkInfo == null) return;
        if (networkInfo.getDetailedState() == CONNECTED) {
            mConnectionTracker.reportOnline();
        }
        if (networkInfo.getDetailedState() == DISCONNECTED) {
            mConnectionTracker.reportOffline();
        }
    }
}