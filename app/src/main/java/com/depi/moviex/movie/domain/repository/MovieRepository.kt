package com.depi.moviex.movie.domain.repository

import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.utils.Response
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun fetchDiscoverMovies(): Flow<Response<List<Movie>>>
    fun fetchTrendingMovies(): Flow<Response<List<Movie>>>
}