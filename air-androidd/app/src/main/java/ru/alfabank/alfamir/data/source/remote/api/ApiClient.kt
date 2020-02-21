package ru.alfabank.alfamir.data.source.remote.api

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.alfabank.alfamir.Constants

interface ApiClient {
    @Headers("Content-Type: application/xml", "User-Agent: Android")
    @POST(Constants.REGISTRATION_SUF)
    fun register(@Header("Authorization") authorization: String?, @Body body: String?): Observable<String>

    @Headers("Content-Type: application/json", "User-Agent: Android")
    @POST(Constants.DATA_SUF)
    fun getData(@Header("Authorization") authorization: String?, @Body body: String?): Observable<String>

    @Headers("Content-Type: application/json", "User-Agent: Android")
    @POST(Constants.DATA_SUF)
    fun getDataCallback(@Header("Authorization") authorization: String?, @Body body: String?): Call<String>

    @Headers("Content-Type: application/json", "User-Agent: Android")
    @POST(Constants.DATA_SUF)
    suspend fun getStringData(@Header("Authorization") authorization: String?, @Body body: String?): String

//    @Headers("Content-Type: application/json", "User-Agent: Android")
//    @POST(Constants.DATA_SUF)
//    fun getVideoData(@Header("Authorization") authorization: String?, @Body body: String?): Call<ResponseBody>
}