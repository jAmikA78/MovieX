package com.depi.moviex.movie_detail.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
) {
    fun toDomain(): com.depi.moviex.movie_detail.domain.models.Video = com.depi.moviex.movie_detail.domain.models.Video(
        id = id ?: "",
        key = key ?: "",
        name = name ?: "",
        site = site ?: "",
        type = type ?: "",
        isOfficial = official ?: false
    )
}

@Serializable
data class VideosResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("results")
    val results: List<VideoDto?>? = null
)