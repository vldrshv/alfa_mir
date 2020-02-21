package ru.alfabank.alfamir.alfa_tv.data.source.repository.password;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import ru.alfabank.alfamir.alfa_tv.data.dto.SavedPassword;

@Singleton
public class PasswordRepository implements PasswordDataSource {

    private List<SavedPassword> mCachedPasswords = new ArrayList<>();
    private PublishSubject<SavedPassword> subject = PublishSubject.create();

    @Inject
    PasswordRepository(){}

    @Override
    public Completable savePassword(SavedPassword savedPassword) {
        return Completable.create(e -> {
            mCachedPasswords.add(savedPassword);
            subject.onNext(savedPassword);
            e.onComplete();
        });
    }

    @Override
    public Observable<List<Integer>> getShowIdWithSavedPasswords() {
        List<Integer> ids = new ArrayList<>();
        for (SavedPassword savedPassword : mCachedPasswords){
            ids.add(savedPassword.getId());
        }
        return Observable.just(ids);
    }

    @Override
    public Observable<Boolean> ifPasswordIsSaved(int id) {
        boolean isSaved = false;
        for (SavedPassword savedPassword : mCachedPasswords){
            if(savedPassword.getId() == id){
                isSaved = true;
            }
        }
        return Observable.just(isSaved);
    }

    @Override
    public PublishSubject<SavedPassword> subscribeForUpdates() {
        return subject;
    }
}
