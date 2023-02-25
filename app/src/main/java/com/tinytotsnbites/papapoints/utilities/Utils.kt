package com.tinytotsnbites.papapoints.utilities

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tinytotsnbites.papapoints.NotificationReceiver
import java.util.*

fun getCalendarInDateFormat(): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}

fun getCalendarDateForMidnightTime(calendar: Calendar): Date {
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}

fun onActivitySetTheme(activity: Activity, theme: Int) {
    activity.setTheme(theme)
}

fun scheduleNotification(context: Context) {
    val intent = Intent(context, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context,0,intent, PendingIntent.FLAG_IMMUTABLE)

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 21)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
    alarmManager.cancel(pendingIntent)
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )
    context.getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putBoolean("notificationScheduled",true).apply()
    LogHelper(context).i("scheduleNotification for ${calendar.time}")
}

fun cancelNotification(context: Context) {
    val intent = Intent(context, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    if(pendingIntent != null) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
        context.getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putBoolean("notificationScheduled",false).apply()
        LogHelper(context).i("cancelNotification")
    } else {
        LogHelper(context).i("Alarm has not been set, so there is nothing to cancel")
    }
}
