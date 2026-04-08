package com.depi.moviex.ui.theme.screens.auth.viewModel

import androidx.lifecycle.ViewModel
import com.depi.moviex.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class SignUpState {
    object Idle : SignUpState()
    object MessageShown : SignUpState()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()

    fun onSignUpClick() {
        _signUpState.value = SignUpState.MessageShown
    }

    fun onGuestLogin() {
        authRepository.logout()
    }

    fun resetState() {
        _signUpState.value = SignUpState.Idle
    }
}
