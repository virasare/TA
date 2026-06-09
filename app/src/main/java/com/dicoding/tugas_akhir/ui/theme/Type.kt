package com.dicoding.tugas_akhir.ui.theme

import com.dicoding.tugas_akhir.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val PlusJakartaSans = FontFamily(
    Font(
        resId = R.font.plus_jakarta_sans_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.plus_jakarta_sans_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.plus_jakarta_sans_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.plus_jakarta_sans_bold,
        weight = FontWeight.Bold
    )
)

private val DefaultTypography = Typography()

val Typography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = 0.1.sp
    ),
    displayMedium = DefaultTypography.displayMedium.copy(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.1.sp
    ),
    displaySmall = DefaultTypography.displaySmall.copy(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.1.sp
    ),

    headlineLarge = DefaultTypography.headlineLarge.copy(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.1.sp
    ),
    headlineMedium = DefaultTypography.headlineMedium.copy(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.1.sp
    ),
    headlineSmall = DefaultTypography.headlineSmall.copy(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.1.sp
    ),

    bodyLarge = DefaultTypography.bodyLarge.copy(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    bodyMedium = DefaultTypography.bodyMedium.copy(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp
    ),
    bodySmall = DefaultTypography.bodySmall.copy(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.1.sp
    ),

    labelLarge = DefaultTypography.labelLarge.copy(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ), // Button

    labelMedium = DefaultTypography.labelMedium.copy(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp
    ), // Badge

    labelSmall = DefaultTypography.labelSmall.copy(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp
    ) // Input Label
)