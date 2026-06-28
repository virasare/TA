package com.dicoding.tugas_akhir.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary2,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEAF4FF),
    onPrimaryContainer = Primary2,

    background = Color(0xFFF7FAFC),
    onBackground = Color(0xFF102A43),

    surface = Color.White,
    onSurface = Color(0xFF102A43),

    surfaceVariant = Color(0xFFEAF4FF),
    onSurfaceVariant = Color(0xFF627D98),

    outline = Color(0xFFBCCCDC),
    outlineVariant = Color(0xFFE3EAF2),

    error = Color(0xFFD32F2F),
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF0B3558),
    primaryContainer = Color(0xFF123C5C),
    onPrimaryContainer = Color(0xFFEAF4FF),

    background = Color(0xFF0B1220),
    onBackground = Color(0xFFEAF4FF),

    surface = Color(0xFF111827),
    onSurface = Color(0xFFEAF4FF),

    surfaceVariant = Color(0xFF1F2937),
    onSurfaceVariant = Color(0xFFBCCCDC),

    outline = Color(0xFF52606D),
    outlineVariant = Color(0xFF323F4B),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

@Composable
fun Tugas_AkhirTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    textScale: Float = 1f,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = scaledTypography(textScale),
        content = content,
    )
}
