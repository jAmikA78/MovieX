package com.depi.moviex.auth.domain.usecase

import com.depi.moviex.auth.domain.models.AuthResult
import com.depi.moviex.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): AuthResult {
        if (username.length < 3) {
            return AuthResult(success = false, error = "Username must be at least 3 characters")
        }
        if (email.isBlank() || !email.contains("@")) {
            return AuthResult(success = false, error = "Please enter a valid email address")
        }
        if (password.length < 6) {
            return AuthResult(success = false, error = "Password must be at least 6 characters")
        }
        if (password != confirmPassword) {
            return AuthResult(success = false, error = "Passwords do not match")
        }
        return repository.register(username, email, password)
    }
}
