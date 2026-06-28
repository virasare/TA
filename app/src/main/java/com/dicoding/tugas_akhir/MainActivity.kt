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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.PathInterpolator
import androidx.core.splashscreen.SplashScreenViewProvider

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            playSplashExitAnimation(splashScreenViewProvider)
        }

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

            val statusBarColor = Color(0xFF0B1F3A)

            val navigationBarColor = if (useDarkTheme) {
                Color(0xFF0B1220)
            } else {
                Color(0xFFF7FAFC)
            }

            SideEffect {
                window.statusBarColor = statusBarColor.toArgb()
                window.navigationBarColor = navigationBarColor.toArgb()

                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = !useDarkTheme
                }
            }

            CompositionLocalProvider(
                LocalAppStrings provides appStrings,
            ) {
                Tugas_AkhirTheme(
                    darkTheme = useDarkTheme,
                    textScale = settings.textSizeMode.scale,
                ) {
                    AppNavigation()
                }
            }
        }
    }

    private fun playSplashExitAnimation(
        splashScreenViewProvider: SplashScreenViewProvider,
    ) {
        val iconAnimation = ObjectAnimator.ofPropertyValuesHolder(
            splashScreenViewProvider.iconView,
            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0.86f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.86f),
            PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f),
        )

        val splashFade = ObjectAnimator.ofFloat(
            splashScreenViewProvider.view,
            View.ALPHA,
            1f,
            0f,
        )

        AnimatorSet().apply {
            playTogether(iconAnimation, splashFade)
            duration = 320L
            interpolator = PathInterpolator(0.2f, 0f, 0f, 1f)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    splashScreenViewProvider.remove()
                }
            })
            start()
        }
    }
}
