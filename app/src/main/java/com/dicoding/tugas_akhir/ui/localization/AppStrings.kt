package com.dicoding.tugas_akhir.ui.localization

import androidx.compose.runtime.staticCompositionLocalOf
import com.dicoding.tugas_akhir.domain.model.LanguageMode
import com.dicoding.tugas_akhir.domain.model.ThemeMode

data class AppStrings(
    val account: String,
    val application: String,
    val cancel: String,
    val logoutDialogTitle: String,
    val logoutDialogMessage: String,
    val profileTitle: String,
    val editProfile: String,
    val editProfileSubtitle: String,
    val passengerData: String,
    val passengerDataSubtitle: String,
    val settings: String,
    val settingsSubtitle: String,
    val help: String,
    val helpSubtitle: String,
    val aboutApp: String,
    val aboutAppSubtitle: String,
    val security: String,
    val securitySubtitle: String,
    val logout: String,

    val preferences: String,
    val information: String,
    val language: String,
    val languageSubtitle: String,
    val theme: String,
    val themeSubtitle: String,

    val indonesian: String,
    val indonesianDesc: String,
    val english: String,
    val englishDesc: String,
    val languageNote: String,

    val lightTheme: String,
    val lightThemeDesc: String,
    val darkTheme: String,
    val darkThemeDesc: String,
    val systemTheme: String,
    val systemThemeDesc: String,
    val themeNote: String,

    val helpTitle: String,
    val helpIntro: String,
    val guideTitle: String,
    val contactTitle: String,
    val contactSubtitle: String,
    val emailSupport: String,
    val phoneSupport: String,
    val serviceHours: String,

    val aboutTitle: String,
    val aboutIntro: String,
    val appVersion: String,
    val platform: String,
    val method: String,
    val purpose: String,
    val mainFeature: String,
    val dataNote: String,

    val securityTitle: String,
    val securityIntro: String,
    val loginStatus: String,
    val accountActive: String,
    val loginProvider: String,
    val googleAccount: String,
    val protectedFeature: String,
    val protectedFeatureDesc: String,
)

val IndonesianStrings = AppStrings(
    account = "Akun",
    application = "Aplikasi",
    cancel = "Batal",
    logoutDialogTitle = "Keluar dari akun?",
    logoutDialogMessage = "Kamu perlu masuk kembali untuk booking, pembayaran, melihat tiket, notifikasi, dan profil.",
    profileTitle = "Profil Saya",
    editProfile = "Edit Profil",
    editProfileSubtitle = "Ubah nama, foto, atau informasi akun.",
    passengerData = "Data Penumpang",
    passengerDataSubtitle = "Kelola data penumpang yang sering digunakan.",
    settings = "Pengaturan",
    settingsSubtitle = "Atur bahasa, tema, dan preferensi aplikasi.",
    help = "Bantuan",
    helpSubtitle = "Lihat panduan penggunaan dan kontak bantuan.",
    aboutApp = "Tentang Aplikasi",
    aboutAppSubtitle = "Informasi versi, fitur, dan tujuan aplikasi.",
    security = "Keamanan Akun",
    securitySubtitle = "Lihat status login dan akses fitur akun.",
    logout = "Keluar Akun",

    preferences = "Preferensi",
    information = "Informasi",
    language = "Bahasa",
    languageSubtitle = "Pilih bahasa yang digunakan di aplikasi.",
    theme = "Tema",
    themeSubtitle = "Atur tampilan terang, gelap, atau mengikuti sistem.",

    indonesian = "Bahasa Indonesia",
    indonesianDesc = "Gunakan Bahasa Indonesia di aplikasi.",
    english = "English",
    englishDesc = "Use English in the application.",
    languageNote = "Perubahan bahasa akan langsung diterapkan pada halaman yang sudah menggunakan sistem bahasa aplikasi.",

    lightTheme = "Terang",
    lightThemeDesc = "Gunakan tampilan terang sebagai default aplikasi.",
    darkTheme = "Gelap",
    darkThemeDesc = "Gunakan tampilan gelap untuk kenyamanan malam hari.",
    systemTheme = "Ikuti Sistem",
    systemThemeDesc = "Tema mengikuti pengaturan perangkat.",
    themeNote = "Tema akan mengubah warna background, teks, komponen, navigation bar, dan status bar.",

    helpTitle = "Pusat Bantuan",
    helpIntro = "Temukan panduan penggunaan aplikasi dan kontak bantuan dummy untuk kebutuhan pengujian.",
    guideTitle = "Panduan Penggunaan",
    contactTitle = "Kontak Bantuan",
    contactSubtitle = "Hubungi bantuan jika mengalami kendala saat menggunakan aplikasi.",
    emailSupport = "support@example.com",
    phoneSupport = "+62 812-0000-0000",
    serviceHours = "Senin - Jumat, 09.00 - 17.00 WITA",

    aboutTitle = "Tentang Aplikasi",
    aboutIntro = "Aplikasi ini dirancang untuk membantu pengguna mencari jadwal kapal, melakukan pemesanan, pembayaran, dan melihat e-ticket secara lebih mudah.",
    appVersion = "Versi 1.0.0",
    platform = "Android",
    method = "User Centered Design",
    purpose = "Tugas Akhir",
    mainFeature = "Fitur Utama",
    dataNote = "Data jadwal, pembayaran, dan tiket masih menggunakan dummy data untuk kebutuhan pengembangan tugas akhir.",

    securityTitle = "Keamanan Akun",
    securityIntro = "Halaman ini menampilkan status login dan fitur yang membutuhkan akun.",
    loginStatus = "Status Login",
    accountActive = "Akun sedang aktif",
    loginProvider = "Metode Login",
    googleAccount = "Google Account",
    protectedFeature = "Fitur Dilindungi",
    protectedFeatureDesc = "Booking, pembayaran, tiket saya, notifikasi, dan profil hanya dapat diakses setelah login.",
)

val EnglishStrings = AppStrings(
    account = "Account",
    application = "Application",
    cancel = "Cancel",
    logoutDialogTitle = "Sign out from your account?",
    logoutDialogMessage = "You need to sign in again to book tickets, complete payments, view tickets, notifications, and profile.",
    profileTitle = "My Profile",
    editProfile = "Edit Profile",
    editProfileSubtitle = "Update your name, photo, or account information.",
    passengerData = "Passenger Data",
    passengerDataSubtitle = "Manage frequently used passenger information.",
    settings = "Settings",
    settingsSubtitle = "Manage language, theme, and app preferences.",
    help = "Help",
    helpSubtitle = "View usage guides and support contact.",
    aboutApp = "About App",
    aboutAppSubtitle = "View version, features, and app information.",
    security = "Account Security",
    securitySubtitle = "View login status and account access.",
    logout = "Sign Out",

    preferences = "Preferences",
    information = "Information",
    language = "Language",
    languageSubtitle = "Choose the language used in the app.",
    theme = "Theme",
    themeSubtitle = "Choose light, dark, or system theme.",

    indonesian = "Bahasa Indonesia",
    indonesianDesc = "Use Indonesian in the application.",
    english = "English",
    englishDesc = "Use English in the application.",
    languageNote = "Language changes will be applied immediately to pages that already use the app language system.",

    lightTheme = "Light",
    lightThemeDesc = "Use light mode as the default app appearance.",
    darkTheme = "Dark",
    darkThemeDesc = "Use dark mode for comfortable night usage.",
    systemTheme = "System Default",
    systemThemeDesc = "Follow your device theme setting.",
    themeNote = "Theme changes will affect background, text, components, navigation bar, and status bar.",

    helpTitle = "Help Center",
    helpIntro = "Find app usage guides and dummy support contact for testing purposes.",
    guideTitle = "Usage Guide",
    contactTitle = "Support Contact",
    contactSubtitle = "Contact support if you experience issues while using the app.",
    emailSupport = "support@example.com",
    phoneSupport = "+62 812-0000-0000",
    serviceHours = "Monday - Friday, 09.00 - 17.00 WITA",

    aboutTitle = "About App",
    aboutIntro = "This application is designed to help users search ship schedules, make bookings, complete payments, and view e-tickets more easily.",
    appVersion = "Version 1.0.0",
    platform = "Android",
    method = "User Centered Design",
    purpose = "Final Project",
    mainFeature = "Main Features",
    dataNote = "Schedule, payment, and ticket data still use dummy data for final project development.",

    securityTitle = "Account Security",
    securityIntro = "This page shows your login status and account-protected features.",
    loginStatus = "Login Status",
    accountActive = "Account is active",
    loginProvider = "Login Method",
    googleAccount = "Google Account",
    protectedFeature = "Protected Features",
    protectedFeatureDesc = "Booking, payment, my tickets, notifications, and profile can only be accessed after login.",
)

fun getAppStrings(languageMode: LanguageMode): AppStrings {
    return when (languageMode) {
        LanguageMode.INDONESIAN -> IndonesianStrings
        LanguageMode.ENGLISH -> EnglishStrings
    }
}

fun ThemeMode.getLabel(strings: AppStrings): String {
    return when (this) {
        ThemeMode.LIGHT -> strings.lightTheme
        ThemeMode.DARK -> strings.darkTheme
        ThemeMode.SYSTEM -> strings.systemTheme
    }
}

fun ThemeMode.getDescription(strings: AppStrings): String {
    return when (this) {
        ThemeMode.LIGHT -> strings.lightThemeDesc
        ThemeMode.DARK -> strings.darkThemeDesc
        ThemeMode.SYSTEM -> strings.systemThemeDesc
    }
}

val LocalAppStrings = staticCompositionLocalOf {
    IndonesianStrings
}