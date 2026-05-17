package com.depi.moviex.utils

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsTest {

    @Test
    fun `collectAndHandle calls stateReducer on Success`() = runTest {
        val flow = flowOf(Response.Success("hello"))

        var captured: String? = null
        flow.collectAndHandle(
            stateReducer = { captured = it }
        )

        assertEquals("hello", captured)
    }

    @Test
    fun `collectAndHandle calls onError on Error`() = runTest {
        val error = Exception("test error")
        val flow = flowOf(Response.Error<String>(error))

        var capturedError: Throwable? = null
        flow.collectAndHandle(
            onError = { capturedError = it },
            stateReducer = {}
        )

        assertEquals(error, capturedError)
    }

    @Test
    fun `collectAndHandle calls onLoading on Loading`() = runTest {
        val flow = flow {
            emit(Response.Loading<String>())
            emit(Response.Success("done"))
        }

        var loadingCalled = false
        flow.collectAndHandle(
            onLoading = { loadingCalled = true },
            stateReducer = {}
        )

        assertEquals(true, loadingCalled)
    }

    @Test
    fun `collectAndHandle handles all three states in sequence`() = runTest {
        val flow = flow {
            emit(Response.Loading<String>())
            emit(Response.Success("data"))
            emit(Response.Error(Exception("err")))
        }

        val states = mutableListOf<String>()
        flow.collectAndHandle(
            onLoading = { states.add("loading") },
            onError = { states.add("error") },
            stateReducer = { states.add("success:$it") }
        )

        assertEquals(listOf("loading", "success:data", "error"), states)
    }
}
