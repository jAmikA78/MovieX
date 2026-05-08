package com.depi.moviex.ui.theme.screens.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.data.local.dao.WatchlistDao
import com.depi.moviex.data.local.entity.WatchlistMovieEntity
import com.depi.moviex.data.local.extensions.toDomainMovie
import com.depi.moviex.data.local.extensions.toWatchlistEntity
import com.depi.moviex.ui.theme.screens.watchlist.WatchlistState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WatchlistState(
    val watchlistMovies: List<com.depi.moviex.movie.domain.models.Movie> = emptyList(),
    val availableCategories: List<String> = emptyList(),
    val selectedCategory: String = "All",
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistDao: WatchlistDao,
) : ViewModel() {

    private val _watchlistState = MutableStateFlow(WatchlistState())
    val watchlistState = _watchlistState.asStateFlow()

    init {
        loadWatchlistMovies()
        loadCategories()
    }

    fun loadWatchlistMovies() = viewModelScope.launch {
        watchlistDao.getAllWatchlistMovies().collect { entities ->
            val movies = entities.map { it.toDomainMovie() }
            _watchlistState.update {
                it.copy(isLoading = false, watchlistMovies = movies)
            }
        }
    }

    fun loadMoviesByCategory(category: String) = viewModelScope.launch {
        watchlistDao.getMoviesByCategory(category).collect { entities ->
            val movies = entities.map { it.toDomainMovie() }
            _watchlistState.update {
                it.copy(watchlistMovies = movies, selectedCategory = category)
            }
        }
    }

    private fun loadCategories() = viewModelScope.launch {
        watchlistDao.getCategories().collect { categories ->
            _watchlistState.update {
                it.copy(availableCategories = listOf("All") + categories)
            }
        }
    }

    suspend fun addToWatchlist(movie: com.depi.moviex.movie.domain.models.Movie, category: String = CATEGORY_TRENDING) {
        val entity = movie.toWatchlistEntity(category)
        watchlistDao.addToWatchlist(entity)
    }

    suspend fun removeFromWatchlist(movie: com.depi.moviex.movie.domain.models.Movie) {
        val entity = movie.toWatchlistEntity(CATEGORY_REMOVE)
        watchlistDao.removeFromWatchlist(entity)
    }

    companion object {
        const val CATEGORY_TRENDING = "Trending"
        const val CATEGORY_DETAIL = "Detail"
        const val CATEGORY_SEARCH = "Search"
        const val CATEGORY_REMOVE = ""
    }

    fun isInWatchlist(movieId: Int): Flow<Boolean> {
        return watchlistDao.isInWatchlist(movieId)
    }
}
