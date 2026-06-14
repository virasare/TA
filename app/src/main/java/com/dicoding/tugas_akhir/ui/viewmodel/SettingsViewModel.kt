package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugas_akhir.data.repository.SettingsRepository
import com.dicoding.tugas_akhir.domain.model.LanguageMode
import com.dicoding.tugas_akhir.domain.model.ThemeMode
import com.dicoding.tugas_akhir.domain.model.UserSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val settingsState: StateFlow<UserSettings> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserSettings(),
        )

    fun updateThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.saveThemeMode(themeMode)
        }
    }

    fun updateLanguageMode(languageMode: LanguageMode) {
        viewModelScope.launch {
            settingsRepository.saveLanguageMode(languageMode)
        }
    }
}