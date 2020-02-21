package ru.alfabank.alfamir.utility.update_notifier;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import ru.alfabank.alfamir.utility.update_notifier.notifications.UpdateNotification;

public class UpdateNotifierImpl implements UpdateNotifier{

    @Inject
    public UpdateNotifierImpl(){}

    private static PublishSubject<UpdateNotification> subject = PublishSubject.create();

    @Override
    public void reportUpdate(UpdateNotification notification) {
        subject.onNext(notification);
    }

    @Override
    public Observable<UpdateNotification> getConnectionStatusListener() {
        return subject;
    }
}
