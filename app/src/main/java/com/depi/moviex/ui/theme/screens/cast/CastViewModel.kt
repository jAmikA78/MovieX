package com.depi.moviex.ui.theme.screens.cast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.movie_detail.domain.models.Cast
import com.depi.moviex.movie_detail.domain.repository.MovieDetailRepository
import com.depi.moviex.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CastState(
    val cast: List<Cast> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CastViewModel @Inject constructor(
    private val repository: MovieDetailRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    private val _castState = MutableStateFlow(CastState())
    val castState = _castState.asStateFlow()

    init {
        fetchCast(movieId)
    }

    private fun fetchCast(movieId: Int) = viewModelScope.launch {
        repository.fetchMovieDetail(movieId).collectAndHandle(
            onError = { error ->
                _castState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _castState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movieDetail ->
            _castState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    cast = movieDetail.cast
                )
            }
        }
    }
}