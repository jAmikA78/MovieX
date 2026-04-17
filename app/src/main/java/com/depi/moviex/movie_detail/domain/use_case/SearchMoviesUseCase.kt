package com.depi.moviex.movie.domain.use_case

import com.depi.moviex.movie_detail.data.remote.models.MovieResponse

interface MovieRepository {

    suspend fun searchMovies(query: String): Result<MovieResponse>
}

class SearchMoviesUseCase {

}
