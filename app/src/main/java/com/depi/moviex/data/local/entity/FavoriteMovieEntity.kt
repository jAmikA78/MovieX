package com.depi.moviex.data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "favorite_movies",
    primaryKeys = ["movieId", "accountName"]
)
data class FavoriteMovieEntity(
    val movieId: Int,
    val accountName: String,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val overview: String,
    val category: String,
    val addedAt: Long = System.currentTimeMillis()
)
