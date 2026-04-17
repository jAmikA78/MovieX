package com.depi.moviex.cast_member.data.remote.api

import com.depi.moviex.BuildConfig
import com.depi.moviex.cast_member.data.remote.models.CastMemberDto
import com.depi.moviex.utils.K
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val PERSON_ID = "person_id"

interface CastMemberApiService {

    @GET("${K.MOVIE_ACTOR_ENDPOINT}/{$PERSON_ID}")
    suspend fun fetchCastMember(
        @Path(PERSON_ID) personId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): CastMemberDto
}