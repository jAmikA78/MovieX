package com.depi.moviex.ui.theme.screens.moviedetail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.auth.domain.repository.AuthRepository
import com.depi.moviex.common.MediaType
import com.depi.moviex.data.local.dao.ReminderDao
import com.depi.moviex.data.local.entity.ReminderEntity
import com.depi.moviex.utils.Response
import com.depi.moviex.movie_detail.domain.models.MovieDetail
import com.depi.moviex.movie_detail.domain.models.Video
import com.depi.moviex.movie_detail.domain.repository.MovieDetailRepository
import com.depi.moviex.utils.ReminderScheduler
import com.depi.moviex.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    val mediaType: MediaType = MediaType.MOVIE,
    val isReminderSet: Boolean = false
)

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieDetailRepository,
    private val reminderDao: ReminderDao,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])
    private val mediaType: MediaType = MediaType.fromValue(savedStateHandle["mediaType"] ?: "movie")

    private val _movieDetailState = MutableStateFlow(MovieDetailState(mediaType = mediaType))
    val movieDetailState = _movieDetailState.asStateFlow()

    init {
        fetchDetail(movieId, mediaType)
        fetchVideos(movieId, mediaType)
        checkReminder()
    }

    private fun checkReminder() {
        viewModelScope.launch {
            val exists = reminderDao.isReminderSetOnce(movieId, authRepository.getAccountName())
            _movieDetailState.update { it.copy(isReminderSet = exists) }
        }
    }

    fun toggleReminder(movieTitle: String, posterPath: String?, releaseDate: String) {
        viewModelScope.launch {
            val accountName = authRepository.getAccountName()
            val isSet = _movieDetailState.value.isReminderSet
            if (isSet) {
                reminderDao.removeReminder(movieId, accountName)
                ReminderScheduler.cancelReminder(context, movieId)
                _movieDetailState.update { it.copy(isReminderSet = false) }
            } else {
                reminderDao.addReminder(
                    ReminderEntity(
                        movieId = movieId,
                        accountName = accountName,
                        title = movieTitle,
                        posterPath = posterPath,
                        releaseDate = releaseDate
                    )
                )
                ReminderScheduler.scheduleReminder(context, movieId, movieTitle, posterPath, releaseDate)
                _movieDetailState.update { it.copy(isReminderSet = true) }
            }
        }
    }

    private fun fetchDetail(movieId: Int, mediaType: MediaType) = viewModelScope.launch {
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

    private fun fetchVideos(movieId: Int, mediaType: MediaType) = viewModelScope.launch {
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
