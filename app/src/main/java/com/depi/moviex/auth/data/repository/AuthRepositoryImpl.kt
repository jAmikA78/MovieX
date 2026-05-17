package com.depi.moviex.auth.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.depi.moviex.auth.data.remote.api.AuthApi
import com.depi.moviex.auth.data.remote.models.ValidateLoginRequest
import com.depi.moviex.auth.domain.models.AuthResult
import com.depi.moviex.auth.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    @ApplicationContext context: Context
) : AuthRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun login(username: String, password: String): Flow<AuthResult> = flow {
        try {
            val tokenResponse = authApi.getRequestToken()
            if (!tokenResponse.success || tokenResponse.requestToken == null) {
                emit(AuthResult(success = false, error = "Failed to get request token"))
                return@flow
            }

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

            val sessionResponse = authApi.createSession(requestToken = tokenResponse.requestToken)
            if (!sessionResponse.success || sessionResponse.sessionId == null) {
                emit(AuthResult(success = false, error = "Failed to create session"))
                return@flow
            }

            savePreviousAccountName()
            clearGuest()
            saveSession(sessionResponse.sessionId)
            saveUsername(username)
            emit(AuthResult(success = true, sessionId = sessionResponse.sessionId))

        } catch (e: Exception) {
            val message = when {
                e is HttpException && e.code() == 401 -> "Invalid username or password"
                e is HttpException && e.code() == 404 -> "Service not available"
                e is HttpException && e.code() == 500 -> "Server error. Please try again later"
                e is HttpException -> "Login failed (${e.code()}). Please try again"
                else -> "Connection error. Check your internet and try again"
            }
            emit(AuthResult(success = false, error = message))
        }
    }

    override suspend fun register(username: String, email: String, password: String): AuthResult {
        if (username.isBlank()) return AuthResult(success = false, error = "Username cannot be empty")
        if (email.isBlank()) return AuthResult(success = false, error = "Email cannot be empty")
        if (password.isBlank()) return AuthResult(success = false, error = "Password cannot be empty")

        if (prefs.getBoolean(KEY_IS_REGISTERED, false)) {
            return AuthResult(success = false, error = "A user is already registered on this device")
        }

        savePreviousAccountName()
        clearGuest()
        prefs.edit()
            .putString(KEY_REGISTERED_USERNAME, username)
            .putString(KEY_REGISTERED_EMAIL, email)
            .putBoolean(KEY_IS_REGISTERED, true)
            .apply()
        return AuthResult(success = true)
    }

    override fun getSession(): String? = prefs.getString(KEY_SESSION_ID, null)

    override fun isLoggedIn(): Boolean = getSession() != null || prefs.getBoolean(KEY_IS_REGISTERED, false)

    override fun getRegisteredUsername(): String? = prefs.getString(KEY_REGISTERED_USERNAME, null)

    override fun saveGuest() {
        prefs.edit().putBoolean(KEY_IS_GUEST, true).apply()
    }

    override fun isGuest(): Boolean = prefs.getBoolean(KEY_IS_GUEST, false)

    override fun getAccountName(): String {
        getRegisteredUsername()?.let { return it }
        return GUEST_ACCOUNT_NAME
    }

    override fun getPreviousAccountName(): String? = prefs.getString(KEY_PREVIOUS_ACCOUNT, null)

    override fun clearPreviousAccountName() {
        prefs.edit().remove(KEY_PREVIOUS_ACCOUNT).apply()
    }

    override fun clearGuest() {
        prefs.edit().remove(KEY_IS_GUEST).apply()
    }

    override fun logout() {
        prefs.edit()
            .remove(KEY_SESSION_ID)
            .remove(KEY_IS_GUEST)
            .remove(KEY_IS_REGISTERED)
            .remove(KEY_REGISTERED_USERNAME)
            .remove(KEY_REGISTERED_EMAIL)
            .apply()
    }

    private fun saveSession(sessionId: String) {
        prefs.edit().putString(KEY_SESSION_ID, sessionId).apply()
    }

    private fun saveUsername(username: String) {
        prefs.edit().putString(KEY_REGISTERED_USERNAME, username).apply()
    }

    private fun savePreviousAccountName() {
        val currentName = getAccountName()
        prefs.edit().putString(KEY_PREVIOUS_ACCOUNT, currentName).apply()
    }

    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_SESSION_ID = "session_id"
        private const val KEY_IS_GUEST = "is_guest"
        private const val KEY_IS_REGISTERED = "is_registered"
        private const val KEY_REGISTERED_USERNAME = "registered_username"
        private const val KEY_REGISTERED_EMAIL = "registered_email"
        private const val KEY_PREVIOUS_ACCOUNT = "previous_account_name"
        private const val GUEST_ACCOUNT_NAME = "guest"
    }
}
