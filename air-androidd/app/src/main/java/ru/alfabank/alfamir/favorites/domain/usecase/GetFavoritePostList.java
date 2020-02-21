package ru.alfabank.alfamir.favorites.domain.usecase;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.favorites.data.source.repository.FavoriteRepository;
import ru.alfabank.alfamir.favorites.domain.mapper.FavoritePostMapper;
import ru.alfabank.alfamir.favorites.presentation.dto.FavoritePost;

import javax.inject.Inject;
import java.util.List;

public class GetFavoritePostList extends UseCase<GetFavoritePostList.RequestValues, GetFavoritePostList.ResponseValue> {

    private FavoriteRepository mFavoriteRepository;
    private FavoritePostMapper mFavoritePostMapper;

    @Inject
    GetFavoritePostList(FavoriteRepository favoriteRepository,
                        FavoritePostMapper favoritePostMapper) {
        mFavoriteRepository = favoriteRepository;
        mFavoritePostMapper = favoritePostMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        return mFavoriteRepository.getFavoritePostList()
                .flatMapIterable(favoritePostRawList -> favoritePostRawList)
                .map(mFavoritePostMapper)
                .toList()
                .flatMapObservable(topNews -> Observable.just(new ResponseValue(topNews)));
    }

    public static class RequestValues implements UseCase.RequestValues {
        private boolean mIsCacheDirty;
        public RequestValues(boolean isCacheDirty){
            mIsCacheDirty = isCacheDirty;
        }
        public boolean getIsCacheDirty() {
            return mIsCacheDirty;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private List<FavoritePost> mFavoritePosts;
        public ResponseValue(List<FavoritePost> favoritePosts){
            mFavoritePosts = favoritePosts;
        }
        public List<FavoritePost> getmFavoritePosts() {
            return mFavoritePosts;
        }
    }
}