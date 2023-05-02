package com.example.petzzu

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

const val notificationId=1
const val channelId="petzzu_notification"
const val titleExtra="titleExtra"
const val messageExtra="messageExtra"

class Notifications: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notifications=NotificationCompat.Builder(context!!, channelId)
            .setSmallIcon(R.drawable.newlogo)
            .setContentTitle(intent?.getStringExtra(titleExtra))
            .setContentText(intent?.getStringExtra(messageExtra))
            .build()

        val manager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId,notifications)
    }
}