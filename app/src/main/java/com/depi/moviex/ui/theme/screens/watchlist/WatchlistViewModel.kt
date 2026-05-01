package com.depi.moviex.ui.theme.screens.watchlist

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

data class WatchlistState(
    val watchlistMovies: List<Movie> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {

    private val _watchlistState = MutableStateFlow(WatchlistState())
    val watchlistState = _watchlistState.asStateFlow()

    init {
        fetchWatchlistMovies()
    }

    private fun fetchWatchlistMovies() = viewModelScope.launch {
        repository.fetchTrendingMovies().collectAndHandle(
            onError = { error ->
                _watchlistState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _watchlistState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movies ->
            _watchlistState.update {
                it.copy(isLoading = false, error = null, watchlistMovies = movies.take(10))
            }
        }
    }
}
