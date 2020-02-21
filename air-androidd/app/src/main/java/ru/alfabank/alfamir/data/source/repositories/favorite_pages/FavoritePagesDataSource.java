package ru.alfabank.alfamir.data.source.repositories.favorite_pages;

import io.reactivex.Observable;

public interface FavoritePagesDataSource {

    Observable <String> addPage(String type, String postId, String postUrl);

    Observable<String> removePage(String postId);

}
