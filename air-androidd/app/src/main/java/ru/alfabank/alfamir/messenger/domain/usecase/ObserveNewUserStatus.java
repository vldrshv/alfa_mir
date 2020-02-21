package ru.alfabank.alfamir.messenger.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerRepository;
import ru.alfabank.alfamir.messenger.domain.mapper.UserMapper;
import ru.alfabank.alfamir.messenger.presentation.dto.User;

public class ObserveNewUserStatus extends UseCase<ObserveNewUserStatus.RequestValues, ObserveNewUserStatus.ResponseValue> {
    private MessengerRepository mMessengerRepository;
    private UserMapper mUserMapper;

    @Inject
    public ObserveNewUserStatus(MessengerRepository messengerRepository,
                                UserMapper userMapper){
        mMessengerRepository = messengerRepository;
        mUserMapper = userMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        return Observable.just(new ResponseValue(mMessengerRepository.subscribeForUser()
                .map(mUserMapper)));
    }

    public static class RequestValues implements UseCase.RequestValues { }

    public static class ResponseValue implements UseCase.ResponseValue {
        private Observable<User> mUserObservable;
        ResponseValue(Observable <User> userObservable){
            mUserObservable = userObservable;
        }
        public Observable<User> getUserObservable() {
            return mUserObservable;
        }
    }

}