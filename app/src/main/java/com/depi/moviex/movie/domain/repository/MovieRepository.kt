package com.depi.moviex.movie.domain.repository

import androidx.paging.PagingData
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie_detail.data.remote.models.MovieResponse
import com.depi.moviex.utils.Response
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun fetchDiscoverMovies(): Flow<Response<List<Movie>>>
    fun fetchTrendingMovies(): Flow<Response<List<Movie>>>
    fun fetchTvShows(): Flow<Response<List<Movie>>>
    fun fetchActionMovies(): Flow<Response<List<Movie>>>
    fun fetchDramaMovies(): Flow<Response<List<Movie>>>
    fun fetchComedyMovies(): Flow<Response<List<Movie>>>

    suspend fun searchMovies(query: String): Result<MovieResponse>

    fun fetchTrendingMoviesPaged(category: String): Flow<PagingData<Movie>>
}