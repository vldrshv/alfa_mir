package ru.alfabank.alfamir.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.alfabank.alfamir.Constants.Initialization.ALFA_TV_ENABLED
import ru.alfabank.alfamir.Constants.Initialization.PHOTOS_DISABLED
import ru.alfabank.alfamir.Constants.Messenger.CHAT_ID
import ru.alfabank.alfamir.Constants.UI.FRAGMENT_TO_BACKSTACK
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.main.main_activity.MainActivity
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper

class FBService : FirebaseMessagingService() {

    private lateinit var sPref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sPref = getSharedPreferences("appSettings", Context.MODE_PRIVATE)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val bundle = remoteMessage!!.data

        remoteMessage.notification?.also { notification ->
            val type = bundle["type"]
            val data = bundle["getData"]
            val title = notification.title
            val message = notification.body
            val chatMessage = JsonWrapper.getChatMessage(data)
            val chatId = chatMessage?.chatId ?: ""

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(CHAT_ID, chatId)
            intent.putExtra(FRAGMENT_TO_BACKSTACK, false)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            val mBuilder = NotificationCompat.Builder(this, "notifications")
                    .setSmallIcon(R.drawable.ic_app_small)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            mBuilder.build()
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(1, mBuilder.build())
            return
        }

        val photosDisabled = bundle["params_IMAGES_DISABLED"]
        PHOTOS_DISABLED = if (photosDisabled.isNullOrEmpty()) PHOTOS_DISABLED else photosDisabled.toBoolean()
        sPref.edit().putBoolean("photosDisabled", PHOTOS_DISABLED).apply()

        val alfaTvEnabled = bundle["params_AlfaTvOnAir"]
        ALFA_TV_ENABLED = if (alfaTvEnabled.isNullOrEmpty()) ALFA_TV_ENABLED else alfaTvEnabled.toBoolean()
        sPref.edit().putBoolean("AlfaTvOnAir", ALFA_TV_ENABLED).apply()
    }
}
