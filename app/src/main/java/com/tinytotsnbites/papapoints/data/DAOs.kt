package com.tinytotsnbites.papapoints.data

import androidx.room.*

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

    @Query("SELECT * FROM rating WHERE task_id = :taskId")
    fun getByTaskId(taskId: Long): List<Rating>
}