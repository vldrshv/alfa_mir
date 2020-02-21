package ru.alfabank.alfamir.data.source.broadcast_receiver.сonnectionTracker;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class ConnectionTrackerImpl implements ConnectionTracker {
    private static PublishSubject<Boolean> subject = PublishSubject.create();
    private static boolean mConnectionState;
    private boolean mIsInitialized = false;

    @Inject
    ConnectionTrackerImpl(){}

    @Override
    public Observable<Boolean> getConnectionStatusListener(){
        return subject;
    }

    @Override
    public void reportOnline() {
        if(!mIsInitialized) return;
        if(mConnectionState) return;
        mConnectionState = true;
        subject.onNext(mConnectionState);
    }

    @Override
    public void reportOffline(){
        if(!mIsInitialized) return;
        if(!mConnectionState) return;
        mConnectionState = false;
        subject.onNext(mConnectionState);
    }

    @Override
    public void reportInitialised() {
        mIsInitialized = true;
    }
}
