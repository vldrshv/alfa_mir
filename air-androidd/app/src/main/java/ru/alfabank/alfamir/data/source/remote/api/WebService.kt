package ru.alfabank.alfamir.data.source.remote.api

import android.content.Context
import android.content.Intent
import android.net.*
import android.os.Build
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.alfabank.alfamir.App
import ru.alfabank.alfamir.Constants
import ru.alfabank.alfamir.Constants.Initialization.AUTH_STRING
import ru.alfabank.alfamir.Constants.Initialization.PHOTOS_DISABLED
import ru.alfabank.alfamir.Constants.Initialization.USER_ID
import ru.alfabank.alfamir.Constants.Log.LOG_RESPONSE
import ru.alfabank.alfamir.data.dto.notifications_settings.NotificationsSettings
import ru.alfabank.alfamir.data.dto.old_trash.models.ResponsePicUpload
import ru.alfabank.alfamir.data.source.repositories.favorite_people.FavoritePeopleDataSource
import ru.alfabank.alfamir.data.source.repositories.old_trash.PostPhotoRepository
import ru.alfabank.alfamir.feed.data.source.repository.PostDataSource
import ru.alfabank.alfamir.image.data.source.repository.ImageDataSource
import ru.alfabank.alfamir.initialization.presentation.initialization.InitializationPresenter
import ru.alfabank.alfamir.initialization.presentation.initialization.contract.InitializationContract
import ru.alfabank.alfamir.notification.data.source.repository.NotificationsDataSource
import ru.alfabank.alfamir.profile.data.source.repository.ProfileDataSource
import ru.alfabank.alfamir.survey.data.dto.Answer
import ru.alfabank.alfamir.survey.data.source.repository.SurveyDataSource
import ru.alfabank.alfamir.utility.callbacks.*
import ru.alfabank.alfamir.utility.logging.local.LogWrapper
import ru.alfabank.alfamir.utility.logging.remote.Logger
import ru.alfabank.alfamir.utility.logging.remote.events.Event
import ru.alfabank.alfamir.utility.network.CookiesInterceptor
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("SimpleRedundantLet")
@Singleton
class WebService @Inject
constructor(private val mLog: LogWrapper) {

    private lateinit var presenter: InitializationContract.Presenter

    private var client: ApiClient
    private var connected = true

    init {
        val builder = OkHttpClient().newBuilder()
        val okHttpClient = builder
                .connectTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .addNetworkInterceptor(CookiesInterceptor.saveCookies)
                .addNetworkInterceptor(CookiesInterceptor.addCookies)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_SERVER)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        client = retrofit.create(ApiClient::class.java)
    }

    fun setApiClient(client: ApiClient) {
        this.client = client
    }

    fun setPresenter(presenter: InitializationContract.Presenter) {
        this.presenter = presenter
    }

    fun register(): Observable<String> {
        return client.register(AUTH_STRING, Constants.REGISTRATION_XML)
                .doOnError { it.onError() }
    }

    fun requestX(request: String): Observable<String> {
        return client.getData(AUTH_STRING, request)
                .doOnError {
                    it.onError()
                }
                .map { result ->
                    Log.d("Request:", request)
                    Log.d("Response:", result)
                    result
                }
    }

    private val codes = arrayListOf(401, 403, 404)

    private fun Throwable.onError() {
        Log.d("WebService:", this.localizedMessage)
        if (this is HttpException && codes.contains(code())) {
            presenter.makeSapRequest()
        }
    }

    fun getSettings(): Observable<String> {
        return client.getData(AUTH_STRING, RequestFactory.settings)
    }

    fun getComments(postType: String, threadId: String, postUrl: String, callback: PostDataSource.LoadCommentsCallback) {
        val request = RequestFactory.formGetCommentsRequest(postType, threadId, postUrl)
        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val json = response.body()
                        val comments = JsonWrapper.getComments(json)
                        if (comments == null) {
                            callback.onDataNotAvailable()
                        } else {
                            callback.onCommentsLoaded(comments)
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onDataNotAvailable()
                    }
                })
    }

    fun getPostPictureBlurred(url: String, width: Int, height: Int, callbackDownloaded: DownloadingPostPicBlurred,
                              callbackViewHolder: PostPhotoRepository.InflatingPostPicBlurred, position: Int, blurType: Int) {

        if (!PHOTOS_DISABLED) {
            client.getDataCallback(AUTH_STRING, "{'type':'image','parameters':{'obj':'getfromurl','url':'$url','width':'$width','height':'$height', isoriginal:'0', hq:'2' }}")
                    .enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            try {
                                val base64 = JsonWrapper(response.body()!!).photo.imgbasecode
                                callbackDownloaded.onDownloadedPostPicBlurred(base64, url, callbackViewHolder, position, blurType)
                            } catch (e: Exception) {
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                        }
                    })
        }
    }

    fun getPostPictureAnimated(url: String, width: Int, height: Int, callbackDownloaded: DownloadingPostPicAnimated, callbackViewHolder: InflatingPostPic, position: Int) {

        if (!PHOTOS_DISABLED) {
            client.getDataCallback(AUTH_STRING, "{'type':'image','parameters':{'obj':'getfromurl','url':'$url','width':'$width','height':'$height', isoriginal:'0', hq:'2' }}")
                    .enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            mLog.debug("0kek0 ", "$width $height $url")
                            try {
                                val base64 = JsonWrapper(response.body()!!).photo.imgbasecode
                                callbackDownloaded.onDownloadedPostPicAnimated(base64, url, callbackViewHolder, position)
                            } catch (e: Exception) {
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {

                        }
                    })
        }
    }

    fun getProfilePictureAnimated(url: String, width: Int, height: Int, callbackDownloaded: DownloadingProfilePic, callbackViewHolder: InflatingProfilePic, position: Int) {

        if (!PHOTOS_DISABLED) {
            client.getDataCallback(AUTH_STRING, "{'type':'image','parameters':{'obj':'getfromurl','url':'$url','width':'$width','height':'$height', isoriginal:'0', hq:'0' }}")
                    .enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            try {
                                val base64 = JsonWrapper(response.body()!!).photo.imgbasecode
                                val test = base64.length
                                mLog.debug("for_gods_sake", test.toString() + "")
                                callbackDownloaded.onDownloadedProfilePicAnimated(base64, url, callbackViewHolder, position)
                            } catch (e: Exception) {
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {

                        }
                    })
        }
    }

    fun getCommunitiesTypes(callback: JsonGetter, requestBody: String) {
        client.getDataCallback(AUTH_STRING, requestBody)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onResponse(JsonWrapper(response.body()!!), 4)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                    }
                })
    }

    fun getTeam(callback: JsonGetter, teamType: String, id: String) {
        client.getDataCallback(AUTH_STRING, RequestFactory.team(teamType, id))
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onResponse(JsonWrapper(response.body()!!), 0)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onFailure(JsonWrapper(""), 0)
                    }
                })
    }

    fun getProfiles(profileId: String, callback: ProfileDataSource.LoadProfilesCallback) {
        val request = RequestFactory.formProfilesRequest(profileId)
        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val profiles = JsonWrapper(response.body()!!).profiles
                        callback.onProfilesLoaded(profiles)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onDataNotAvailable()
                    }
                })
    }

    fun deletePost(postType: String, postUrl: String, postId: String, callback: PostDataSource.DeletePostCallback) {
        val request = RequestFactory.formDeletePostRequest(postType, postId, postUrl)
        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onPostDeleted()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onServerNotAvailable()
                    }
                })
    }

    fun sendLog(events: List<Event>, callback: Logger.LogEventsCallback) {

        val request = RequestFactory.formLogEvents(events)
        client.getDataCallback(AUTH_STRING, request).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                callback.onEventsLogged()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                callback.onDataNotAvailable()
            }
        })

    }

    fun uploadPhoto(callback: JsonGetter, destination: String, image: String) {
        val request = "{type:'image', parameters:{obj: 'load', destination:'$destination',image:'$image'}}"
        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onResponse(JsonWrapper(response.body()!!), 1)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {

                    }
                })
    }

    suspend fun uploadPhoto(destination: String, image: String): String? {
        val request = "{type:'image', parameters:{obj: 'load', destination:'$destination',image:'$image'}}"

        return try {
            val response = client.getStringData(AUTH_STRING, request)
            Gson().fromJson(response, ResponsePicUpload::class.java).url
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun uploadImage(encodedImage: String, callback: ImageDataSource.UploadImageCallback) {
        client.getDataCallback(AUTH_STRING, RequestFactory.formUploadImage(encodedImage))
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onImageUploaded()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onServerNotAvailable()
                    }
                })
    }

    fun deletePhoto(callback: JsonGetter, destination: String, url: String) {
        client.getDataCallback(AUTH_STRING, "{type:'image', parameters:{obj: 'delete', destination:'$destination',, fileurl:'$url'}}")
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onResponse(JsonWrapper(response.body()!!), 2)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {}
                })
    }

    fun publishPost(callback: JsonGetter?, request: String) {

        val intent = Intent()
        intent.action = Constants.ACTION_POST_SENDING
        val context = (presenter as InitializationPresenter).getContext()

        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback?.onResponse(JsonWrapper(response.body()!!), 3)
                        intent.putExtra("result", Constants.POST_SENDING_SUCCESS)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        if (!isNetworkConnected()) {

                            callback?.onResponse(null, 3)

                            val manager = App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                                override fun onAvailable(network: Network?) {
                                    super.onAvailable(network)
                                    publishPost(callback, request)
                                }
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                manager.registerDefaultNetworkCallback(networkCallback)
                            } else {
                                val mRequest = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
                                manager.registerNetworkCallback(mRequest, networkCallback)
                            }
                        }
                    }
                })
    }

    fun search(callback: SearchResultRequester, activityCallback: Searcher, keyWord: String, peopleCount: Int, pagesCount: Int) {
        client.getDataCallback(AUTH_STRING, "{type:'search',parameters:{term:'" + keyWord + "',peoplecount:'" + peopleCount + "',pagecount:'" + pagesCount + "', authorsimageloadmode:1, city:'" + Constants.Initialization.CITY + "' }}")
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        try {
                            callback.onResultReceived(activityCallback, JsonWrapper(response.body()!!).searchResults, keyWord)
                        } catch (e: Exception) {

                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {}
                })
    }

    fun searchCommunities(callback: SearchResultRequester, activityCallback: Searcher, keyWord: String, peopleCount: Int, pagesCount: Int) {
        client.getDataCallback(AUTH_STRING, "{type:'search',parameters:{obj:'community', term:'$keyWord', user:'$USER_ID',peoplecount:'$peopleCount',pagecount:'$pagesCount', authorsimageloadmode:1 }}")
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        try {
                            callback.onResultReceived(activityCallback, JsonWrapper(response.body()!!).searchResults, keyWord)
                        } catch (e: Exception) {

                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {}
                })
    }

    fun likePost(postId: String, postUrl: String, postType: String, likeStatus: Int, callback: PostDataSource.LikePostCallback) {
        val request = RequestFactory.formLikePostRequest(postId, postUrl, postType, likeStatus)
        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onPostLiked()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onServerNotAvailable()
                    }
                })
    }

    fun request(request: String, repositoryCallback: WebRequestListener, requestType: Int) {
        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            val stringResponse = response.body()!!
                            mLog.debug(LOG_RESPONSE, stringResponse)
                            repositoryCallback.onResponse(JsonWrapper(stringResponse), requestType)
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        repositoryCallback.onFailure(null, 0)
                    }
                })
    }

    fun hideSurvey(surveyId: String, callback: SurveyDataSource.HideSurveyCallback) {
        val request = RequestFactory.formHideQuizRequest(surveyId)
        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onSurveyHidden()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onServerNotAvailable()
                    }
                })
    }

    fun getSurvey(surveyId: String, callback: SurveyDataSource.LoadSurveyCallback) {
        val request = RequestFactory.formQuizRequest(surveyId)
        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val json = response.body()!!
                        val quiz = JsonWrapper.getQuiz(json)
                        if (quiz == null) {
                            callback.onDataNotAvailable()
                        } else {
                            callback.onSurveyLoaded(quiz)
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onDataNotAvailable()
                    }
                })
    }

    fun getNewsInjectionSurvey(callback: SurveyDataSource.LoadNewsInjectionSurveyCallback) {
        val request = RequestFactory.formNewsInjectionQuizRequest()
        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {

                        if (response.isSuccessful) {
                            mLog.debug(LOG_RESPONSE, "size: " + response.body()!!.toString().length + ", response: " + response.body()!!.toString())

                            val quizCover = JsonWrapper.getQuizCover(response.body())

                            if (quizCover == null) {
                                callback.onDataNotAvailable()
                            } else {
                                callback.onNewsInjectionSurveyLoaded(quizCover)
                            }
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onDataNotAvailable()
                    }
                })
    }

    fun uploadAnswers(surveyId: String, answers: List<Answer>, callback: SurveyDataSource.UploadAnswersCallback) {
        val request = RequestFactory.formUploadResults(surveyId, answers)
        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onAnswersUploaded()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onDataNotAvailable()
                    }
                })
    }

    fun uploadAnswer(surveyId: String, answer: Answer, callback: SurveyDataSource.UploadAnswerCallback) {
        val request = RequestFactory.formUploadSurveyAnswer(surveyId, answer)

        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onAnswerUploaded()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onDataNotAvailable()
                    }
                })
    }

    // Favorite

    // person
    fun getFavouritePersons(callback: FavoritePeopleDataSource.LoadFavouritePersonsCallback) {
        client.getDataCallback(AUTH_STRING, RequestFactory.favoriteProfiles())
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val persons = JsonWrapper(response.body()!!).favouritePersons
                        callback.onFavouritePersonsLoaded(persons)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onDataNotAvailable()
                    }
                })
    }

    fun addFavoritePerson(callback: JsonGetter, friendId: String) {
        val request = RequestFactory.addUserToFavorite(friendId)
        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onResponse(JsonWrapper(response.body()!!), 1)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onFailure(null, 1)
                    }
                })
    }

    fun addFavoritePerson(id: String, callback: FavoritePeopleDataSource.AddPersonCallback) {
        val request = RequestFactory.addUserToFavorite(id)
        client.getDataCallback(AUTH_STRING, request)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onPersonAdded()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onDataNotAvailable()
                    }
                })
    }

    fun deleteFavoritePerson(callback: JsonGetter, friendId: String) {
        client.getDataCallback(AUTH_STRING, RequestFactory.removeUserFromFavorite(friendId))
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onResponse(JsonWrapper(response.body()!!), 0)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onFailure(null, 0)
                    }
                })
    }

    fun removeFavoritePerson(id: String, callback: FavoritePeopleDataSource.RemovePersonCallback) {
        client.getDataCallback(AUTH_STRING, RequestFactory.removeUserFromFavorite(id))
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        callback.onPersonRemoved()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback.onDataNotAvailable()
                    }
                })
    }

    fun getUserSettings(callback: NotificationsDataSource.LoadSettingsCallback) {
        val request = RequestFactory.formGetNotificationsSettings()

        client.getDataCallback(AUTH_STRING, request).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val json = response.body()!!
                val settings = JsonWrapper.getNotificationSettings(json)
                callback.onSettingsLoaded(settings)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                callback.onServerNotAvailable()
            }
        })
    }

    fun uploadUserSettings(settings: NotificationsSettings, callback: NotificationsDataSource.UploadSettingsCallback) {
        val request = RequestFactory.formSaveNotificationsSettings(settings)

        client.getDataCallback(AUTH_STRING, request).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                callback.onSettingsUploaded()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                callback.onServerNotAvailable()
            }
        })
    }

    interface WebRequestListener {
        fun onResponse(jsonWrapper: JsonWrapper, responseType: Int) {}

        fun onFailure(jsonWrapper: JsonWrapper?, responseType: Int) {}
    }

    private fun isNetworkConnected(): Boolean {
        val manager = App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.getNetworkCapabilities(manager.activeNetwork)?.apply {
                return hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
        } else {
            return manager.getNetworkInfo(0).state == NetworkInfo.State.CONNECTED || manager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
        }
    }
}
