package com.tinytotsnbites.papapoints.utilities

import android.util.Log
import com.tinytotsnbites.papapoints.BuildConfig

class LogHelper(val context: Any, private val charLimit: Int = 20) {
    companion object {
        private const val TAG = "PapaPoints"
    }

    private fun getTag(context: Any): String {
        val fullClassName = context::class.java.simpleName
        return if (fullClassName.length > charLimit) {
            fullClassName.substring(0, charLimit)
        } else {
            fullClassName
        }
    }

    fun d(message: String) {
        if(BuildConfig.DEBUG)
            Log.d(getTag(context), message)
    }

    fun e(message: String) {
        Log.e(getTag(context), message)
    }

    fun i(message: String) {
        Log.i(getTag(context), message)
    }

    fun v(message: String) {
        if(BuildConfig.DEBUG)
            Log.v(getTag(context), message)
    }

    fun w(message: String) {
        Log.w(getTag(context), message)
    }
}
