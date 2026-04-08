package com.depi.moviex.auth.data.repository

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSession(sessionId: String) {
        prefs.edit().putString(KEY_SESSION_ID, sessionId).apply()
    }

    fun getSession(): String? {
        return prefs.getString(KEY_SESSION_ID, null)
    }

    fun isLoggedIn(): Boolean {
        return getSession() != null
    }

    fun logout() {
        prefs.edit().remove(KEY_SESSION_ID).apply()
    }

    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_SESSION_ID = "session_id"
    }
}
