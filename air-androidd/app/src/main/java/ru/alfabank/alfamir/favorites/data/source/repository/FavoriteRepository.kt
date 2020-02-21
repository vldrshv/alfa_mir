package ru.alfabank.alfamir.favorites.data.source.repository

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.alfabank.alfamir.di.qualifiers.Remote
import ru.alfabank.alfamir.favorites.data.dto.FavoritePostRaw
import ru.alfabank.alfamir.favorites.data.dto.FavoriteProfileRaw
import ru.alfabank.alfamir.favorites.data.dto.PostInfo
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository

@Inject
internal constructor(@param:Remote private val mFavoriteDataSource: FavoriteDataSource) : FavoriteRepo {

    private var mFavoritePostRawCache: List<FavoritePostRaw>? = null
    private var mFavoriteProfileRawCache: List<FavoriteProfileRaw>? = null
    private val mLock = Any()
    @Volatile
    private var mIsPostCacheDirty = false
    @Volatile
    private var mIsProfileCacheDirty = false

    override fun getFavoritePostList(): Observable<List<FavoritePostRaw>> {
        return mFavoriteDataSource.favoritePostList
                .flatMap { favoritePostRaws ->
                    favoritePostRaws.reverse()
                    savePostToCache(favoritePostRaws)
                    Observable.just(favoritePostRaws)
                }
    }

    override fun getFavoriteProfileList(isCacheDirty: Boolean): Observable<List<FavoriteProfileRaw>> {
        return if (isCacheDirty || mIsProfileCacheDirty || mFavoriteProfileRawCache == null) {
            mFavoriteDataSource.favoriteProfileList
                    .flatMap { favoriteProfileRaws ->
                        saveProfileToCache(favoriteProfileRaws)
                        Observable.just(favoriteProfileRaws)
                    }
        } else Observable.just(mFavoriteProfileRawCache!!)
    }

    @SuppressLint("CheckResult")
    override fun addFavoritePage(feedUrl: String, feedType: String, postId: String) {
//        favoritePages.addPage(feedUrl, feedType, postId)
        mFavoriteDataSource.addFavoritePage(feedUrl, feedType, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshPostList() },
                        Throwable::printStackTrace)
    }

    @SuppressLint("CheckResult")
    override fun removeFavoritePage(postId: String) {
        mFavoriteDataSource.removeFavoritePage(postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshPostList() },
                        Throwable::printStackTrace)
    }

    override fun addFavoritePerson(userLogin: String): Observable<String> {
        return mFavoriteDataSource.addFavoritePerson(userLogin)
    }

    override fun removeFavoritePerson(userLogin: String): Observable<String> {
        return mFavoriteDataSource.removeFavoritePerson(userLogin)
    }

    fun refreshPostList() {
        mIsPostCacheDirty = true
    }

    fun refreshPeopleList() {
        mIsProfileCacheDirty = true
    }

    private fun savePostToCache(favoritePostRaws: List<FavoritePostRaw>) {
        synchronized(mLock) {
            if (mFavoritePostRawCache == null) {
                mFavoritePostRawCache = ArrayList()
            }
            mFavoritePostRawCache = favoritePostRaws
            mIsPostCacheDirty = false
        }
    }

    private fun saveProfileToCache(favoriteProfileRaws: List<FavoriteProfileRaw>) {
        synchronized(mLock) {
            if (mFavoriteProfileRawCache == null) {
                mFavoriteProfileRawCache = ArrayList()
            }
            mFavoriteProfileRawCache = favoriteProfileRaws
            mIsProfileCacheDirty = false
        }
    }

    fun isFavorite(postUrl: String, postId: String): Observable<PostInfo> {
        return mFavoriteDataSource.getPostInfo(postUrl, postId)
    }
}
