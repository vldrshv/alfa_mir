package ru.alfabank.alfamir.utility.lifecycle

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import ru.alfabank.alfamir.Constants.Initialization.MESSENGER_ENABLED
import ru.alfabank.alfamir.data.source.broadcast_receiver.сonnectionTracker.ConnectionTracker
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerRepository
import javax.inject.Inject

class AppLIfeCycleListener @Inject constructor(val messengerRepository: MessengerRepository, val connectionHandler: ConnectionTracker) : LifecycleObserver {

    @SuppressLint("CheckResult")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun movedToForeground() {
        if (MESSENGER_ENABLED) {
            messengerRepository.connect()
            connectionHandler.reportInitialised()
            connectionHandler.reportOnline()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun movedToBackground() {
        connectionHandler.reportOffline()
    }
}