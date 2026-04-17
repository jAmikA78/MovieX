package com.depi.moviex.ui.theme.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.movie.domain.use_case.SearchMoviesUseCase
import com.depi.moviex.movie.data.remote.models.Result
import com.depi.moviex.movie_detail.data.remote.models.MovieResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _movies = MutableStateFlow<List<Result>>(emptyList())
    val movies = _movies.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        // Immediate UI update
        _searchQuery.value = newQuery
        
        // Cancel previous search job
        searchJob?.cancel()
        
        if (newQuery.length > 2) {
            // Start debounced search
            searchJob = viewModelScope.launch {
                delay(500)
                search(newQuery)
            }
        } else {
            _movies.value = emptyList()
        }
    }

    private suspend fun search(query: String) {
        Log.d("SearchViewModel", "Searching for: $query")
        
        // Run network operation on IO thread to prevent main thread blocking
        val result: kotlin.Result<MovieResponse> = withContext(Dispatchers.IO) {
            searchMoviesUseCase(query)
        }

        result.onSuccess { response ->
            Log.d("SearchViewModel", "Search success: ${response.results.size} movies found")
            _movies.value = response.results
        }.onFailure { error ->
            Log.e("SearchViewModel", "Search Error: ${error.message}", error)
        }
    }
}