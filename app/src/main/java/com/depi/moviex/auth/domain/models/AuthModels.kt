package com.depi.moviex.auth.domain.models

data class AuthResult(
    val success: Boolean,
    val sessionId: String? = null,
    val error: String? = null
)

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
