package com.depi.moviex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist_movies")
data class WatchlistMovieEntity(
    @PrimaryKey
    val id: Int,
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
