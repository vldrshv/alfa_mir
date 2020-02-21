package ru.alfabank.alfamir.data.source.broadcast_receiver.сonnectionTracker;

import io.reactivex.Observable;

public interface ConnectionTracker {
    void reportOnline();
    void reportOffline();
    void reportInitialised();
    Observable <Boolean> getConnectionStatusListener();

    interface Reporter {
        void reportOnline();
        void reportOffline();
    }
}
