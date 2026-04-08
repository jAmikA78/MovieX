package com.depi.moviex.auth.data.remote.api

import com.depi.moviex.auth.data.remote.models.RequestTokenResponse
import com.depi.moviex.auth.data.remote.models.SessionResponse
import com.depi.moviex.auth.data.remote.models.ValidateLoginRequest
import com.depi.moviex.auth.data.remote.models.ValidateLoginResponse
import com.depi.moviex.BuildConfig
import com.depi.moviex.utils.K
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @GET(K.AUTH_TOKEN_ENDPOINT)
    suspend fun getRequestToken(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): RequestTokenResponse

    @POST(K.AUTH_TOKEN_VALIDATE_ENDPOINT)
    suspend fun validateLogin(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Body request: ValidateLoginRequest
    ): ValidateLoginResponse

    @GET(K.AUTH_SESSION_ENDPOINT)
    suspend fun createSession(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("request_token") requestToken: String
    ): SessionResponse
}
