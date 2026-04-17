package com.depi.moviex.ui.theme.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.movie.domain.use_case.SearchMoviesUseCase
import com.depi.moviex.movie.data.remote.models.MovieDto
import com.depi.moviex.movie_detail.data.remote.models.MovieResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _movies = MutableStateFlow<List<MovieDto>>(emptyList())
    val movies = _movies.asStateFlow()

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.length > 2) {
            search(newQuery)
        }
    }

    private fun search(query: String) {
        viewModelScope.launch {

            val result: kotlin.Result<MovieResponse> = searchMoviesUseCase(query)

            result.onSuccess { response ->
                _movies.value = response.results
            }.onFailure { error ->

                println("Search Error: ${error.message}")
            }
        }
    }

    private fun searchMoviesUseCase(string: String): Result<MovieResponse> {
        TODO("Not yet implemented")
    }
}