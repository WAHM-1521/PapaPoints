package com.tinytotsnbites.papapoints.utilities

import android.app.Activity
import java.util.*

fun getCalendarInDateFormat(): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val currentDate = calendar.time
    return currentDate
}

fun getCalendarDateForMidnightTime(calendar: Calendar): Date {
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val currentDate = calendar.time
    return currentDate
}

fun onActivitySetTheme(activity: Activity, theme: Int) {
    activity.setTheme(theme)
}