package com.depi.moviex.cast_member.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CastMemberDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("biography")
    val biography: String? = null,
    @SerialName("birthday")
    val birthday: String? = null,
    @SerialName("deathday")
    val deathday: String? = null,
    @SerialName("place_of_birth")
    val placeOfBirth: String? = null,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("known_for_department")
    val knownForDepartment: String? = null,
    @SerialName("popularity")
    val popularity: Double = 0.0,
    @SerialName("known_for")
    val knownFor: List<KnownForMovieDto> = emptyList()
)

@Serializable
data class KnownForMovieDto(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("title")
    val title: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("media_type")
    val mediaType: String = ""
)