package ru.alfabank.alfamir.favorites.data.source.repository

import io.reactivex.Observable
import ru.alfabank.alfamir.favorites.data.dto.FavoritePostRaw
import ru.alfabank.alfamir.favorites.data.dto.FavoriteProfileRaw

interface FavoriteRepo {

    fun getFavoritePostList(): Observable<List<FavoritePostRaw>>

    fun getFavoriteProfileList(isCacheDirty: Boolean): Observable<List<FavoriteProfileRaw>>

    fun addFavoritePage(feedUrl: String, feedType: String, postId: String)

    fun removeFavoritePage(postId: String)

    fun addFavoritePerson(userLogin: String): Observable<String>

    fun removeFavoritePerson(userLogin: String): Observable<String>

    class RequestValues(val isCacheDirty: Boolean)

    class AddFavoritePageRequestValues(val feedUrl: String, val feedType: String, val postId: String)
}