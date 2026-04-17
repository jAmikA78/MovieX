package com.depi.moviex.cast_member.domain.models

data class CastMember(
    val id: Int,
    val name: String,
    val biography: String,
    val birthday: String,
    val deathday: String?,
    val placeOfBirth: String,
    val profilePath: String?,
    val knownForDepartment: String,
    val popularity: Double,
    val knownFor: List<KnownForMovie>
)

data class KnownForMovie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val releaseDate: String,
    val mediaType: String
)