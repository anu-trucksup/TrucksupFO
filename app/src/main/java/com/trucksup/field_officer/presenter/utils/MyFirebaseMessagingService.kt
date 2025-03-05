package com.trucksup.field_officer.presenter.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.view.activity.other.SplashScreenActivity
import java.util.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {
    var app: CommonApplication? = null
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        app = application as CommonApplication

        val intent = Intent("in.co.ksquaretech.propease")
        sendBroadcast(intent)

        if (remoteMessage.data.size > 0) {
            Log.d("TAG", "Message data payload: " + remoteMessage.data)
            if (
                    //Check if data needs to be processed by long running job
            true) {
                    // For long-running tasks (10 seconds or more) use WorkManager.
            } else {
                    // Handle message within 10 seconds
                handleNow()
            }
              sendNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"),remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(
                "TAG", "Message Notification Body: " + remoteMessage.notification!!
                    .body
            )
            sendNotification(
                remoteMessage.notification!!.body, remoteMessage.notification!!
                    .title, remoteMessage.data
            )
        }
    }

    override fun onNewToken(token: String) {
        Log.e("TAG", "Refreshed token: $token")
        app = application as CommonApplication

        Log.e("UserDeviceToken", token)
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]
/**
     * Schedule async work using WorkManager.


*
     * Handle time allotted to BroadcastReceivers.*/


    private fun handleNow() {
    }

/**
     * Persist token to third-party servers.
     *
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.*/


    private fun sendRegistrationToServer(token: String) {
        // TODO: Implement this method to send token to your app server.
    }

/**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     * @param remoteData*/


    private fun sendNotification(
        messageBody: String?,
        fcm: String?,
        remoteData: Map<String, String?>?
    ) {
        var pendingIntent: PendingIntent? = null
        var intent: Intent? = null
        val random = Random()
        val m = random.nextInt(9999 - 1000)
        if (remoteData != null && remoteData["screen_type"] != null && !remoteData["screen_type"]!!
                .isEmpty()
        ) {

                intent = Intent(applicationContext, SplashScreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra("order_id", 123456)
                pendingIntent = PendingIntent.getActivity(
                    this, 0, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

        val channelId = "com.crew.android.sendnotification"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(fcm)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(m /*ID of notification*/,notificationBuilder.build())
        }

}
}
