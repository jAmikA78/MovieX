package com.depi.moviex.ui.theme.screens.moviedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.utils.Response
import com.depi.moviex.movie_detail.domain.models.MovieDetail
import com.depi.moviex.movie_detail.domain.models.Video
import com.depi.moviex.movie_detail.domain.repository.MovieDetailRepository
import com.depi.moviex.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieDetailState(
    val movieDetail: MovieDetail? = null,
    val videos: List<Video> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val mediaType: String = "movie"
)

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieDetailRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])
    private val mediaType: String = savedStateHandle["mediaType"] ?: "movie"

    private val _movieDetailState = MutableStateFlow(MovieDetailState(mediaType = mediaType))
    val movieDetailState = _movieDetailState.asStateFlow()

    init {
        fetchDetail(movieId, mediaType)
        fetchVideos(movieId, mediaType)
    }

    private fun fetchDetail(movieId: Int, mediaType: String) = viewModelScope.launch {
        repository.fetchDetail(movieId, mediaType).collectAndHandle(
            onError = { error ->
                _movieDetailState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _movieDetailState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movieDetail ->
            _movieDetailState.update {
                it.copy(isLoading = false, error = null, movieDetail = movieDetail)
            }
        }
    }

    private fun fetchVideos(movieId: Int, mediaType: String) = viewModelScope.launch {
        repository.fetchVideos(movieId, mediaType).collectAndHandle(
            onError = { },
            onLoading = { }
        ) { videos ->
            _movieDetailState.update {
                it.copy(videos = videos)
            }
        }
    }
}