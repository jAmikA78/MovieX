package com.depi.moviex.ui.theme.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.movie.domain.use_case.SearchMoviesUseCase
import com.depi.moviex.movie.domain.models.Movie
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

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies = _movies.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        searchJob?.cancel()
        if (newQuery.length > 2) {
            searchJob = viewModelScope.launch {
                delay(500)
                search(newQuery)
            }
        } else {
            _movies.value = emptyList()
        }
    }

    private suspend fun search(query: String) {
        try {
            val result = withContext(Dispatchers.IO) {
                searchMoviesUseCase(query)
            }
            _movies.value = result
            Log.d("SearchViewModel", "Search success: ${result.size} movies found")
        } catch (e: Exception) {
            Log.e("SearchViewModel", "Search Error: ${e.message}", e)
        }
    }
}
