package ru.alfabank.alfamir

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.multidex.MultiDex
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import com.google.firebase.iid.FirebaseInstanceId
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import net.danlew.android.joda.JodaTimeAndroid
import ru.alfabank.alfamir.Constants.Companion.PLATFORM
import ru.alfabank.alfamir.Constants.Companion.PREF_UNIQUE_ID
import ru.alfabank.alfamir.Constants.Initialization.APP_VERSION
import ru.alfabank.alfamir.Constants.Initialization.PHONE_MODEL
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_HEIGHT_ACTIVE_SIZE
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_HEIGHT_PHYSICAL
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_DP
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_PHYSICAL
import ru.alfabank.alfamir.Constants.Initialization.TIMEZONE
import ru.alfabank.alfamir.Constants.Initialization.UNIQUE_APP_ID
import ru.alfabank.alfamir.Constants.Initialization.USER_ID
import ru.alfabank.alfamir.data.source.broadcast_receiver.networkBR.NetworkBR
import ru.alfabank.alfamir.data.source.remote.api.WebService
import ru.alfabank.alfamir.data.source.repositories.old_trash.NotificationsRepositoryOld
import ru.alfabank.alfamir.data.source.repositories.old_trash.PostPhotoRepository
import ru.alfabank.alfamir.data.source.repositories.old_trash.ProfilePhotoRepository
import ru.alfabank.alfamir.data.source.repositories.old_trash.SubscribeRepository
import ru.alfabank.alfamir.di.DaggerAppComponent
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerRepository
import ru.alfabank.alfamir.utility.logging.remote.AppLifecycleTracker
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper
import java.util.*
import javax.inject.Inject

class App : DaggerApplication(), LoggerContract.Client.AppStartedTracker {
    @Inject
    lateinit var logger: LoggerContract.Provider
    @Inject
    lateinit var dProvider: WebService
    @Inject
    lateinit var appLifecycleTracker: AppLifecycleTracker
    @Inject
    lateinit var mMessengerRepository: MessengerRepository
    @Inject
    lateinit var networkBR: NetworkBR

    var notificationsRepositoryOld: NotificationsRepositoryOld? = null
        private set
    var postPhotoRepository: PostPhotoRepository? = null
        private set
    var profilePhotoRepository: ProfilePhotoRepository? = null
        private set
    private var userAgent: String? = null
    var token: String? = null
        private set

    val subscribeRepositoryNew: SubscribeRepository?
        get() = subscribeRepository

    private val appMap = mutableMapOf<String, Any>()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        instance = this
        setThirdPartyLibraries()
        setScreenSize()
        setTimeZone()
        setConstants()
        setRepositories()
        setAndroidRIchText()
        setNotificationChannels()
        setBroadcastReceivers()
        clearAppMap()
    }
    fun clearAppMap() {
        for (key in appMap.keys)
            appMap.remove(key)
    }
    fun getAppValue(key: String): Any? {
        return appMap[key]
    }
    fun setAppValue(key: String, value: Any) {
        appMap[key] = value
    }

    // initialization
    private fun setScreenSize() {
        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()

        Constants.Initialization.DENSITY = resources.displayMetrics.density

        display.getMetrics(metrics)
        SCREEN_WIDTH_PHYSICAL = metrics.widthPixels
        SCREEN_HEIGHT_ACTIVE_SIZE = metrics.heightPixels

        val widthDpFloat = metrics.widthPixels / metrics.density
        var widthDpInt = widthDpFloat.toInt()
        if (widthDpFloat % 1 != 0f) widthDpInt += 1 // for some reason, one should round always up
        SCREEN_WIDTH_DP = widthDpInt

        display.getRealMetrics(metrics)
        SCREEN_HEIGHT_PHYSICAL = metrics.heightPixels
    }

    private fun setUniqueAppId() {
        val sharedPrefs = getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE)
        var uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null)
        if (uniqueID == null) {
            uniqueID = UUID.randomUUID().toString()
            val editor = sharedPrefs.edit()
            editor.putString(PREF_UNIQUE_ID, uniqueID)
            editor.apply()
        }
        UNIQUE_APP_ID = (uniqueID)
    }

    private fun setNotificationChannels() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val profileChannel = NotificationChannel("notifications", "Notifications", NotificationManager.IMPORTANCE_HIGH)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(profileChannel)
        }
    }

    private fun setBroadcastReceivers() {
        val br = networkBR as BroadcastReceiver?
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(br, filter)
    }

    private fun setRepositories() {
        postPhotoRepository = PostPhotoRepository(this)
        subscribeRepository = SubscribeRepository(dProvider)
        profilePhotoRepository = ProfilePhotoRepository(this, resources)

        val shPrefEditor = getSharedPreferences("air_notifications", Context.MODE_PRIVATE).edit()
        val shPref = getSharedPreferences("air_notifications", Context.MODE_PRIVATE)
        notificationsRepositoryOld = NotificationsRepositoryOld(dProvider, shPrefEditor, shPref)

        PictureClipper(resources)
    }

    private fun setUserId() {
        val prefsId = getSharedPreferences("air_id", Context.MODE_PRIVATE)
        val id = prefsId.getString("id", null)
        val city = prefsId.getString("city", null)
        val marsId = prefsId.getString("marsId", null)
        val login = prefsId.getString("login", null)

        if (id != null && city != null && marsId != null && login != null) {
            USER_ID = (id.toLowerCase())
            Constants.Initialization.USER_LOGIN = login.toLowerCase()
            Constants.Initialization.CITY = city
            logStartSession()
        }
    }

    private fun setTimeZone() {
        val mCalendar = GregorianCalendar()
        val mTimeZone = mCalendar.timeZone
        val mGMTOffset = mTimeZone.rawOffset
        TIMEZONE = mGMTOffset / (1000 * 60 * 60)
    }

    private fun setAndroidRIchText() {
        val myVersion = Integer.parseInt(Build.VERSION.RELEASE.substring(0, 1))
        if (myVersion < 5) {
            Constants.Initialization.ANDROID_RICH_TEXT = 3
        } else {
            Constants.Initialization.ANDROID_RICH_TEXT = 2
        }
    }

    private fun setThirdPartyLibraries() {
        JodaTimeAndroid.init(this)
        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build())

        userAgent = Util.getUserAgent(this, "ExoPlayerDemo")
    }

    private fun setConstants() {
        token = FirebaseInstanceId.getInstance().token
        Constants.Initialization.SESSION_ID = UUID.randomUUID().toString()
        PHONE_MODEL = (Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT].name)

        setUniqueAppId()
        setUserId()
    }

    fun useExtensionRenderers(): Boolean {
        return false
    }

    override fun logStartSession() {
        logger.startSession(UNIQUE_APP_ID, APP_VERSION, PHONE_MODEL, "", USER_ID, PLATFORM, "" + TIMEZONE)
    }

    // trash
    fun buildDataSourceFactory(bandwidthMeter: DefaultBandwidthMeter): DataSource.Factory {
        return DefaultDataSourceFactory(this, bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter))
    }

    fun buildHttpDataSourceFactory(bandwidthMeter: DefaultBandwidthMeter): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(userAgent, bandwidthMeter)
    }

    companion object {
        private lateinit var context: Context

        lateinit var subscribeRepository: SubscribeRepository
            private set

        private lateinit var instance: App

        fun getAppInstance() = instance

        fun getAppContext(): Context{
            return context
        }
    }

}
