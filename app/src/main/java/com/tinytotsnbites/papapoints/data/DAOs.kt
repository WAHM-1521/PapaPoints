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

    @Query("DELETE FROM child")
    fun deleteAll()

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

    @Query("DELETE FROM task WHERE user_defined = 1")
    fun deleteUserDefinedTasks()

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getById(id: Long): Task

    //@Query("SELECT task.*, rating.rating FROM task LEFT JOIN rating ON task.id = rating.task_id WHERE rating.date = :date OR rating.date IS NULL")
    @Query("SELECT task.*, rating.rating FROM task LEFT JOIN rating ON task.id = rating.task_id AND rating.date = :date WHERE (rating.date IS NULL OR rating.date = :date) AND task.enabled = 1")
    fun getTasksWithRatingForDate(date: Date): List<TaskWithRating>

    @Query("UPDATE task SET enabled = 0 WHERE id = :taskId")
    fun disableTask(taskId: Long)

    //enable the Task which were earlier disabled during Delete Task
    @Query("UPDATE task SET enabled = 1 WHERE enabled = 0")
    fun enableAll()
}

@Dao
interface RatingDao {
    @Insert
    fun insert(rating: Rating)

    @Update
    fun update(rating: Rating)

    @Delete
    fun delete(rating: Rating)

    @Query("DELETE FROM rating")
    fun deleteAll()

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


@Dao
interface RewardDao {
    @Insert
    fun insert(reward: Reward)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reward : List<Reward>)

    @Update
    fun update(reward: Reward)

    @Delete
    fun delete(reward: Reward)

    @Query("DELETE FROM reward WHERE user_defined = 1")
    fun deleteUserDefinedRewards()

    @Query("SELECT * FROM reward")
    fun getAll(): List<Reward>

    @Query("SELECT * FROM reward WHERE id = :id")
    fun getById(id: Long): Reward

    @Query("SELECT reward.*, redeem.redeem FROM reward LEFT JOIN redeem ON reward.id = redeem.reward_id AND redeem.date = :date WHERE (redeem.date IS NULL OR redeem.date = :date) AND reward.enabled = 1")
    fun getRewardsRedeemedForDate(date: Date): List<RewardsRedeemed>

    @Query("UPDATE reward SET enabled = 0 WHERE id = :rewardId")
    fun disableReward(rewardId: Long)

    //enable the reward which were earlier disabled during Delete Task
    @Query("UPDATE reward SET enabled = 1 WHERE enabled = 0")
    fun enableAll()
}

@Dao
interface RedeemDao {
    @Insert
    fun insert(redeem: Redeem)

    @Update
    fun update(redeem: Redeem)

    @Delete
    fun delete(redeem: Redeem)

    @Query("DELETE FROM redeem")
    fun deleteAll()

    @Query("SELECT * FROM redeem")
    fun getAll(): List<Redeem>

    @Query("SELECT * FROM redeem WHERE id = :id")
    fun getById(id: Long): Redeem

    @Query("SELECT * FROM redeem WHERE child_id = :childId")
    fun getByChildId(childId: Long): List<Redeem>

    @Query("SELECT * FROM redeem WHERE reward_id = :rewardId AND date = :date")
    fun getByRewardId(rewardId: Long, date: Date): List<Redeem>

    @Query("SELECT SUM(redeem) FROM redeem")
    fun getTotalRewardsRedeemed(): Int

    @Query("SELECT SUM(redeem) FROM redeem WHERE date = :today")
    fun getTotalRewardsRedeemedToday(today: Date): Int

}