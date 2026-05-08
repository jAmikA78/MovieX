package com.depi.moviex.ui.theme.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.auth.domain.repository.AuthRepository
import com.depi.moviex.data.local.dao.FavoriteDao
import com.depi.moviex.data.local.extensions.toDomainMovie
import com.depi.moviex.data.local.extensions.toFavoriteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoriteState(
    val favoriteMovies: List<com.depi.moviex.movie.domain.models.Movie> = emptyList(),
    val availableCategories: List<String> = emptyList(),
    val selectedCategory: String = "All",
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _accountName = MutableStateFlow(accountName())

    private val _favoriteState = MutableStateFlow(FavoriteState())
    val favoriteState = _favoriteState.asStateFlow()

    init {
        migrateIfNeeded()
        observeAllFavorites()
        observeCategories()
    }

    private fun accountName() = authRepository.getAccountName()

    fun refreshAccountName() {
        val current = accountName()
        if (_accountName.value != current) {
            _accountName.value = current
        }
    }

    private fun observeAllFavorites() {
        viewModelScope.launch {
            _accountName
                .flatMapLatest { name -> favoriteDao.getAllFavoriteMovies(name) }
                .collect { entities ->
                    val movies = entities.map { it.toDomainMovie() }
                    _favoriteState.update {
                        it.copy(isLoading = false, favoriteMovies = movies)
                    }
                }
        }
    }

    private fun observeCategories() {
        viewModelScope.launch {
            _accountName
                .flatMapLatest { name -> favoriteDao.getCategories(name) }
                .collect { categories ->
                    _favoriteState.update {
                        it.copy(availableCategories = listOf("All") + categories)
                    }
                }
        }
    }

    private fun migrateIfNeeded() {
        val previous = authRepository.getPreviousAccountName()
        val current = accountName()
        if (previous != null && previous != current) {
            viewModelScope.launch {
                favoriteDao.migrateAccount(previous, current)
                authRepository.clearPreviousAccountName()
            }
        }
    }

    fun showAllFavorites() {
        viewModelScope.launch {
            val name = accountName()
            val entities = favoriteDao.getAllFavoriteMoviesOnce(name)
            val movies = entities.map { it.toDomainMovie() }
            _favoriteState.update {
                it.copy(favoriteMovies = movies, selectedCategory = "All")
            }
        }
    }

    fun loadMoviesByCategory(category: String) {
        viewModelScope.launch {
            val name = accountName()
            val entities = favoriteDao.getMoviesByCategoryOnce(name, category)
            val movies = entities.map { it.toDomainMovie() }
            _favoriteState.update {
                it.copy(favoriteMovies = movies, selectedCategory = category)
            }
        }
        viewModelScope.launch {
            val name = accountName()
            favoriteDao.getMoviesByCategory(name, category).collect { entities ->
                val movies = entities.map { it.toDomainMovie() }
                _favoriteState.update {
                    it.copy(favoriteMovies = movies, selectedCategory = category)
                }
            }
        }
    }

    suspend fun addToFavorite(movie: com.depi.moviex.movie.domain.models.Movie, category: String = CATEGORY_TRENDING) {
        val entity = movie.toFavoriteEntity(accountName(), category)
        favoriteDao.addToFavorite(entity)
    }

    suspend fun removeFromFavorite(movie: com.depi.moviex.movie.domain.models.Movie) {
        favoriteDao.removeFromFavorite(movie.id, accountName())
    }

    companion object {
        const val CATEGORY_TRENDING = "Trending"
        const val CATEGORY_DETAIL = "Detail"
        const val CATEGORY_SEARCH = "Search"
        const val CATEGORY_REMOVE = ""
    }

    fun isInFavorite(movieId: Int): Flow<Boolean> {
        return favoriteDao.isInFavorite(movieId, accountName())
    }
}
