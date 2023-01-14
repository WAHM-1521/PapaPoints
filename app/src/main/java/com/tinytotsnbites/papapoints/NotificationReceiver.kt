package com.tinytotsnbites.papapoints

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.*

class NotificationReceiver : BroadcastReceiver() {

    private val channelId = UUID.randomUUID().toString()
    private val notificationId = UUID.randomUUID().hashCode()

    override fun onReceive(context: Context, intent: Intent) {
        val pointsAndTaskIntent = Intent(context, PointsAndTaskActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, pointsAndTaskIntent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, context.getString(R.string.notification_channel_name), NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }
}