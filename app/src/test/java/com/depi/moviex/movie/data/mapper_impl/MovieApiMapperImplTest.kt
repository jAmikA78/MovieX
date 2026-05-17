package com.depi.moviex.movie.data.mapper_impl

import com.depi.moviex.common.MediaType
import com.depi.moviex.movie.data.remote.models.MovieDto
import com.depi.moviex.movie.data.remote.models.Result
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MovieApiMapperImplTest {

    private val mapper = MovieApiMapperImpl()

    @Test
    fun `mapToDomain maps result correctly`() {
        val dto = MovieDto(
            page = 1,
            results = listOf(
                Result(
                    id = 123,
                    title = "Test Movie",
                    overview = "A test overview",
                    posterPath = "/poster.jpg",
                    backdropPath = "/backdrop.jpg",
                    releaseDate = "2024-01-15",
                    voteAverage = 8.5,
                    voteCount = 100,
                    popularity = 95.0,
                    originalLanguage = "en",
                    originalTitle = "Test Movie",
                    genreIds = listOf(28, 18),
                    video = false
                )
            )
        )

        val movies = mapper.mapToDomain(dto)

        assertEquals(1, movies.size)
        val movie = movies[0]
        assertEquals(123, movie.id)
        assertEquals("Test Movie", movie.title)
        assertEquals("Test Movie", movie.originalTitle)
        assertEquals("A test overview", movie.overview)
        assertEquals("/poster.jpg", movie.posterPath)
        assertEquals("/backdrop.jpg", movie.backdropPath)
        assertEquals("2024-01-15", movie.releaseDate)
        assertEquals(8.5, movie.voteAverage, 0.001)
        assertEquals(100, movie.voteCount)
        assertEquals(95.0, movie.popularity, 0.001)
        assertEquals("en", movie.originalLanguage)
        assertEquals(MediaType.MOVIE, movie.mediaType)
        assertEquals(listOf("Action", "Drama"), movie.genreIds)
        assertEquals(false, movie.video)
    }

    @Test
    fun `mapToDomain maps TV show correctly`() {
        val dto = MovieDto(
            results = listOf(
                Result(
                    id = 456,
                    name = "Test TV Show",
                    originalName = "Test TV Show Original",
                    firstAirDate = "2023-06-01",
                    overview = "TV overview",
                    popularity = 80.0
                )
            )
        )

        val movies = mapper.mapToDomain(dto)

        assertEquals(1, movies.size)
        val movie = movies[0]
        assertEquals("Test TV Show", movie.title)
        assertEquals("Test TV Show Original", movie.originalTitle)
        assertEquals("2023-06-01", movie.releaseDate)
        assertEquals(MediaType.TV, movie.mediaType)
    }

    @Test
    fun `mapToDomain handles null values with defaults`() {
        val dto = MovieDto(
            results = listOf(Result())
        )

        val movies = mapper.mapToDomain(dto)

        assertEquals(1, movies.size)
        val movie = movies[0]
        assertEquals(0, movie.id)
        assertEquals("Unknown title", movie.title)
        assertEquals("Unknown title", movie.originalTitle)
        assertEquals("Unknown overview", movie.overview)
        assertEquals("Unknown date", movie.releaseDate)
        assertEquals("Unknown language", movie.originalLanguage)
        assertEquals(0.0, movie.voteAverage, 0.001)
        assertEquals(0, movie.voteCount)
        assertEquals(0.0, movie.popularity, 0.001)
        assertEquals(MediaType.MOVIE, movie.mediaType)
        assertTrue(movie.genreIds.isEmpty())
        assertEquals(false, movie.video)
    }

    @Test
    fun `mapToDomain handles null results list`() {
        val dto = MovieDto(results = null)
        val movies = mapper.mapToDomain(dto)
        assertTrue(movies.isEmpty())
    }

    @Test
    fun `mapToDomain handles null genreIds`() {
        val dto = MovieDto(
            results = listOf(
                Result(id = 1, title = "No Genres", genreIds = null)
            )
        )
        val movies = mapper.mapToDomain(dto)
        assertTrue(movies[0].genreIds.isEmpty())
    }
}
