package ru.alfabank.alfamir.utility.update_notifier;

import io.reactivex.Observable;
import ru.alfabank.alfamir.utility.update_notifier.notifications.UpdateNotification;

public interface UpdateNotifier {

    void reportUpdate(UpdateNotification notification);

    Observable<UpdateNotification> getConnectionStatusListener();

}
