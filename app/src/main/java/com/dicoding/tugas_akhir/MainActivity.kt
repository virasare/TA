package com.dicoding.tugas_akhir

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.domain.model.ThemeMode
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings
import com.dicoding.tugas_akhir.ui.localization.getAppStrings
import com.dicoding.tugas_akhir.ui.navigation.AppNavigation
import com.dicoding.tugas_akhir.ui.theme.Tugas_AkhirTheme
import com.dicoding.tugas_akhir.ui.viewmodel.SettingsViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = ViewModelFactory.getInstance()
            )

            val settings by settingsViewModel.settingsState.collectAsStateWithLifecycle()
            val systemInDarkTheme = isSystemInDarkTheme()

            val useDarkTheme = when (settings.themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> systemInDarkTheme
            }

            val appStrings = getAppStrings(settings.languageMode)
            val view = LocalView.current
            val window = (view.context as Activity).window

            val statusBarColor = if (useDarkTheme) {
                Color(0xFF0B1220)
            } else {
                Color(0xFFF7FAFC)
            }

            SideEffect {
                window.statusBarColor = statusBarColor.toArgb()
                window.navigationBarColor = statusBarColor.toArgb()

                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = !useDarkTheme
                    isAppearanceLightNavigationBars = !useDarkTheme
                }
            }

            CompositionLocalProvider(
                LocalAppStrings provides appStrings,
            ) {
                Tugas_AkhirTheme(
                    darkTheme = useDarkTheme,
                ) {
                    AppNavigation()
                }
            }
        }
    }
}