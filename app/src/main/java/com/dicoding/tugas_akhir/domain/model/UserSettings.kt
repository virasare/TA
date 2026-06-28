package com.dicoding.tugas_akhir.domain.model

data class UserSettings(
    val themeMode: ThemeMode = ThemeMode.LIGHT,
    val languageMode: LanguageMode = LanguageMode.INDONESIAN,
    val textSizeMode: TextSizeMode = TextSizeMode.NORMAL,
)

enum class ThemeMode(
    val label: String,
    val description: String,
) {
    LIGHT(
        label = "Terang",
        description = "Gunakan tampilan terang.",
    ),
    DARK(
        label = "Gelap",
        description = "Gunakan tampilan gelap.",
    ),
    SYSTEM(
        label = "Ikuti Sistem",
        description = "Tema mengikuti pengaturan perangkat.",
    ),
}

enum class LanguageMode(
    val label: String,
    val description: String,
) {
    INDONESIAN(
        label = "Bahasa Indonesia",
        description = "Gunakan Bahasa Indonesia di aplikasi.",
    ),
    ENGLISH(
        label = "English",
        description = "Use English in the application.",
    ),
}

enum class TextSizeMode(
    val scale: Float,
) {
    SMALL(scale = 0.92f),
    NORMAL(scale = 1f),
    LARGE(scale = 1.12f),
}
