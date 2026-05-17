package com.depi.moviex.movie.domain.use_case

import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchMoviesUseCaseTest {

    private val repository: MovieRepository = mockk()
    private val useCase = SearchMoviesUseCase(repository)

    @Test
    fun `invoke delegates to repository searchMovies`() = runTest {
        val expected = listOf(
            Movie(
                id = 1, title = "Result 1", overview = "Overview 1",
                posterPath = "/p1.jpg", backdropPath = "/b1.jpg",
                releaseDate = "2024-01-01", voteAverage = 7.0, voteCount = 50,
                popularity = 60.0, originalLanguage = "en", originalTitle = "Result 1",
                genreIds = emptyList(), video = false
            )
        )
        coEvery { repository.searchMovies("test query") } returns expected

        val result = useCase("test query")

        assertEquals(expected, result)
    }

    @Test
    fun `invoke returns empty list for empty query`() = runTest {
        coEvery { repository.searchMovies("") } returns emptyList()

        val result = useCase("")

        assertEquals(0, result.size)
    }
}
