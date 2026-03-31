package com.depi.moviex.movie.data.remote.api

import com.depi.moviex.movie.data.remote.models.MovieDto
import com.depi.moviex.BuildConfig
import com.depi.moviex.utils.K
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET(K.MOVIE_ENDPOINT)
    suspend fun fetchDiscoverMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto

    @GET(K.TRENDING_MOVIE_ENDPOINT)
    suspend fun fetchTrendingMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto
}