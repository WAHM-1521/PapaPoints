package com.tinytotsnbites.papapoints.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tinytotsnbites.papapoints.R
import com.tinytotsnbites.papapoints.data.Task
import com.tinytotsnbites.papapoints.data.AppDatabase
import com.tinytotsnbites.papapoints.utilities.LogHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PapaPointsDatabaseWorker(
    context: Context,
    workerParams : WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try {
            val database  = AppDatabase.getInstance(applicationContext)
            val taskList: List<Task> = applicationContext.resources.getStringArray(R.array.tasks).mapIndexed {
                    _, task -> Task(0, task, true, false)
            }
            database.taskDao().insertAll(taskList)
            Result.success()
        } catch (ex : Exception) {
            LogHelper(this).e("Error seeding database $ex")
            Result.failure()
        }
    }
}