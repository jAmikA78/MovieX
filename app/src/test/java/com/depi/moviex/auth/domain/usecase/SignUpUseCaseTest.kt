package com.depi.moviex.auth.domain.usecase

import com.depi.moviex.auth.domain.models.AuthResult
import com.depi.moviex.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SignUpUseCaseTest {

    private val repository: AuthRepository = mockk()
    private val useCase = SignUpUseCase(repository)

    @Test
    fun `invoke rejects username shorter than 3 characters`() = runTest {
        val result = useCase("ab", "a@b.com", "pass123", "pass123")

        assertFalse(result.success)
        assertEquals("Username must be at least 3 characters", result.error)
    }

    @Test
    fun `invoke rejects blank email`() = runTest {
        val result = useCase("user", "", "pass123", "pass123")

        assertFalse(result.success)
        assertEquals("Please enter a valid email address", result.error)
    }

    @Test
    fun `invoke rejects email without at sign`() = runTest {
        val result = useCase("user", "notanemail", "pass123", "pass123")

        assertFalse(result.success)
        assertEquals("Please enter a valid email address", result.error)
    }

    @Test
    fun `invoke rejects password shorter than 6 characters`() = runTest {
        val result = useCase("user", "a@b.com", "12345", "12345")

        assertFalse(result.success)
        assertEquals("Password must be at least 6 characters", result.error)
    }

    @Test
    fun `invoke rejects mismatched passwords`() = runTest {
        val result = useCase("user", "a@b.com", "password1", "password2")

        assertFalse(result.success)
        assertEquals("Passwords do not match", result.error)
    }

    @Test
    fun `invoke delegates to repository on valid input`() = runTest {
        coEvery { repository.register("validuser", "a@b.com", "pass123") } returns AuthResult(success = true)

        val result = useCase("validuser", "a@b.com", "pass123", "pass123")

        assertTrue(result.success)
    }

    @Test
    fun `invoke returns repository error on failure`() = runTest {
        coEvery { repository.register("user", "a@b.com", "pass123") } returns AuthResult(
            success = false, error = "Username already taken"
        )

        val result = useCase("user", "a@b.com", "pass123", "pass123")

        assertFalse(result.success)
        assertEquals("Username already taken", result.error)
    }
}
