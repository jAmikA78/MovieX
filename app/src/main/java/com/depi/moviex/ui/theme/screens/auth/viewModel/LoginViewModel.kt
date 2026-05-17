package com.depi.moviex.ui.theme.screens.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depi.moviex.auth.domain.models.LoginState
import com.depi.moviex.auth.domain.usecase.LoginUseCase
import com.depi.moviex.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    init {
        checkExistingSession()
    }

    private fun checkExistingSession() {
        if (authRepository.isLoggedIn()) {
            _loginState.value = LoginState.Success
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            loginUseCase(username, password).collect { result ->
                _loginState.value = if (result.success) {
                    LoginState.Success
                } else {
                    LoginState.Error(result.error ?: "Login failed")
                }
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }

    fun loginAsGuest() {
        authRepository.saveGuest()
    }

    fun logout() {
        authRepository.logout()
        _loginState.value = LoginState.Idle
    }
}
