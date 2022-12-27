package com.tinytotsnbites.papapoints.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Child(
    @PrimaryKey @ColumnInfo(name = "id") val childId: Long,
    @ColumnInfo(name = "child_name") val name: String?,
    @ColumnInfo(name = "child_age") val age: Int
)

@Entity
data class Task(
    @PrimaryKey @ColumnInfo(name  = "id") val taskId: Long,
    @ColumnInfo(name = "task_name") val taskName: String
)

@Entity
data class Rating(
    @PrimaryKey @ColumnInfo(name = "id") val ratingId: Long,
    @ColumnInfo(name = "child_id") val childId: Long,
    @ColumnInfo(name = "task_id") val taskId: Long,
    val rating: Float,
    val date: Date
)