package com.depi.moviex.auth.domain.repository

import com.depi.moviex.auth.domain.models.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Flow<AuthResult>
    suspend fun register(username: String, email: String, password: String): AuthResult
    fun getSession(): String?
    fun isLoggedIn(): Boolean
    fun getRegisteredUsername(): String?
    fun saveGuest()
    fun isGuest(): Boolean
    fun clearGuest()
    fun logout()
}
