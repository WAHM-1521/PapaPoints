package com.tinytotsnbites.papapoints.workers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tinytotsnbites.papapoints.R
import com.tinytotsnbites.papapoints.data.Task
import com.tinytotsnbites.papapoints.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlinx.coroutines.withContext

class PapaPointsDatabaseWorker(
    context: Context,
    workerParams : WorkerParameters
) : CoroutineWorker(context, workerParams) {
    @SuppressLint("LongLogTag")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try {
            val database  = AppDatabase.getInstance(applicationContext)
            val taskList: List<Task> = applicationContext.resources.getStringArray(R.array.tasks).mapIndexed { index, task ->
                Task((index + 1).toLong(), task)
            }
            database.taskDao().insertAll(taskList)
            Result.success()
        } catch (ex : Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "PapaPoints"
    }
}