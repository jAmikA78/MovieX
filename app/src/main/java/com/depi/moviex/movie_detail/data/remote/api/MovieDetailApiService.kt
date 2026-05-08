package com.depi.moviex.movie_detail.data.remote.api

import com.depi.moviex.BuildConfig
import com.depi.moviex.movie_detail.data.remote.models.MovieDetailDto
import com.depi.moviex.utils.K
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val MOVIE_ID = "movie_id"
private const val TV_ID = "tv_id"

interface MovieDetailApiService {

    @GET("${K.MOVIE_DETAIL_ENDPOINT}/{$MOVIE_ID}")
    suspend fun fetchMovieDetail(
        @Path(MOVIE_ID) movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("append_to_response") appendToResponse: String = "credits,reviews,videos"
    ): MovieDetailDto

    @GET("tv/{$TV_ID}")
    suspend fun fetchTvDetail(
        @Path(TV_ID) tvId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("append_to_response") appendToResponse: String = "credits,reviews,videos"
    ): MovieDetailDto
}
