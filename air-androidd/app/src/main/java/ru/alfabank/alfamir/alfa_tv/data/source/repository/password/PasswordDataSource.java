package ru.alfabank.alfamir.alfa_tv.data.source.repository.password;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import ru.alfabank.alfamir.alfa_tv.data.dto.SavedPassword;

public interface PasswordDataSource {

    Completable savePassword(SavedPassword savedPassword);

    Observable<List<Integer>> getShowIdWithSavedPasswords();

    Observable<Boolean> ifPasswordIsSaved(int id);

    PublishSubject<SavedPassword> subscribeForUpdates();

}
