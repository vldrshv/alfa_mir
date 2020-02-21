package ru.alfabank.alfamir.favorites.domain.usecase;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.favorites.data.source.repository.FavoriteRepository;
import ru.alfabank.alfamir.favorites.domain.mapper.FavoriteProfileMapper;
import ru.alfabank.alfamir.favorites.presentation.dto.FavoriteProfile;

import javax.inject.Inject;
import java.util.List;

public class GetFavoriteProfileList extends UseCase<GetFavoriteProfileList.RequestValues, GetFavoriteProfileList.ResponseValue> {

    private FavoriteRepository mFavoriteRepository;
    private FavoriteProfileMapper mFavoriteProfileMapper;

    @Inject
    GetFavoriteProfileList(FavoriteRepository favoriteRepository,
                        FavoriteProfileMapper favoriteProfileMapper) {
        mFavoriteRepository = favoriteRepository;
        mFavoriteProfileMapper = favoriteProfileMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        return mFavoriteRepository.getFavoriteProfileList(false)
                .flatMapIterable(favoriteProfileRawList -> favoriteProfileRawList)
                .map(mFavoriteProfileMapper)
                .toList()
                .flatMapObservable(favoriteProfileList -> Observable.just(new ResponseValue(favoriteProfileList)));
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private List<FavoriteProfile> mFavoriteProfiles;
        public ResponseValue(List<FavoriteProfile> favoriteProfiles){
            mFavoriteProfiles = favoriteProfiles;
        }
        public List<FavoriteProfile> getmFavoriteProfiles() {
            return mFavoriteProfiles;
        }
    }
}