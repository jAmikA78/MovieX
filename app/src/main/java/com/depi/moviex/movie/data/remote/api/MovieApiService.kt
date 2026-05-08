package com.depi.moviex.movie.data.remote.api

import com.depi.moviex.BuildConfig
import com.depi.moviex.movie.data.remote.models.MovieDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("discover/movie")
    suspend fun fetchDiscoverMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("with_genres") genreId: Int? = null,
        @Query("page") page: Int = 1
    ): MovieDto

    @GET("trending/movie/week")
    suspend fun fetchTrendingMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1
    ): MovieDto

    @GET("discover/tv")
    suspend fun fetchTvShows(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("page") page: Int = 1
    ): MovieDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MovieDto
}
