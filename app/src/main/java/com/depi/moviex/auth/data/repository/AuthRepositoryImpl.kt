package com.depi.moviex.auth.data.repository

import com.depi.moviex.auth.data.remote.api.AuthApi
import com.depi.moviex.auth.data.remote.models.ValidateLoginRequest
import com.depi.moviex.auth.domain.models.AuthResult
import com.depi.moviex.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authManager: AuthManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Flow<AuthResult> = flow {
        try {
            // Step 1: Get request token
            val tokenResponse = authApi.getRequestToken()
            if (!tokenResponse.success || tokenResponse.requestToken == null) {
                emit(AuthResult(success = false, error = "Failed to get request token"))
                return@flow
            }

            // Step 2: Validate login with username/password
            val validateRequest = ValidateLoginRequest(
                username = username,
                password = password,
                requestToken = tokenResponse.requestToken
            )
            val validateResponse = authApi.validateLogin(request = validateRequest)
            if (!validateResponse.success) {
                emit(AuthResult(success = false, error = "Invalid username or password"))
                return@flow
            }

            // Step 3: Create session
            val sessionResponse = authApi.createSession(requestToken = tokenResponse.requestToken)
            if (!sessionResponse.success || sessionResponse.sessionId == null) {
                emit(AuthResult(success = false, error = "Failed to create session"))
                return@flow
            }

            // Step 4: Save session
            authManager.saveSession(sessionResponse.sessionId)
            emit(AuthResult(success = true, sessionId = sessionResponse.sessionId))

        } catch (e: Exception) {
            emit(AuthResult(success = false, error = e.message ?: "Unknown error occurred"))
        }
    }

    override fun getSession(): String? = authManager.getSession()

    override fun isLoggedIn(): Boolean = authManager.isLoggedIn()

    override fun logout() = authManager.logout()
}
