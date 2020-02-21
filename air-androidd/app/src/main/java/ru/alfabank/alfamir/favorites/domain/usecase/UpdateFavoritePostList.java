package ru.alfabank.alfamir.favorites.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.favorites.data.source.repository.FavoriteRepository;

public class UpdateFavoritePostList extends UseCase<UpdateFavoritePostList.RequestValues, UpdateFavoritePostList.ResponseValue> {

    private FavoriteRepository mFavoriteRepository;

    @Inject
    UpdateFavoritePostList(FavoriteRepository favoriteRepository) {
        mFavoriteRepository = favoriteRepository;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        mFavoriteRepository.refreshPostList();
        return Observable.just(new ResponseValue());
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValue implements UseCase.ResponseValue {

    }
}