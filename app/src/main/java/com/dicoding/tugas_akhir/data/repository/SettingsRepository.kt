package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.data.local.datastore.SettingsDataStore
import com.dicoding.tugas_akhir.domain.model.LanguageMode
import com.dicoding.tugas_akhir.domain.model.ThemeMode
import com.dicoding.tugas_akhir.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

class SettingsRepository private constructor(
    private val settingsDataStore: SettingsDataStore,
) {

    fun getSettings(): Flow<UserSettings> {
        return settingsDataStore.settingsFlow
    }

    suspend fun saveThemeMode(themeMode: ThemeMode) {
        settingsDataStore.saveThemeMode(themeMode)
    }

    suspend fun saveLanguageMode(languageMode: LanguageMode) {
        settingsDataStore.saveLanguageMode(languageMode)
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingsRepository? = null

        fun getInstance(
            settingsDataStore: SettingsDataStore,
        ): SettingsRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsRepository(settingsDataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}