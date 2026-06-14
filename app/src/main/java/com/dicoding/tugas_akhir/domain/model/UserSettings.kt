package com.dicoding.tugas_akhir.domain.model

data class UserSettings(
    val themeMode: ThemeMode = ThemeMode.LIGHT,
    val languageMode: LanguageMode = LanguageMode.INDONESIAN,
)

enum class ThemeMode(
    val label: String,
    val description: String,
) {
    LIGHT(
        label = "Terang",
        description = "Gunakan tampilan terang.",
    ),
    SYSTEM(
        label = "Ikuti Sistem",
        description = "Tema mengikuti pengaturan perangkat.",
    ),
    DARK(
        label = "Gelap",
        description = "Gunakan tampilan gelap.",
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