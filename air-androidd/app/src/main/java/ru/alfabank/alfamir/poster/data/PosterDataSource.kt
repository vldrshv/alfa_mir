package ru.alfabank.alfamir.poster.data

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.alfabank.alfamir.data.source.remote.api.WebService
import ru.alfabank.alfamir.poster.data.dto.Poster
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory
import javax.inject.Inject


class PosterDataSource @Inject constructor(private var webService: WebService) {

    @SuppressLint("CheckResult")
    fun getPost(id: Int): Observable<Poster> {
        val request = RequestFactory.formGetPosterEventRequest(id)

        return webService.requestX(request).map {
            JsonWrapper.posterList(it)
        }
    }

    @SuppressLint("CheckResult")
    fun sendQuestion(body: String, anonymous: Int, eventId: Int): Observable<String> {
        val request = RequestFactory.sendQuestion(body, anonymous, eventId)
        return webService.requestX(request)
    }

    @SuppressLint("CheckResult")
    fun like(id: Int) {
        val request = RequestFactory.likeQuestion(id)
        webService.requestX(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { e ->
                    e.printStackTrace()
                })
    }

    @SuppressLint("CheckResult")
    fun unlike(id: Int) {
        val request = RequestFactory.unlikeQuestion(id)
        webService.requestX(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { e ->
                    e.printStackTrace()
                })
    }
}