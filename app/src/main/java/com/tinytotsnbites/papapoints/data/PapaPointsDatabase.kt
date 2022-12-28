package com.tinytotsnbites.papapoints.data

import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import com.tinytotsnbites.papapoints.utilities.Converters
import com.tinytotsnbites.papapoints.utilities.DATABASE_NAME
import com.tinytotsnbites.papapoints.workers.PapaPointsDatabaseWorker

@Database(entities = [Child::class, Task::class, Rating::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun childDao(): ChildDao
    abstract fun taskDao(): TaskDao
    abstract fun ratingDao(): RatingDao

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            Log.d("tushar","getInstance")
            return instance ?: synchronized(this) {
                Log.d("tushar", "instance is null - create database")
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database.
        private fun buildDatabase(context: Context): AppDatabase {
            Log.d("tushar", "starting buildDatabase ")
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("tushar", "creating database")
                            val request = OneTimeWorkRequestBuilder<PapaPointsDatabaseWorker>()
                                //.setInputData(workDataOf(KEY_FILENAME to PLANT_DATA_FILENAME))
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()
        }

    }
}