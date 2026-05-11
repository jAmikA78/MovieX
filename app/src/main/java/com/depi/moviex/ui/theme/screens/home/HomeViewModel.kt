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
    val animationMovies: List<Movie> = emptyList(),
    val topRatedMovies: List<Movie> = emptyList(),
    val documentariesMovies: List<Movie> = emptyList(),
    val horrorMovies: List<Movie> = emptyList(),
    val familyKidsMovies: List<Movie> = emptyList(),
    val warMovies: List<Movie> = emptyList(),
    val crimeMovies: List<Movie> = emptyList(),
    val egyptianMovies: List<Movie> = emptyList(),
    val egyptianTv: List<Movie> = emptyList(),
    val koreanTv: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList(),
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
            val shuffled = movies.shuffled()
            _homeState.update { state ->
                state.copy(
                    isLoading = false,
                    error = null,
                    discoverMovies = if (category == MovieCategory.DISCOVER) shuffled else state.discoverMovies,
                    trendingMovies = if (category == MovieCategory.TRENDING) shuffled else state.trendingMovies,
                    tvShows = if (category == MovieCategory.TV_SHOWS) shuffled else state.tvShows,
                    actionMovies = if (category == MovieCategory.ACTION) shuffled else state.actionMovies,
                    dramaMovies = if (category == MovieCategory.DRAMA) shuffled else state.dramaMovies,
                    comedyMovies = if (category == MovieCategory.COMEDY) shuffled else state.comedyMovies,
                    animationMovies = if (category == MovieCategory.ANIMATION) shuffled else state.animationMovies,
                    topRatedMovies = if (category == MovieCategory.TOP_RATED) shuffled else state.topRatedMovies,
                    documentariesMovies = if (category == MovieCategory.DOCUMENTARIES) shuffled else state.documentariesMovies,
                    horrorMovies = if (category == MovieCategory.HORROR) shuffled else state.horrorMovies,
                    familyKidsMovies = if (category == MovieCategory.FAMILY_KIDS) shuffled else state.familyKidsMovies,
                    warMovies = if (category == MovieCategory.WAR) shuffled else state.warMovies,
                    crimeMovies = if (category == MovieCategory.CRIME) shuffled else state.crimeMovies,
                    egyptianMovies = if (category == MovieCategory.EGYPTIAN_MOVIES) shuffled else state.egyptianMovies,
                    egyptianTv = if (category == MovieCategory.EGYPTIAN_TV) shuffled else state.egyptianTv,
                    koreanTv = if (category == MovieCategory.KOREAN_TV) shuffled else state.koreanTv,
                    upcomingMovies = if (category == MovieCategory.UPCOMING) shuffled else state.upcomingMovies,
                )
            }
        }
    }
}
