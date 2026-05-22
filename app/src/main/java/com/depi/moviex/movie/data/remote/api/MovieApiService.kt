package com.depi.moviex.movie.data.remote.api

import com.depi.moviex.BuildConfig
import com.depi.moviex.movie.data.remote.models.MovieDto
import com.depi.moviex.movie.data.remote.models.MultiSearchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("discover/movie")
    suspend fun fetchDiscoverMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("with_genres") genreId: Int? = null,
        @Query("with_original_language") originalLanguage: String? = null,
        @Query("with_origin_country") originCountry: String? = null,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieDto

    @GET("trending/movie/week")
    suspend fun fetchTrendingMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieDto

    @GET("discover/tv")
    suspend fun fetchTvShows(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("with_original_language") originalLanguage: String? = null,
        @Query("with_origin_country") originCountry: String? = null,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieDto

    @GET("movie/top_rated")
    suspend fun fetchTopRatedMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieDto

    @GET("movie/upcoming")
    suspend fun fetchUpcomingMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieDto

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MultiSearchDto
}
