package com.depi.moviex.movie.domain.use_case

import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.repository.MovieRepository
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(query: String): List<Movie> {
        return repository.searchMovies(query)
    }
}
