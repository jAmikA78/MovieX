package com.depi.moviex.auth.domain.usecase

import com.depi.moviex.auth.domain.models.AuthResult
import com.depi.moviex.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Flow<AuthResult> {
        if (username.isBlank()) {
            return kotlinx.coroutines.flow.flowOf(AuthResult(success = false, error = "Username cannot be empty"))
        }
        if (password.isBlank()) {
            return kotlinx.coroutines.flow.flowOf(AuthResult(success = false, error = "Password cannot be empty"))
        }
        return repository.login(username, password)
    }
}
