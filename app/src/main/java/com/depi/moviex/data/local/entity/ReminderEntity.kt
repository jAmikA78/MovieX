package com.depi.moviex.data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "reminder_movies",
    primaryKeys = ["movieId", "accountName"]
)
data class ReminderEntity(
    val movieId: Int,
    val accountName: String,
    val title: String,
    val posterPath: String?,
    val releaseDate: String,
    val backdropPath: String? = null,
    val overview: String? = null,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
