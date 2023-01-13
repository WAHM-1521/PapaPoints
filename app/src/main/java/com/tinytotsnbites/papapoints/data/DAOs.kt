package com.tinytotsnbites.papapoints.data

import androidx.room.*
import java.util.*

@Dao
interface ChildDao {
    @Insert
    fun insert(child: Child)

    @Update
    fun update(child: Child)

    @Delete
    fun delete(child: Child)

    @Query("SELECT * FROM child")
    fun getAll(): List<Child>

    @Query("SELECT * FROM child WHERE id = :id")
    fun getById(id: Long): Child
}

@Dao
interface TaskDao {
    @Insert
    fun insert(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks : List<Task>)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getById(id: Long): Task

    //@Query("SELECT task.*, rating.rating FROM task LEFT JOIN rating ON task.id = rating.task_id WHERE rating.date = :date OR rating.date IS NULL")
    @Query("SELECT task.*, rating.rating FROM task LEFT JOIN rating ON task.id = rating.task_id AND rating.date = :date WHERE (rating.date IS NULL OR rating.date = :date) AND task.enabled = 1")
    fun getTasksWithRatingForDate(date: Date): List<TaskWithRating>

    @Query("UPDATE task SET enabled = 0 WHERE id = :taskId")
    fun disableTask(taskId: Long)
}

@Dao
interface RatingDao {
    @Insert
    fun insert(rating: Rating)

    @Update
    fun update(rating: Rating)

    @Delete
    fun delete(rating: Rating)

    @Query("SELECT * FROM rating")
    fun getAll(): List<Rating>

    @Query("SELECT * FROM rating WHERE id = :id")
    fun getById(id: Long): Rating

    @Query("SELECT * FROM rating WHERE child_id = :childId")
    fun getByChildId(childId: Long): List<Rating>

    @Query("SELECT * FROM rating WHERE task_id = :taskId AND date = :date")
    fun getByTaskId(taskId: Long, date: Date): List<Rating>

    @Query("SELECT SUM(rating) FROM rating")
    fun getTotalRating(): Int

    @Query("SELECT SUM(rating) FROM rating WHERE date = :today")
    fun getTotalRatingToday(today: Date): Int

}