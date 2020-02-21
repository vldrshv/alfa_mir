package ru.alfabank.alfamir.favorites.data.source.remote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.favorites.data.dto.FavoritePostRaw;
import ru.alfabank.alfamir.favorites.data.dto.FavoriteProfileRaw;
import ru.alfabank.alfamir.favorites.data.dto.PostInfo;
import ru.alfabank.alfamir.favorites.data.source.repository.FavoriteDataSource;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;

public class FavoriteRemoteDataSource implements FavoriteDataSource {

    private WebService mService;

    @Inject
    FavoriteRemoteDataSource(WebService service){
        mService = service;
    }

    @Override
    public Observable<List<FavoritePostRaw>> getFavoritePostList() {
        String request = RequestFactory.INSTANCE.formFavoritePostRequest();
        return mService.requestX(request)
                .map(JsonWrapper::getFavoritePostList);
    }

    @Override
    public Observable<List<FavoriteProfileRaw>> getFavoriteProfileList() {
        String request = RequestFactory.INSTANCE.favoriteProfiles();
        return mService.requestX(request)
                .map(JsonWrapper::getFavoritePeopleList);
    }

    @Override
    public Observable <String> addFavoritePage(String feedUrl, String feedType, String postId) {
        String request = RequestFactory.INSTANCE.addPostToFavorite(feedUrl, feedType, postId);
        return mService.requestX(request);
    }

    @Override
    public Observable<String> removeFavoritePage(String postId) {
        String request = RequestFactory.INSTANCE.removePostFromFavorite(postId);
        return mService.requestX(request);
    }

    @Override
    public Observable<String> addFavoritePerson(String userLogin) {
        String request = RequestFactory.INSTANCE.addUserToFavorite(userLogin);
        return mService.requestX(request);
    }

    @Override
    public Observable<String> removeFavoritePerson(String userLogin) {
        String request = RequestFactory.INSTANCE.removeUserFromFavorite(userLogin);
        return mService.requestX(request);
    }

    @Override
    public Observable<PostInfo> getPostInfo(String postUrl, String postId) {
        String request = RequestFactory.INSTANCE.getPostInfo(postUrl, postId);
        return mService.requestX(request)
                .map(response -> JsonWrapper.getPostInfo(response));
    }
}
