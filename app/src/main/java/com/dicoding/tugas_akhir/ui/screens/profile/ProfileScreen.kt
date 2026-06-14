package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ContactSupport
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.domain.model.UserProfile
import com.dicoding.tugas_akhir.ui.components.loading.ProfilePlaceholder
import com.dicoding.tugas_akhir.ui.components.profile.ProfileHeaderCard
import com.dicoding.tugas_akhir.ui.components.profile.ProfileMenuCard
import com.dicoding.tugas_akhir.ui.components.profile.ProfileMenuItemData
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings
import com.dicoding.tugas_akhir.ui.state.AuthUiState
import com.dicoding.tugas_akhir.ui.viewmodel.AuthViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ProfileViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit,
    onPassengerDataClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAboutClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onLogoutSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
    profileViewModel: ProfileViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val authUiState by authViewModel.authUiState.collectAsStateWithLifecycle()
    val profile by profileViewModel.profile.collectAsStateWithLifecycle()

    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onDismiss = {
                showLogoutDialog = false
            },
            onConfirm = {
                showLogoutDialog = false
                authViewModel.logout()
                onLogoutSuccess()
            },
        )
    }

    when (authUiState) {
        is AuthUiState.Loading -> {
            ProfilePlaceholder(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
        }

        is AuthUiState.Authenticated -> {
            ProfileContent(
                profile = profile,
                onEditProfileClick = onEditProfileClick,
                onPassengerDataClick = onPassengerDataClick,
                onSettingsClick = onSettingsClick,
                onHelpClick = onHelpClick,
                onAboutClick = onAboutClick,
                onSecurityClick = onSecurityClick,
                onLogoutClick = {
                    showLogoutDialog = true
                },
                modifier = modifier,
            )
        }

        is AuthUiState.Unauthenticated -> {
            ProfilePlaceholder(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}

@Composable
private fun ProfileContent(
    profile: UserProfile,
    onEditProfileClick: () -> Unit,
    onPassengerDataClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAboutClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("profile_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProfileHeaderCard(
            profile = profile,
        )

        ProfileMenuCard(
            title = strings.account,
            items = listOf(
                ProfileMenuItemData(
                    title = strings.editProfile,
                    subtitle = strings.editProfileSubtitle,
                    icon = Icons.Outlined.Edit,
                    onClick = onEditProfileClick,
                ),
                ProfileMenuItemData(
                    title = strings.passengerData,
                    subtitle = strings.passengerDataSubtitle,
                    icon = Icons.Outlined.People,
                    onClick = onPassengerDataClick,
                ),
            ),
        )

        ProfileMenuCard(
            title = strings.application,
            items = listOf(
                ProfileMenuItemData(
                    title = strings.settings,
                    subtitle = strings.settingsSubtitle,
                    icon = Icons.Outlined.Settings,
                    onClick = onSettingsClick,
                ),
                ProfileMenuItemData(
                    title = strings.help,
                    subtitle = strings.helpSubtitle,
                    icon = Icons.AutoMirrored.Outlined.ContactSupport,
                    onClick = onHelpClick,
                ),
                ProfileMenuItemData(
                    title = strings.aboutApp,
                    subtitle = strings.aboutAppSubtitle,
                    icon = Icons.Outlined.Info,
                    onClick = onAboutClick,
                ),
            ),
        )

        ProfileMenuCard(
            title = strings.security,
            items = listOf(
                ProfileMenuItemData(
                    title = strings.loginStatus,
                    subtitle = strings.accountActive,
                    icon = Icons.Outlined.Shield,
                    onClick = onSecurityClick,
                ),
            ),
        )

        OutlinedButton(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = null,
                tint = colors.error,
            )

            Text(
                text = strings.logout,
                modifier = Modifier.padding(start = 8.dp),
                color = colors.error,
            )
        }
    }
}

@Composable
private fun LogoutConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val strings = LocalAppStrings.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(strings.logoutDialogTitle)
        },
        text = {
            Text(strings.logoutDialogMessage)
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
            ) {
                Text(strings.logout)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
            ) {
                Text(strings.cancel)
            }
        },
    )
}