package ru.alfabank.alfamir.alfa_tv.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import ru.alfabank.alfamir.alfa_tv.data.dto.SavedPassword;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.password.PasswordRepository;
import ru.alfabank.alfamir.base_elements.UseCase;

public class ObserveSavedPassword extends UseCase<ObserveSavedPassword.RequestValues, ObserveSavedPassword.ResponseValue> {

    private PasswordRepository mPasswordRepository;

    @Inject
    public ObserveSavedPassword(PasswordRepository passwordRepository){
        mPasswordRepository = passwordRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        return Observable.just(new ResponseValue(mPasswordRepository.subscribeForUpdates()));
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private PublishSubject<SavedPassword> mPSubject;

        ResponseValue(PublishSubject<SavedPassword> pSubject){
            mPSubject = pSubject;
        }

        public PublishSubject<SavedPassword> getPSubject() {
            return mPSubject;
        }
    }
}