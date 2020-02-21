package ru.alfabank.alfamir.favorites.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.favorites.data.source.repository.FavoriteRepository;

public class AddFavoritePerson extends UseCase<AddFavoritePerson.RequestValues, AddFavoritePerson.ResponseValue> {

    private FavoriteRepository mFavoriteRepository;

    @Inject
    AddFavoritePerson(FavoriteRepository favoriteRepository) {
        mFavoriteRepository = favoriteRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        String userLogin = requestValues.getUserLogin();
        return mFavoriteRepository.addFavoritePerson(userLogin)
                .flatMap(s -> {
                    mFavoriteRepository.refreshPeopleList();
                    return Observable.just(new ResponseValue());
                });
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String mUserLogin;
        public RequestValues(String userLogin){
            mUserLogin = userLogin;
        }
        public String getUserLogin() {
            return mUserLogin;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {

    }
}