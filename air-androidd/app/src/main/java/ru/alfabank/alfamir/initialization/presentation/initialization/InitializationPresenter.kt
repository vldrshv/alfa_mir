package ru.alfabank.alfamir.initialization.presentation.initialization

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.common.base.Strings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import ru.alfabank.alfamir.BuildConfig
import ru.alfabank.alfamir.Constants
import ru.alfabank.alfamir.Constants.Companion.BUILD_TYPE_PROD
import ru.alfabank.alfamir.Constants.Companion.LOG_ITEMS_COUNT
import ru.alfabank.alfamir.Constants.Initialization.ALFA_TV_ENABLED
import ru.alfabank.alfamir.Constants.Initialization.AUTH_STRING
import ru.alfabank.alfamir.Constants.Initialization.CITY
import ru.alfabank.alfamir.Constants.Initialization.IS_NEW_YEAR_COME
import ru.alfabank.alfamir.Constants.Initialization.MESSENGER_ENABLED
import ru.alfabank.alfamir.Constants.Initialization.MESSENGER_LP_ENABLED
import ru.alfabank.alfamir.Constants.Initialization.MESSENGER_LP_TIMEOUT
import ru.alfabank.alfamir.Constants.Initialization.PHOTOS_DISABLED
import ru.alfabank.alfamir.Constants.Initialization.POSTER_LP_ENABLED
import ru.alfabank.alfamir.Constants.Initialization.USER_ID
import ru.alfabank.alfamir.Constants.Initialization.USER_LOGIN
import ru.alfabank.alfamir.data.source.remote.api.ApiClientProvider
import ru.alfabank.alfamir.data.source.remote.api.WebService
import ru.alfabank.alfamir.di.qualifiers.shared_pref.UserIdData
import ru.alfabank.alfamir.initialization.domain.mapper.ShortUserInfoMapper
import ru.alfabank.alfamir.initialization.domain.utilities.InitializationController
import ru.alfabank.alfamir.initialization.presentation.initialization.contract.InitializationContract
import ru.alfabank.alfamir.utility.lifecycle.AppLIfeCycleListener
import ru.alfabank.alfamir.utility.lifecycle.AppSettings
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory
//import ru.alfabank.oavdo.mars.Mars
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("LogNotTimber")
@Singleton
class InitializationPresenter @Inject
internal constructor(private val mInitializationController: InitializationController,
                     private val mWebService: WebService,
                     private val mShortUserInfoMapper: ShortUserInfoMapper,
                     private val mApiClientProvider: ApiClientProvider,
                     @param:UserIdData private val mSharedPreferences: SharedPreferences,
                     private val mLifeCycleListener: AppLIfeCycleListener) : InitializationContract.Presenter {
    private var mView: InitializationContract.View? = null
    private var mActivity: Activity? = null
    private var registered = true

    init {
        mWebService.setPresenter(this)
    }

    private val isUserIdDataSaved: Boolean
        get() {
            val userId = mSharedPreferences.getString("id", "")
            val userLogin = mSharedPreferences.getString("login", "")
            val city = mSharedPreferences.getString("city", "") ?: ""
            return if (Strings.isNullOrEmpty(userId)) {
                false
            } else {
                USER_ID = userId!!.toLowerCase(Locale.ENGLISH)
                USER_LOGIN = userLogin!!.toLowerCase(Locale.ENGLISH)
                CITY = (city)
                true
            }
        }

    override fun takeView(view: InitializationContract.View) {
        mView = view
        mActivity = view as Activity
        mWebService.setPresenter(this)
        makeSapRequest()
        initializeConstants()
        initializeApiClient()
    }

    override fun makeSapRequest() {
        if( !registered){
            Log.d("Sap init stopped", "Registration request already sent")
            return
        }

        registered = false
        val scope = "urn:oauth:alfamir"
        val version = BuildConfig.VERSION_NAME
        val build = BuildConfig.VERSION_CODE.toString() + ""
        val fakeBundleID = "ru.alfabank.alfamir"
        val jsonString = "{\"type\":\"atRequest\",\"scope\":\"$scope\",\"version\":\"$version\",\"build\":\"$build\",\"bundleID\":\"$fakeBundleID\"}"

        val browserIntent = Intent("ru.alfabank.tokenagent.ACTION_ALFA_MESSAGE")
        browserIntent.putExtra("PARAM_EXTRA_MESSAGE", jsonString)

        try {
            mActivity!!.startActivity(browserIntent)
        } catch (e: Exception) {
            Log.e("Sap init failed", e.message)
        }

    }

    @SuppressLint("CheckResult")
    override fun onSapResponse(response: JSONObject) {
        AUTH_STRING = String.format("%s %s", response.getString("token_type"), response.getString("access_token"))

        mWebService.register()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            registered = true
                            getSettings()
                        },
                        { e ->
                            Log.e("Initialization", "Settings request error: " + e.message)
                        })
    }

    @Suppress("ConstantConditionIf")
    private fun initializeApiClient() {

        if (Constants.USE_HOMO) {
            if (BUILD_TYPE_PROD) { // false
//                try {
//                    mars = Mars(mActivity!!, object : BroadcastReceiver() {
//                        @SuppressLint("CheckResult")
//                        override fun onReceive(context: Context, intent: Intent) {
//                            mApiClientProvider.initialize(mars)
//                            mWebService.setApiClient(mApiClientProvider.apiClient)
//                            if (!isUserIdDataSaved) {
//                                val shortId = mars!!.userName
//                                getUserIdData(shortId)
//                            } else {
//                                getSettings()
//                            }
//                        }
//                    })

//                } catch (e: Exception) {
//                    Log.e("Initialization", "error creating api client: " + e.message)
//                }

            } else {
                mApiClientProvider.initializeHomo()
                mWebService.setApiClient(mApiClientProvider.apiClient)
            }
        }
    }

    @SuppressLint("CheckResult", "LogNotTimber")
    private fun getSettings() {
        val sPref = mActivity!!.getSharedPreferences("appSettings", Context.MODE_PRIVATE)

        mWebService.getSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<AppSettings> { v -> JsonWrapper.getSettings(v) }
                .subscribe({ settings ->
                    IS_NEW_YEAR_COME = (settings.isNewYearCome())
                    ALFA_TV_ENABLED = (settings.isAlfaTvEnabled())
                    MESSENGER_ENABLED = (settings.isMessengerEnabled())
                    MESSENGER_LP_ENABLED = (settings.isMessengerLpEnabled())
                    MESSENGER_LP_TIMEOUT = (settings.getMessengerLpTimeout().toLong())
                    POSTER_LP_ENABLED = (settings.isPosterLpEnabled())
                    LOG_ITEMS_COUNT = (settings.getLogItemsCount())

                    onInitialized()

                    sPref.edit()
                            .putBoolean("AlfaTvOnAir", ALFA_TV_ENABLED)
                            .putBoolean("messengerEnabled", MESSENGER_ENABLED)
                            .putBoolean("messengerLpEnabled", MESSENGER_LP_ENABLED)
                            .apply()
                }, { e ->
                    Log.e("Initialization", "Settings request error: " + e.message)
                    ALFA_TV_ENABLED = (sPref.getBoolean("AlfaTvOnAir", true))
                    MESSENGER_ENABLED = (sPref.getBoolean("messengerEnabled", false))
                    MESSENGER_LP_ENABLED = (sPref.getBoolean("messengerLpEnabled", false))
                    PHOTOS_DISABLED = (sPref.getBoolean("photosDisabled", false))
                    onInitialized()
                })
    }

    @SuppressLint("CheckResult", "LogNotTimber")
    private fun getUserIdData(shortId: String) {
        val request = RequestFactory.formGetFullUserId(shortId)
        mWebService.requestX(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    saveUserIdData(response)
                    onInitialized()
                }, { throwable -> Log.e("Initialization", throwable.message) })
    }

    @SuppressLint("DefaultLocale")
    @Throws(Exception::class)
    private fun saveUserIdData(json: String) {
        val shortUserInfoRaw = JsonWrapper.getShortUserInfoRaw(json)
        val shortUserInfo = mShortUserInfoMapper.apply(shortUserInfoRaw!!)
        val userId = shortUserInfo.id
        val city = shortUserInfo.city
        val userLogin = userId.substring(0, userId.indexOf(";"))

        USER_ID = (userId.toLowerCase())
        USER_LOGIN = (userLogin.toLowerCase())
        CITY = (city)

        val mEditor = mSharedPreferences.edit()
        mEditor.putString("id", USER_ID)
        mEditor.putString("city", CITY)
        mEditor.putString("login", USER_LOGIN)
        mEditor.apply()
    }

    private fun initializeConstants() {
        if (!BUILD_TYPE_PROD) {
            initializeUser(MIR)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun initializeUser(user: Int) {
        when (user) {
            ME -> {
                USER_ID = ("MOSCOW\\U_M11CM;67435".toLowerCase())
                USER_LOGIN = ("MOSCOW\\U_M11CM".toLowerCase())
                CITY = ("Москва")
            }
            YULYA -> {
                USER_ID = ("MOSCOW\\U_P0116;14518".toLowerCase())
                USER_LOGIN = ("MOSCOW\\U_P0116".toLowerCase())
                CITY = ("Москва")
            }
            ANDREW -> {
                USER_ID = ("MOSCOW\\U_M0SU5;34390".toLowerCase())
                USER_LOGIN = ("MOSCOW\\U_M0SU5".toLowerCase())
                CITY = ("Москва")
            }
            OLEG -> {
                USER_ID = ("MOSCOW\\U_M0YFV;56397".toLowerCase())
                USER_LOGIN = ("MOSCOW\\U_M0YFV".toLowerCase())
                CITY = ("Москва")
            }
            MIR -> {
                USER_ID = ("moscow\\amir_usr;36100".toLowerCase())
                USER_LOGIN = ("moscow\\amir_usr".toLowerCase())
                CITY = ("Москва")
            }
        }
    }

    private fun onInitialized() {
        mInitializationController.setIsInitialized(true)

        ProcessLifecycleOwner.get().lifecycle.addObserver(mLifeCycleListener)
//        mActivity = null
        view?.close()
    }

    override fun dropView() {
        mView = null
    }

    override fun getView(): InitializationContract.View? {
        return mView
    }

    fun getContext(): Context{
        return (mView as InitializationActivity).applicationContext
    }

    companion object {

        private const val ME = 0
        private const val YULYA = 1
        private const val ANDREW = 2
        private const val OLEG = 7
        private const val MIR = 8
    }
}
