package com.dicoding.tugas_akhir.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.ui.screens.auth.AuthRequiredScreen
import com.dicoding.tugas_akhir.ui.state.AuthUiState
import com.dicoding.tugas_akhir.ui.viewmodel.AuthViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

@Composable
fun AuthGate(
    onLoginClick: () -> Unit,
    onBackClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val authViewModel: AuthViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    )

    val authUiState by authViewModel.authUiState.collectAsStateWithLifecycle()

    when (authUiState) {
        is AuthUiState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is AuthUiState.Authenticated -> {
            content()
        }

        is AuthUiState.Unauthenticated -> {
            AuthRequiredScreen(
                onLoginClick = onLoginClick,
                onBackClick = onBackClick,
            )
        }
    }
}