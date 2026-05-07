package com.depi.moviex.movie_detail.data.mapper_impl

import com.depi.moviex.common.data.ApiMapper
import com.depi.moviex.movie_detail.data.remote.models.CastDto
import com.depi.moviex.movie_detail.data.remote.models.Crew
import com.depi.moviex.movie_detail.data.remote.models.MovieDetailDto
import com.depi.moviex.movie_detail.domain.models.Cast
import com.depi.moviex.movie_detail.domain.models.Crew as DomainCrew
import com.depi.moviex.movie_detail.domain.models.MovieDetail
import com.depi.moviex.movie_detail.domain.models.Review
import java.text.SimpleDateFormat
import java.util.Locale

class MovieDetailMapperImpl : ApiMapper<MovieDetail, MovieDetailDto> {
    override fun mapToDomain(apiDto: MovieDetailDto): MovieDetail {
        return MovieDetail(
            backdropPath = formatEmptyValue(apiDto.backdropPath),
            genreIds = apiDto.genres?.map { formatEmptyValue(it?.name) } ?: emptyList(),
            id = apiDto.id ?: 0,
            originalLanguage = formatEmptyValue(apiDto.originalLanguage, "language"),
            originalTitle = formatEmptyValue(apiDto.originalTitle ?: apiDto.originalName, "title"),
            overview = formatEmptyValue(apiDto.overview, "overview"),
            popularity = apiDto.popularity ?: 0.0,
            posterPath = formatEmptyValue(apiDto.posterPath),
            releaseDate = formatEmptyValue(apiDto.releaseDate, "date"),
            title = formatEmptyValue(apiDto.title ?: apiDto.name, "title"),
            voteAverage = apiDto.voteAverage ?: 0.0,
            voteCount = apiDto.voteCount ?: 0,
            video = apiDto.video ?: false,
            videoUrl = apiDto.videos?.results?.firstOrNull {
                it?.site == "YouTube" && it.type == "Trailer"
            }?.key?.let { "https://www.youtube.com/watch?v=$it" },
            cast = formatCast(apiDto.credits?.cast),
            crew = formatCrew(apiDto.credits?.crew),
            language = apiDto.spokenLanguages?.map { formatEmptyValue(it?.englishName) }
                ?: emptyList<String>(),
            productionCountry = apiDto.productionCountries?.map { formatEmptyValue(it?.name) }
                ?: emptyList<String>(),
            reviews = apiDto.reviews?.results?.map {
                Review(
                    author = formatEmptyValue(it?.author),
                    content = formatEmptyValue(it?.content),
                    createdAt = formatTimeStamp(time = it?.createdAt ?: "0"),
                    id = formatEmptyValue(it?.id),
                    rating = it?.authorDetails?.rating ?: 0.0
                )
            } ?: emptyList<Review>(),
            runTime = convertMinutesToHours(apiDto.runtime ?: 0),
            videos = emptyList<com.depi.moviex.movie_detail.domain.models.Video>()
        )

    }

    private fun formatTimeStamp(pattern: String = "dd.MM.yy", time: String): String {
        val inputFormats = listOf(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        )

        val outputDateFormatter = SimpleDateFormat(
            pattern,
            Locale.getDefault()
        )

        // Try parsing with each format
        for (formatter in inputFormats) {
            try {
                val date = formatter.parse(time)
                if (date != null) {
                    return outputDateFormatter.format(date)
                }
            } catch (e: Exception) {
                // Try next format
            }
        }

        return time
    }

    private fun convertMinutesToHours(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return "${hours}h:${remainingMinutes}m"
    }

    private fun formatEmptyValue(value: String?, default: String = ""): String {
        if (value.isNullOrEmpty()) return "Unknown $default"
        return value
    }

    private fun formatCast(castDto: List<CastDto?>?): List<Cast> {
        return castDto?.map {
            val genderRole = if (it?.gender == 2) "Actor" else "Actress"
            Cast(
                id = it?.id ?: 0,
                name = formatEmptyValue(it?.name),
                genderRole = genderRole,
                character = formatEmptyValue(it?.character),
                profilePath = it?.profilePath
            )
        } ?: emptyList()
    }

    private fun formatCrew(crewDto: List<Crew?>?): List<DomainCrew> {
        return crewDto?.map {
            DomainCrew(
                id = it?.id ?: 0,
                name = formatEmptyValue(it?.name),
                job = formatEmptyValue(it?.job),
                department = formatEmptyValue(it?.department),
                profilePath = it?.profilePath
            )
        } ?: emptyList()
    }
}