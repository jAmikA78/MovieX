package com.depi.moviex.movie.data.mapper_impl

import com.depi.moviex.common.MediaType
import com.depi.moviex.common.data.ApiMapper
import com.depi.moviex.movie.data.remote.models.MovieDto
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.models.MovieCategory

class MovieApiMapperImpl : ApiMapper<List<Movie>, MovieDto> {

    override fun mapToDomain(entity: MovieDto): List<Movie> {
        return entity.results?.filter { result ->
            result?.adult != true && !result.isExplicit()
        }?.map { result ->
            val isTvShow = !result?.name.isNullOrEmpty()
            Movie(
                backdropPath = formatEmptyValue(result?.backdropPath),
                genreIds = formatGenre(result?.genreIds),
                id = result?.id ?: 0,
                originalLanguage = formatEmptyValue(result?.originalLanguage, "language"),
                originalTitle = formatEmptyValue(result?.originalTitle ?: result?.originalName, "title"),
                overview = formatEmptyValue(result?.overview, "overview"),
                popularity = result?.popularity ?: 0.0,
                posterPath = formatEmptyValue(result?.posterPath),
                releaseDate = formatEmptyValue(result?.releaseDate ?: result?.firstAirDate, "date"),
                title = formatEmptyValue(result?.title ?: result?.name, "title"),
                voteAverage = result?.voteAverage ?: 0.0,
                voteCount = result?.voteCount ?: 0,
                video = result?.video ?: false,
                mediaType = if (isTvShow) MediaType.TV else MediaType.MOVIE
            )
        } ?: emptyList()
    }

    private fun formatEmptyValue(value: String?, default: String = ""): String {
        if (value.isNullOrEmpty()) return "Unknown $default"
        return value
    }

    private fun formatGenre(genreIds: List<Int?>?): List<String> {
        return genreIds?.map { MovieCategory.getGenreNameById(it ?: 0) } ?: emptyList()
    }
}

private val explicitKeywords = setOf("erotic", "porn", "xxx", "nsfw", "hardcore")

private fun com.depi.moviex.movie.data.remote.models.Result?.isExplicit(): Boolean {
    if (this == null) return false
    val text = "${title.orEmpty()} ${originalTitle.orEmpty()} ${name.orEmpty()} ${originalName.orEmpty()}".lowercase()
    return explicitKeywords.any { text.contains(it) }
}