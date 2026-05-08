package com.depi.moviex.ui.theme.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.movie.domain.models.Movie
import com.depi.moviex.movie.domain.models.MovieCategory
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
        MovieCategory.entries.forEach { fetchCategory(it) }
    }

    private fun fetchCategory(category: MovieCategory) = viewModelScope.launch {
        repository.fetchMovies(category).collectAndHandle(
            onError = { error ->
                _homeState.update { it.copy(isLoading = false, error = error?.message) }
            },
            onLoading = {
                _homeState.update { it.copy(isLoading = true, error = null) }
            }
        ) { movies ->
            _homeState.update { state ->
                state.copy(
                    isLoading = false,
                    error = null,
                    discoverMovies = if (category == MovieCategory.DISCOVER) movies else state.discoverMovies,
                    trendingMovies = if (category == MovieCategory.TRENDING) movies else state.trendingMovies,
                    tvShows = if (category == MovieCategory.TV_SHOWS) movies else state.tvShows,
                    actionMovies = if (category == MovieCategory.ACTION) movies else state.actionMovies,
                    dramaMovies = if (category == MovieCategory.DRAMA) movies else state.dramaMovies,
                    comedyMovies = if (category == MovieCategory.COMEDY) movies else state.comedyMovies,
                )
            }
        }
    }
}
