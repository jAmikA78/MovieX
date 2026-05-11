package com.depi.moviex.movie.domain.models

sealed class SearchResultItem {
    data class MovieResult(val movie: Movie) : SearchResultItem()
    data class TvResult(val movie: Movie) : SearchResultItem()
    data class PersonResult(
        val id: Int,
        val name: String,
        val profilePath: String?,
        val knownFor: String
    ) : SearchResultItem()
}
