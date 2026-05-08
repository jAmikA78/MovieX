package com.depi.moviex.movie_detail.data.mapper_impl

import com.depi.moviex.movie_detail.data.remote.models.AuthorDetails
import com.depi.moviex.movie_detail.data.remote.models.CastDto
import com.depi.moviex.movie_detail.data.remote.models.Credits
import com.depi.moviex.movie_detail.data.remote.models.Crew
import com.depi.moviex.movie_detail.data.remote.models.Genre
import com.depi.moviex.movie_detail.data.remote.models.MovieDetailDto
import com.depi.moviex.movie_detail.data.remote.models.ProductionCountry
import com.depi.moviex.movie_detail.data.remote.models.ReviewsDto
import com.depi.moviex.movie_detail.data.remote.models.SpokenLanguage
import com.depi.moviex.movie_detail.data.remote.models.VideoDto
import com.depi.moviex.movie_detail.data.remote.models.VideosDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MovieDetailMapperImplTest {

    private val mapper = MovieDetailMapperImpl()

    @Test
    fun `mapToDomain maps all fields correctly`() {
        val dto = MovieDetailDto(
            id = 1,
            title = "Test Movie",
            overview = "Great movie",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            releaseDate = "2024-06-15",
            voteAverage = 8.0,
            voteCount = 200,
            popularity = 90.0,
            originalLanguage = "en",
            originalTitle = "Test Movie Original",
            video = false,
            runtime = 142,
            genres = listOf(Genre(name = "Action"), Genre(name = "Drama")),
            credits = Credits(
                cast = listOf(
                    CastDto(id = 10, name = "Actor One", gender = 2, character = "Hero", profilePath = "/actor.jpg"),
                    CastDto(id = 11, name = "Actor Two", gender = 1, character = "Heroine", profilePath = null)
                ),
                crew = listOf(
                    Crew(id = 20, name = "Director", job = "Director", department = "Directing", profilePath = "/dir.jpg")
                )
            ),
            spokenLanguages = listOf(SpokenLanguage(englishName = "English")),
            productionCountries = listOf(ProductionCountry(name = "United States")),
            reviews = ReviewsDto(
                results = listOf(
                    com.depi.moviex.movie_detail.data.remote.models.Result(
                        author = "Critic",
                        content = "Great!",
                        id = "r1",
                        createdAt = "2024-01-01T12:00:00.000Z",
                        authorDetails = AuthorDetails(rating = 9.0)
                    )
                )
            ),
            videos = VideosDto(
                results = listOf(
                    VideoDto(key = "abc123", site = "YouTube", type = "Trailer"),
                    VideoDto(key = "other", site = "Vimeo", type = "Teaser")
                )
            )
        )

        val result = mapper.mapToDomain(dto)

        assertEquals(1, result.id)
        assertEquals("Test Movie", result.title)
        assertEquals("Test Movie Original", result.originalTitle)
        assertEquals("Great movie", result.overview)
        assertEquals("/poster.jpg", result.posterPath)
        assertEquals("/backdrop.jpg", result.backdropPath)
        assertEquals("2024-06-15", result.releaseDate)
        assertEquals(8.0, result.voteAverage, 0.001)
        assertEquals(200, result.voteCount)
        assertEquals(90.0, result.popularity, 0.001)
        assertEquals("en", result.originalLanguage)
        assertEquals(false, result.video)
        assertEquals("2h:22m", result.runTime)
        assertEquals(listOf("Action", "Drama"), result.genreIds)
        assertEquals("https://www.youtube.com/watch?v=abc123", result.videoUrl)

        assertEquals(2, result.cast.size)
        assertEquals("Actor One", result.cast[0].name)
        assertEquals("Actor", result.cast[0].genderRole)
        assertEquals("Hero", result.cast[0].character)
        assertEquals("/actor.jpg", result.cast[0].profilePath)
        assertEquals("Actor Two", result.cast[1].name)
        assertEquals("Actress", result.cast[1].genderRole)

        assertEquals(1, result.crew.size)
        assertEquals("Director", result.crew[0].name)
        assertEquals("Director", result.crew[0].job)
        assertEquals("Directing", result.crew[0].department)

        assertEquals(listOf("English"), result.language)
        assertEquals(listOf("United States"), result.productionCountry)

        assertEquals(1, result.reviews.size)
        assertEquals("Critic", result.reviews[0].author)
        assertEquals("Great!", result.reviews[0].content)
        assertEquals(9.0, result.reviews[0].rating, 0.001)
    }

    @Test
    fun `mapToDomain handles null videoUrl when no YouTube trailer`() {
        val dto = MovieDetailDto(
            id = 2, title = "No Trailer",
            videos = VideosDto(
                results = listOf(VideoDto(key = "xyz", site = "Vimeo", type = "Trailer"))
            )
        )

        val result = mapper.mapToDomain(dto)

        assertNull(result.videoUrl)
    }

    @Test
    fun `mapToDomain handles null fields with defaults`() {
        val dto = MovieDetailDto(id = 3)

        val result = mapper.mapToDomain(dto)

        assertEquals("Unknown title", result.title)
        assertEquals("Unknown title", result.originalTitle)
        assertEquals("Unknown overview", result.overview)
        assertEquals("Unknown date", result.releaseDate)
        assertEquals("Unknown language", result.originalLanguage)
        assertEquals(0.0, result.voteAverage, 0.001)
        assertEquals(0, result.voteCount)
        assertEquals(0.0, result.popularity, 0.001)
        assertEquals(false, result.video)
        assertEquals("0h:0m", result.runTime)
        assertTrue(result.genreIds.isEmpty())
        assertTrue(result.cast.isEmpty())
        assertTrue(result.crew.isEmpty())
        assertTrue(result.language.isEmpty())
        assertTrue(result.productionCountry.isEmpty())
        assertTrue(result.reviews.isEmpty())
        assertNull(result.videoUrl)
    }

    @Test
    fun `formatTimeStamp parses ISO date correctly`() {
        val dto = MovieDetailDto(
            id = 4, title = "Time Test",
            reviews = ReviewsDto(
                results = listOf(
                    com.depi.moviex.movie_detail.data.remote.models.Result(
                        author = "User",
                        content = "OK",
                        id = "r2",
                        createdAt = "2024-03-15T10:30:00.000Z",
                        authorDetails = AuthorDetails()
                    )
                )
            )
        )

        val result = mapper.mapToDomain(dto)

        assertEquals("15.03.24", result.reviews[0].createdAt)
    }

    @Test
    fun `convertMinutesToHours formats correctly`() {
        val dto = MovieDetailDto(id = 5, title = "Runtime", runtime = 95)

        val result = mapper.mapToDomain(dto)

        assertEquals("1h:35m", result.runTime)
    }
}
