package com.dicoding.tugas_akhir.ui.state

import com.dicoding.tugas_akhir.domain.model.UserSession

sealed interface AuthUiState {
    data object Loading : AuthUiState
    data class Authenticated(val user: UserSession) : AuthUiState
    data object Unauthenticated : AuthUiState
}