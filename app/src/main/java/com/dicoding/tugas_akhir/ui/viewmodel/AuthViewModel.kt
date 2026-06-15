package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugas_akhir.data.repository.AuthRepository
import com.dicoding.tugas_akhir.ui.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val authUiState: StateFlow<AuthUiState> = _authUiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.observeAuthState().collect { user ->
                _authUiState.value = if (user != null) {
                    AuthUiState.Authenticated(user)
                } else {
                    AuthUiState.Unauthenticated
                }
            }
        }
    }

    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        authRepository.loginWithEmail(
            email = email,
            password = password,
            onSuccess = onSuccess,
            onError = onError,
        )
    }

    fun registerWithEmail(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        authRepository.registerWithEmail(
            name = name,
            email = email,
            password = password,
            onSuccess = onSuccess,
            onError = onError,
        )
    }

    fun loginWithGoogle(
        idToken: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        authRepository.loginWithGoogle(
            idToken = idToken,
            onSuccess = onSuccess,
            onError = onError,
        )
    }

    fun getCurrentUser() = authRepository.getCurrentUser()

    fun isLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }

    fun logout() {
        authRepository.logout()
    }
}