package com.example.jetmovie.movie_detail.domain.repository

import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.utils.Response
import com.example.jetmovie.movie_detail.domain.models.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieDetailRepository {
    fun fetchMovieDetail(movieId: Int): Flow<Response<MovieDetail>>
    fun fetchMovie(): Flow<Response<List<Movie>>>
}