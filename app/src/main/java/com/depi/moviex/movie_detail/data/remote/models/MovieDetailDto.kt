package com.depi.moviex.movie_detail.data.remote.models


import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailDto(
    @SerialName("adult")
    val adult: Boolean? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection? = null,
    @SerialName("budget")
    val budget: Int? = null,
    @SerialName("credits")
    val credits: Credits? = null,
    @SerialName("genres")
    val genres: List<Genre?>? = null,
    @SerialName("homepage")
    val homepage: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("imdb_id")
    val imdbId: String? = null,
    @SerialName("origin_country")
    val originCountry: List<String?>? = null,
    @SerialName("original_language")
    val originalLanguage: String? = null,
    @SerialName("original_title")
    val originalTitle: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("popularity")
    val popularity: Double? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany?>? = null,
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountry?>? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("revenue")
    val revenue: Int? = null,
    @SerialName("reviews")
    val reviews: ReviewsDto? = null,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage?>? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("tagline")
    val tagline: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("video")
    val video: Boolean? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("vote_count")
    val voteCount: Int? = null,
    @SerialName("videos")
    @Contextual
    val videos: VideosDto? = null
)

@Serializable
data class VideosDto(
    @SerialName("results")
    val results: List<VideoDto?>? = null
)

@Serializable
data class VideoDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("key")
    val key: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("site")
    val site: String? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("official")
    val official: Boolean? = null
)

fun VideoDto.toDomain(): com.depi.moviex.movie_detail.domain.models.Video = com.depi.moviex.movie_detail.domain.models.Video(
    id = id ?: "",
    key = key ?: "",
    name = name ?: "",
    site = site ?: "",
    type = type ?: "",
    isOfficial = official ?: false
)