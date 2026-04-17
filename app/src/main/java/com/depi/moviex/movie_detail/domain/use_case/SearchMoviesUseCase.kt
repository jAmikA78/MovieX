package com.depi.moviex.movie.domain.use_case

import com.depi.moviex.movie.domain.repository.MovieRepository
import com.depi.moviex.movie_detail.data.remote.models.MovieResponse
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(query: String): Result<MovieResponse> {
        return repository.searchMovies(query)
    }
}
