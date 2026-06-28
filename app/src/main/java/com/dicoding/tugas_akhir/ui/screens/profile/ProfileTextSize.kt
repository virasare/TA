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
import com.dicoding.tugas_akhir.domain.model.TextSizeMode
import com.dicoding.tugas_akhir.ui.components.profile.InfoNote
import com.dicoding.tugas_akhir.ui.components.profile.SettingOptionCard
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings
import com.dicoding.tugas_akhir.ui.localization.getDescription
import com.dicoding.tugas_akhir.ui.localization.getLabel
import com.dicoding.tugas_akhir.ui.viewmodel.SettingsViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

@Composable
fun ProfileTextSizeScreen(
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
            count = TextSizeMode.entries.size,
        ) { index ->
            val textSizeMode = TextSizeMode.entries[index]

            SettingOptionCard(
                title = textSizeMode.getLabel(strings),
                description = textSizeMode.getDescription(strings),
                selected = settings.textSizeMode == textSizeMode,
                onClick = {
                    viewModel.updateTextSizeMode(textSizeMode)
                },
            )
        }

        item {
            InfoNote(
                title = strings.information,
                text = strings.textSizeNote,
            )
        }
    }
}
