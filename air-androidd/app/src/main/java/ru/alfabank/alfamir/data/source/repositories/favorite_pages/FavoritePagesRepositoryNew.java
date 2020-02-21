package ru.alfabank.alfamir.data.source.repositories.favorite_pages;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.dto.old_trash.models.MFavoritePage;
import ru.alfabank.alfamir.di.qualifiers.Remote;

@Singleton
public class FavoritePagesRepositoryNew implements FavoritePagesDataSource {

    private FavoritePagesDataSource mFavoritePagesRemoteDataSource;

    @Inject
    FavoritePagesRepositoryNew(@Remote FavoritePagesDataSource favoritePagesRemoteDataSource){
        mFavoritePagesRemoteDataSource = favoritePagesRemoteDataSource;
    }

    @Override
    public Observable<String> addPage(String type, String postId, String postUrl) {
        return mFavoritePagesRemoteDataSource.addPage(type, postId, postUrl);
    }

    @Override
    public Observable<String> removePage(String postId) {
        return mFavoritePagesRemoteDataSource.removePage(postId);
    }

    private void saveToCache(List<MFavoritePage> favoritePages){

    }

}
