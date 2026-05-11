package com.depi.moviex.ui.theme.screens.notifications

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.auth.domain.repository.AuthRepository
import com.depi.moviex.data.local.dao.ReminderDao
import com.depi.moviex.data.local.entity.ReminderEntity
import com.depi.moviex.utils.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsState(
    val reminders: List<ReminderEntity> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val reminderDao: ReminderDao,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _accountName = MutableStateFlow(authRepository.getAccountName())

    private val _state = MutableStateFlow(NotificationsState())
    val state = _state.asStateFlow()

    init {
        observeReminders()
    }

    private fun observeReminders() {
        viewModelScope.launch {
            _accountName
                .flatMapLatest { name -> reminderDao.getAllReminders(name) }
                .collect { reminders ->
                    _state.update {
                        it.copy(isLoading = false, reminders = reminders)
                    }
                }
        }
    }

    fun removeReminder(movieId: Int) {
        viewModelScope.launch {
            val accountName = authRepository.getAccountName()
            reminderDao.removeReminder(movieId, accountName)
            ReminderScheduler.cancelReminder(context, movieId)
        }
    }
}
