package com.example.androidproject03.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.androidproject03.MainActivity
import com.example.androidproject03.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG = "FCMService"

class FCMService: FirebaseMessagingService() {

    /* When receive a new token */
    override fun onNewToken(token: String) {

        /* Token - to identify the device */
        Log.d(TAG, "FCM token: $token")
    }

    /* Listening message */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Payload: " + remoteMessage.data)

            /* Receive a message with the 'product' key, if it exists */
            remoteMessage.data.get("product")?.let {
                sendProductNotification(it)
            }

        }
    }

    private fun sendProductNotification(productInfo: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("product", productInfo)

        /* Function that will show the message to the user */
        sendNotification(intent)
    }

    // Show message
    private fun sendNotification(intent: Intent) {
        val pendingIntent = PendingIntent.getActivity(this,
            0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = "1"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_cloud_queue_24)
            .setContentTitle("Sales Message")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Sales provider",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }


}