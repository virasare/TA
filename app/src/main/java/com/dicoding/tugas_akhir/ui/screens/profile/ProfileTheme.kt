package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.domain.model.ThemeMode
import com.dicoding.tugas_akhir.ui.components.profile.InfoNote
import com.dicoding.tugas_akhir.ui.components.profile.SettingOptionCard
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings
import com.dicoding.tugas_akhir.ui.localization.getDescription
import com.dicoding.tugas_akhir.ui.localization.getLabel
import com.dicoding.tugas_akhir.ui.viewmodel.SettingsViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

@Composable
fun ProfileThemeScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val strings = LocalAppStrings.current
    val settings by viewModel.settingsState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(
            count = ThemeMode.values().size,
        ) { index ->
            val themeMode = ThemeMode.values()[index]

            SettingOptionCard(
                title = themeMode.getLabel(strings),
                description = themeMode.getDescription(strings),
                selected = settings.themeMode == themeMode,
                onClick = {
                    viewModel.updateThemeMode(themeMode)
                },
            )
        }

        item {
            InfoNote(
                title = strings.information,
                text = strings.themeNote,
            )
        }
    }
}