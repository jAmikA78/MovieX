package com.depi.moviex.ui.theme.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.movie.domain.models.SearchResultItem
import com.depi.moviex.movie.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class SearchFilter {
    ALL,
    MOVIE,
    TV,
    PERSON
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _allResults = MutableStateFlow<List<SearchResultItem>>(emptyList())
    val allResults = _allResults.asStateFlow()

    private val _selectedFilter = MutableStateFlow(SearchFilter.ALL)
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _filteredResults = MutableStateFlow<List<SearchResultItem>>(emptyList())
    val filteredResults = _filteredResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        searchJob?.cancel()
        _error.value = null
        if (newQuery.length > 2) {
            searchJob = viewModelScope.launch {
                delay(500)
                search(newQuery)
            }
        } else {
            _allResults.value = emptyList()
            _filteredResults.value = emptyList()
            _isLoading.value = false
        }
    }

    fun setFilter(filter: SearchFilter) {
        _selectedFilter.value = filter
        applyFilter()
    }

    fun clearError() {
        _error.value = null
    }

    private fun applyFilter() {
        val filter = _selectedFilter.value
        val all = _allResults.value
        _filteredResults.value = when (filter) {
            SearchFilter.ALL -> all
            SearchFilter.MOVIE -> all.filterIsInstance<SearchResultItem.MovieResult>()
            SearchFilter.TV -> all.filterIsInstance<SearchResultItem.TvResult>()
            SearchFilter.PERSON -> all.filterIsInstance<SearchResultItem.PersonResult>()
        }
    }

    private suspend fun search(query: String) {
        _isLoading.value = true
        _error.value = null
        try {
            val result = withContext(Dispatchers.IO) {
                repository.searchAll(query)
            }
            _allResults.value = result
            applyFilter()
            Log.d("SearchViewModel", "Search success: ${result.size} results found")
        } catch (e: Exception) {
            Log.e("SearchViewModel", "Search Error: ${e.message}", e)
            _error.value = e.message ?: "Unknown error occurred"
            _allResults.value = emptyList()
            _filteredResults.value = emptyList()
        } finally {
            _isLoading.value = false
        }
    }
}
