package com.tinytotsnbites.papapoints

data class Task (
    val id: Long,
    val title: String,
   // val description: String,
    val points: Float
)
    {
    override fun toString(): String {
        return "(Task name $title) (id $id) (points $points)"
    }
}
