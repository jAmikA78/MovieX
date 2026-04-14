package com.depi.moviex.ui.theme.screens.MovieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.depi.moviex.movie_detail.data.remote.api.MovieDetailApiService

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val apiService: MovieDetailApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            try {
                val response = apiService.fetchMovieDetail(movieId)

                _uiState.value = DetailsUiState.Success(
                    title = response.title ?: "Unknown",
                    runtime = formatRuntime(response.runtime ?: 0),

                    rating = "${String.format(java.util.Locale.US, "%.1f", response.voteAverage ?: 0.0)} (TMDB)",
                    releaseDate = response.releaseDate ?: "N/A",
                    synopsis = response.overview ?: "No description.",

                    genres = response.genres?.mapNotNull { it?.name } ?: emptyList(),

                    cast = response.credits?.cast?.take(10)?.mapNotNull { it?.name } ?: emptyList(),
                    posterUrl = "https://image.tmdb.org/t/p/w500${response.posterPath}"
                )
            } catch (e: Exception) {
                _uiState.value = DetailsUiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    private fun formatRuntime(minutes: Int): String {
        if (minutes <= 0) return "N/A"
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return "${hours}h ${remainingMinutes}m"
    }
}

sealed class DetailsUiState {
    object Loading : DetailsUiState()
    data class Success(
        val title: String,
        val runtime: String,
        val rating: String,
        val releaseDate: String,
        val synopsis: String,
        val genres: List<String>,
        val cast: List<String>,
        val posterUrl: String
    ) : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()
}