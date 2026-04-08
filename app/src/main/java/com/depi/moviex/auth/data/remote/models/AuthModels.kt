package com.depi.moviex.auth.data.remote.models

import com.google.gson.annotations.SerializedName

data class RequestTokenResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("expires_at") val expiresAt: String?,
    @SerializedName("request_token") val requestToken: String?
)

data class ValidateLoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("request_token") val requestToken: String
)

data class ValidateLoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("expires_at") val expiresAt: String?,
    @SerializedName("request_token") val requestToken: String?
)

data class SessionResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("session_id") val sessionId: String?
)
