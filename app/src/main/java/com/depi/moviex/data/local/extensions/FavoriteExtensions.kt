package com.depi.moviex.data.local.extensions

import com.depi.moviex.cast_member.domain.models.KnownForMovie
import com.depi.moviex.common.MediaType
import com.depi.moviex.data.local.entity.FavoriteMovieEntity
import com.depi.moviex.movie.domain.models.Movie

fun FavoriteMovieEntity.toDomainMovie(): Movie {
    return Movie(
        backdropPath = this.backdropPath ?: "",
        genreIds = emptyList(),
        id = this.movieId,
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

fun Movie.toFavoriteEntity(accountName: String, category: String = "trending"): FavoriteMovieEntity {
    return FavoriteMovieEntity(
        movieId = this.id,
        accountName = accountName,
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

fun KnownForMovie.toMovie(): Movie {
    return Movie(
        backdropPath = backdropPath ?: "",
        genreIds = emptyList(),
        id = id,
        originalLanguage = "",
        originalTitle = title,
        overview = "",
        popularity = 0.0,
        posterPath = posterPath ?: "",
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        voteCount = 0,
        video = false,
        mediaType = MediaType.fromValue(mediaType)
    )
}
