package com.depi.moviex.data.local.extensions

import com.depi.moviex.data.local.entity.WatchlistMovieEntity
import com.depi.moviex.movie.domain.models.Movie

fun WatchlistMovieEntity.toDomainMovie(): Movie {
    return Movie(
        backdropPath = this.backdropPath ?: "",
        genreIds = emptyList(),
        id = this.id,
        originalLanguage = "",
        originalTitle = this.title,
        overview = this.overview,
        popularity = 0.0,
        posterPath = this.posterPath ?: "",
        releaseDate = this.releaseDate,
        title = this.title,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        video = false
    )
}

fun Movie.toWatchlistEntity(category: String = "trending"): WatchlistMovieEntity {
    return WatchlistMovieEntity(
        id = this.id,
        title = this.title,
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
        releaseDate = this.releaseDate,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        overview = this.overview,
        category = category
    )
}
