package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.profile.InfoNote
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings
import com.dicoding.tugas_akhir.ui.components.profile.StaticInfoItem
import com.dicoding.tugas_akhir.ui.components.profile.StaticInfoSection

@Composable
fun ProfileAboutScreen(
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            InfoNote(
                title = strings.aboutTitle,
                text = strings.aboutIntro,
            )
        }

        item {
            StaticInfoSection(title = strings.information) {
                StaticInfoItem(
                    title = strings.appVersion,
                    subtitle = "Release build",
                    icon = Icons.Outlined.Verified,
                )

                StaticInfoItem(
                    title = strings.platform,
                    subtitle = "Jetpack Compose",
                    icon = Icons.Outlined.Android,
                )

                StaticInfoItem(
                    title = strings.method,
                    subtitle = strings.purpose,
                    icon = Icons.Outlined.Description,
                )
            }
        }

        item {
            StaticInfoSection(title = strings.mainFeature) {
                StaticInfoItem(
                    title = "Cari Jadwal Kapal",
                    subtitle = "Mencari jadwal berdasarkan pelabuhan asal, tujuan, dan tanggal.",
                    icon = Icons.Outlined.CheckCircle,
                )

                StaticInfoItem(
                    title = "Booking dan Pembayaran",
                    subtitle = "Melakukan pemesanan tiket dan melihat instruksi pembayaran.",
                    icon = Icons.Outlined.CheckCircle,
                )

                StaticInfoItem(
                    title = "E-Ticket",
                    subtitle = "Menampilkan tiket digital setelah pembayaran berhasil.",
                    icon = Icons.Outlined.CheckCircle,
                )

                StaticInfoItem(
                    title = "Data Penumpang",
                    subtitle = "Menyimpan data penumpang untuk mempercepat proses booking.",
                    icon = Icons.Outlined.CheckCircle,
                )
            }
        }

        item {
            InfoNote(
                title = strings.information,
                text = strings.dataNote,
            )
        }
    }
}

@Composable
fun AboutAppScreen(
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    ProfileAboutScreen(
        onBackClick = onBackClick,
        modifier = modifier,
    )
}