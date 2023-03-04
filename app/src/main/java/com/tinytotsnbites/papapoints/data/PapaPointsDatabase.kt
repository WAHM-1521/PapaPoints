package com.tinytotsnbites.papapoints.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequestBuilder
import com.tinytotsnbites.papapoints.utilities.Converters
import com.tinytotsnbites.papapoints.utilities.DATABASE_NAME
import com.tinytotsnbites.papapoints.utilities.LogHelper
import com.tinytotsnbites.papapoints.workers.PapaPointsDatabaseWorker

@Database(entities = [Child::class, Task::class, Rating::class], version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun childDao(): ChildDao
    abstract fun taskDao(): TaskDao
    abstract fun ratingDao(): RatingDao

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                LogHelper(this).d("instance is null - create database")
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database.
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            LogHelper(this).d("Creating database")
                            val request = OneTimeWorkRequestBuilder<PapaPointsDatabaseWorker>()
                                //.setInputData(workDataOf(KEY_FILENAME to PLANT_DATA_FILENAME))
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .addMigrations(MIGRATION_3_4)
                .build()
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Child ADD COLUMN child_image_uri TEXT")
            }
        }
    }
}