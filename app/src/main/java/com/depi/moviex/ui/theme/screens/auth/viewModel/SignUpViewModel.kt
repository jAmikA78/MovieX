package com.depi.moviex.ui.theme.screens.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.auth.domain.repository.AuthRepository
import com.depi.moviex.auth.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()

    fun onSignUpClick(username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            val result = signUpUseCase(username, email, password, confirmPassword)
            _signUpState.value = if (result.success) {
                SignUpState.Success
            } else {
                SignUpState.Error(result.error ?: "Registration failed")
            }
        }
    }

    fun onGuestLogin() {
        authRepository.saveGuest()
    }

    fun resetState() {
        _signUpState.value = SignUpState.Idle
    }
}
