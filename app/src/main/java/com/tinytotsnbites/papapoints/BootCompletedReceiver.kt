package com.tinytotsnbites.papapoints

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import com.tinytotsnbites.papapoints.utilities.LogHelper
import com.tinytotsnbites.papapoints.utilities.scheduleNotification

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        LogHelper(this).d("BootCompletedReceiver for PapaPoints")
        if(intent.action == Intent.ACTION_BOOT_COMPLETED &&
            PreferenceManager.getDefaultSharedPreferences(context).getBoolean("notification_preference", true)
            && context.getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("gender","") !== "") {
            scheduleNotification(context)
        }
    }
}