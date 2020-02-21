package ru.alfabank.alfamir.data.source.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by mshvdvsk on 29/03/2018.
 */

public class ProfileModule {

    private static ProfileModule INSTANCE;

    private PublishSubject<String> subject;

    private ProfileModule(){
        subject = PublishSubject.create();
    }

    public static ProfileModule getInstance(){
        if (INSTANCE == null){
            INSTANCE = new ProfileModule();
        }
        return INSTANCE;
    }

    public void updateProfile() {
        subject.onNext("update");
    }

    public Observable<String> listenForUpdate() {
        return subject;
    }

}
