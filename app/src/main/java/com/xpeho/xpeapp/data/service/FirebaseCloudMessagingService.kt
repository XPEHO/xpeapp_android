package com.xpeho.xpeapp.data.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.xpeho.xpeapp.MainActivity
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.utils.CrashlyticsUtils

class FirebaseCloudMessagingService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "XpeApp_Notifications"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CrashlyticsUtils.logEvent("FCM: Nouveau token reçu")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        CrashlyticsUtils.logEvent("FCM: Message reçu")

        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "XpeApp"
        val message = remoteMessage.notification?.body ?: remoteMessage.data["message"] ?: ""

        if (message.isNotEmpty()) {
            showNotification(title, message)
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)

        // Intent pour ouvrir l'app au clic
        val intent = Intent(this, MainActivity::class.java).apply {
            action = "OPEN_XPEAPP"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construire la notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.app_icon_cropped)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true) // Fermer la notification au clic
            .setContentIntent(pendingIntent) // Ouvrir l'app au clic
            .build()

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)
    }
}