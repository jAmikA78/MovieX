package com.depi.moviex.movie_detail.data.remote.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.depi.moviex.movie.data.remote.models.Result

@Serializable
data class MovieResponse(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<Result>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)