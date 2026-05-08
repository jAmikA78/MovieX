package com.depi.moviex.auth.domain.usecase

import com.depi.moviex.auth.domain.models.AuthResult
import com.depi.moviex.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginUseCaseTest {

    private val repository: AuthRepository = mockk()
    private val useCase = LoginUseCase(repository)

    @Test
    fun `invoke with blank username returns error`() = runTest {
        val result = useCase("", "password123").first()

        assertFalse(result.success)
        assertEquals("Username cannot be empty", result.error)
    }

    @Test
    fun `invoke with blank password returns error`() = runTest {
        val result = useCase("user", "").first()

        assertFalse(result.success)
        assertEquals("Password cannot be empty", result.error)
    }

    @Test
    fun `invoke with valid credentials delegates to repository`() = runTest {
        val expected = AuthResult(success = true, sessionId = "session123")
        coEvery { repository.login("user", "pass") } returns flowOf(expected)

        val result = useCase("user", "pass").first()

        assertTrue(result.success)
        assertEquals("session123", result.sessionId)
    }
}
