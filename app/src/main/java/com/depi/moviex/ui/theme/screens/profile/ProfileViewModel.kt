package com.depi.moviex.ui.theme.screens.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileState(
    val username: String = DEFAULT_USERNAME,
    val email: String = DEFAULT_EMAIL,
    val joinDate: String = DEFAULT_JOIN_DATE
) {
    companion object {
        const val DEFAULT_USERNAME = "MovieX User"
        const val DEFAULT_EMAIL = "user@moviex.com"
        const val DEFAULT_JOIN_DATE = "January 2024"
    }
}

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()
}
