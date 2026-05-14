package com.depi.moviex.movie_detail.domain.repository

import com.depi.moviex.common.MediaType
import com.depi.moviex.utils.Response
import com.depi.moviex.movie_detail.domain.models.CollectionMovie
import com.depi.moviex.movie_detail.domain.models.MovieDetail
import com.depi.moviex.movie_detail.domain.models.Video
import kotlinx.coroutines.flow.Flow

interface MovieDetailRepository {
    fun fetchDetail(movieId: Int, mediaType: MediaType = MediaType.MOVIE): Flow<Response<MovieDetail>>
    fun fetchVideos(movieId: Int, mediaType: MediaType = MediaType.MOVIE): Flow<Response<List<Video>>>
    fun getMovieCollection(collectionId: Int): Flow<Response<List<CollectionMovie>>>
}
