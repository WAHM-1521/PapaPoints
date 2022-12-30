package com.tinytotsnbites.papapoints.utilities

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