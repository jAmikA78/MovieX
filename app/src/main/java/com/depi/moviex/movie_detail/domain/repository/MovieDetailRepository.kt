package com.depi.moviex.movie_detail.domain.repository

import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.utils.Response
import com.depi.moviex.movie_detail.domain.models.MovieDetail
import com.depi.moviex.movie_detail.domain.models.Video
import kotlinx.coroutines.flow.Flow

interface MovieDetailRepository {
    fun fetchDetail(movieId: Int, mediaType: String = "movie"): Flow<Response<MovieDetail>>
    fun fetchVideos(movieId: Int, mediaType: String = "movie"): Flow<Response<List<Video>>>
    fun fetchMovie(): Flow<Response<List<Movie>>>
}