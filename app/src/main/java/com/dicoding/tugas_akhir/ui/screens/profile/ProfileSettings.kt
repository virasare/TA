package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.profile.ProfileMenuCard
import com.dicoding.tugas_akhir.ui.components.profile.ProfileMenuItemData
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings

@Composable
fun SettingsScreen(
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
    onAboutClick: () -> Unit,
    onHelpClick: () -> Unit,
    onSecurityClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ProfileMenuCard(
                title = strings.preferences,
                items = listOf(
                    ProfileMenuItemData(
                        title = strings.language,
                        subtitle = strings.languageSubtitle,
                        icon = Icons.Outlined.Language,
                        onClick = onLanguageClick,
                    ),
                    ProfileMenuItemData(
                        title = strings.theme,
                        subtitle = strings.themeSubtitle,
                        icon = Icons.Outlined.DarkMode,
                        onClick = onThemeClick,
                    ),
                ),
            )
        }

        item {
            ProfileMenuCard(
                title = strings.information,
                items = listOf(
                    ProfileMenuItemData(
                        title = strings.help,
                        subtitle = strings.helpSubtitle,
                        icon = Icons.AutoMirrored.Outlined.HelpOutline,
                        onClick = onHelpClick,
                    ),
                    ProfileMenuItemData(
                        title = strings.aboutApp,
                        subtitle = strings.aboutAppSubtitle,
                        icon = Icons.Outlined.Info,
                        onClick = onAboutClick,
                    ),
                    ProfileMenuItemData(
                        title = strings.security,
                        subtitle = strings.securitySubtitle,
                        icon = Icons.Outlined.Security,
                        onClick = onSecurityClick,
                    ),
                ),
            )
        }
    }
}