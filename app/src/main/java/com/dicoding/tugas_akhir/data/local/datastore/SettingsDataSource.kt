package com.dicoding.tugas_akhir.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.tugas_akhir.domain.model.LanguageMode
import com.dicoding.tugas_akhir.domain.model.ThemeMode
import com.dicoding.tugas_akhir.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.settingsDataStore by preferencesDataStore(
    name = "settings_preferences"
)

class SettingsDataStore private constructor(
    private val context: Context,
) {

    val settingsFlow: Flow<UserSettings> = context.settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserSettings(
                themeMode = preferences[THEME_MODE_KEY].toThemeMode(),
                languageMode = preferences[LANGUAGE_MODE_KEY].toLanguageMode(),
            )
        }

    suspend fun saveThemeMode(themeMode: ThemeMode) {
        context.settingsDataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeMode.name
        }
    }

    suspend fun saveLanguageMode(languageMode: LanguageMode) {
        context.settingsDataStore.edit { preferences ->
            preferences[LANGUAGE_MODE_KEY] = languageMode.name
        }
    }

    private fun String?.toThemeMode(): ThemeMode {
        return try {
            if (this == null) {
                ThemeMode.LIGHT
            } else {
                ThemeMode.valueOf(this)
            }
        } catch (exception: Exception) {
            ThemeMode.LIGHT
        }
    }

    private fun String?.toLanguageMode(): LanguageMode {
        return try {
            if (this == null) {
                LanguageMode.INDONESIAN
            } else {
                LanguageMode.valueOf(this)
            }
        } catch (exception: Exception) {
            LanguageMode.INDONESIAN
        }
    }

    companion object {
        private val THEME_MODE_KEY: Preferences.Key<String> =
            stringPreferencesKey("theme_mode")

        private val LANGUAGE_MODE_KEY: Preferences.Key<String> =
            stringPreferencesKey("language_mode")

        @Volatile
        private var INSTANCE: SettingsDataStore? = null

        fun getInstance(
            context: Context,
        ): SettingsDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsDataStore(
                    context = context.applicationContext,
                )
                INSTANCE = instance
                instance
            }
        }
    }
}