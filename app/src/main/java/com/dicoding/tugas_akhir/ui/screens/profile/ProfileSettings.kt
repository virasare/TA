package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.profile.*
import com.dicoding.tugas_akhir.ui.theme.Background

@Composable
fun SettingsScreen(
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
    onAboutClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            SectionLabel(text = "Akun")

            MenuGroup {
                DetailMenuItem(
                    icon = Icons.Outlined.Settings,
                    title = "Bahasa",
                    description = "Indonesia",
                    onClick = onLanguageClick
                )

                DetailMenuItem(
                    icon = Icons.Outlined.Settings,
                    title = "Tema",
                    description = "Mode terang",
                    onClick = onThemeClick
                )

                DetailMenuItem(
                    icon = Icons.Outlined.Notifications,
                    title = "Notifikasi",
                    description = "Aktif",
                    onClick = {}
                )
            }
        }

        item {
            SectionLabel(text = "Informasi")

            MenuGroup {
                DetailMenuItem(
                    icon = Icons.Outlined.Info,
                    title = "Tentang Aplikasi",
                    description = "Informasi versi dan aplikasi",
                    onClick = onAboutClick
                )

                DetailMenuItem(
                    icon = Icons.Outlined.Help,
                    title = "Pusat Bantuan",
                    description = "Panduan penggunaan aplikasi",
                    onClick = onHelpClick
                )
            }
        }
    }
}