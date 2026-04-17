package com.depi.moviex.ui.theme.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.repository.MovieRepository
import com.depi.moviex.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val message: String = "Welcome to MovieX",
    val discoverMovies: List<Movie> = emptyList(),
    val trendingMovies: List<Movie> = emptyList(),
    val tvShows: List<Movie> = emptyList(),
    val actionMovies: List<Movie> = emptyList(),
    val dramaMovies: List<Movie> = emptyList(),
    val comedyMovies: List<Movie> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        fetchDiscoverMovie()
        fetchTrendingMovie()
        fetchTvShows()
        fetchActionMovies()
        fetchDramaMovies()
        fetchComedyMovies()
    }


    private fun fetchDiscoverMovie() = viewModelScope.launch {
        repository.fetchDiscoverMovies().collectAndHandle(
            onError = { error ->
                _homeState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _homeState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movie ->
            _homeState.update {
                it.copy(isLoading = false, error = null, discoverMovies = movie)
            }
        }
    }
    private fun fetchTrendingMovie() = viewModelScope.launch {
        repository.fetchTrendingMovies().collectAndHandle(
            onError = { error ->
                _homeState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _homeState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movie ->
            _homeState.update {
                it.copy(isLoading = false, error = null, trendingMovies = movie)
            }
        }
    }

    private fun fetchTvShows() = viewModelScope.launch {
        repository.fetchTvShows().collectAndHandle(
            onError = { error ->
                _homeState.update { it.copy(error = error?.message) }
            },
            onLoading = { }
        ) { movie ->
            _homeState.update { it.copy(tvShows = movie) }
        }
    }

    private fun fetchActionMovies() = viewModelScope.launch {
        repository.fetchActionMovies().collectAndHandle(
            onError = { error ->
                _homeState.update { it.copy(error = error?.message) }
            },
            onLoading = { }
        ) { movie ->
            _homeState.update { it.copy(actionMovies = movie) }
        }
    }

    private fun fetchDramaMovies() = viewModelScope.launch {
        repository.fetchDramaMovies().collectAndHandle(
            onError = { error ->
                _homeState.update { it.copy(error = error?.message) }
            },
            onLoading = { }
        ) { movie ->
            _homeState.update { it.copy(dramaMovies = movie) }
        }
    }

    private fun fetchComedyMovies() = viewModelScope.launch {
        repository.fetchComedyMovies().collectAndHandle(
            onError = { error ->
                _homeState.update { it.copy(error = error?.message) }
            },
            onLoading = { }
        ) { movie ->
            _homeState.update { it.copy(comedyMovies = movie) }
        }
    }
}