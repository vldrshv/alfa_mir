package ru.alfabank.alfamir.favorites.data.source.repository;

import io.reactivex.Observable;
import ru.alfabank.alfamir.favorites.data.dto.FavoritePostRaw;
import ru.alfabank.alfamir.favorites.data.dto.FavoriteProfileRaw;
import ru.alfabank.alfamir.favorites.data.dto.PostInfo;

import java.util.List;

public interface FavoriteDataSource {

    Observable<List<FavoritePostRaw>> getFavoritePostList();

    Observable<List<FavoriteProfileRaw>> getFavoriteProfileList();

    Observable <String>  addFavoritePage(String feedUrl, String feedType, String postId);

    Observable <String> removeFavoritePage(String postId);

    Observable <String> addFavoritePerson(String userLogin);

    Observable <String> removeFavoritePerson(String userLogin);

    Observable<PostInfo> getPostInfo(String postUrl, String postId);

    class RequestValues {
        private boolean mIsCacheDirty;
        public RequestValues(boolean isCacheDirty){
            mIsCacheDirty = isCacheDirty;
        }
        public boolean getIsCacheDirty() {
            return mIsCacheDirty;
        }
    }
}
