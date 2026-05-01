package com.depi.moviex.ui.theme.screens.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileState(
    val username: String = "MovieX User",
    val email: String = "user@moviex.com",
    val joinDate: String = "January 2024"
)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()
}
