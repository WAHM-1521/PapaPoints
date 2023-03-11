package com.tinytotsnbites.papapoints.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Child(
    @PrimaryKey @ColumnInfo(name = "id") val childId: Long,
    @ColumnInfo(name = "child_name") val name: String?,
    @ColumnInfo(name = "child_DOB") val dob: Date,
    @ColumnInfo(name = "child_gender") val gender: String,
    @ColumnInfo(name = "child_image_uri") val childImageUri: Uri?
)

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name  = "id") val taskId: Long = 0,
    @ColumnInfo(name = "task_name") val taskName: String,
    @ColumnInfo(name = "enabled") val enabled: Boolean,
    @ColumnInfo(name = "user_defined") val user_defined: Boolean
)

@Entity
data class Rating(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val ratingId: Long,
    @ColumnInfo(name = "child_id") val childId: Long,
    @ColumnInfo(name = "task_id") val taskId: Long,
    val rating: Int,
    val date: Date
)

@Entity
data class TaskWithRating(
    @Embedded val task: Task,
    @ColumnInfo(name = "rating") val rating: Int
)

@Entity
data class Reward(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name  = "id") val rewardId: Long = 0,
    @ColumnInfo(name = "reward_name") val rewardName: String,
    @ColumnInfo(name = "enabled") val enabled: Boolean,
    @ColumnInfo(name = "user_defined") val user_defined: Boolean,
    @ColumnInfo(name = "points_required") val pointsRequired: Int,
    @ColumnInfo(name = "image_url") val imageUrl: String?
)

@Entity
data class Redeem(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val redeemId: Long,
    @ColumnInfo(name = "child_id") val childId: Long,
    @ColumnInfo(name = "reward_id") val rewardId: Long,
    val redeem: Int,
    val date: Date
)

@Entity
data class RewardsRedeemed(
    @Embedded val rewards: Reward,
    @ColumnInfo(name = "redeem") val redeem: Int
)