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
import com.dicoding.tugas_akhir.ui.components.profile.DetailMenuItem
import com.dicoding.tugas_akhir.ui.components.profile.InfoNote
import com.dicoding.tugas_akhir.ui.components.profile.ProfileFormCard
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings

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
            ProfileFormCard(
                title = strings.information,
            ) {
                DetailMenuItem(
                    title = strings.appVersion,
                    subtitle = "Release build",
                    icon = Icons.Outlined.Verified,
                    onClick = {},
                )

                DetailMenuItem(
                    title = strings.platform,
                    subtitle = "Jetpack Compose",
                    icon = Icons.Outlined.Android,
                    onClick = {},
                )

                DetailMenuItem(
                    title = strings.method,
                    subtitle = strings.purpose,
                    icon = Icons.Outlined.Description,
                    onClick = {},
                )
            }
        }

        item {
            ProfileFormCard(
                title = strings.mainFeature,
            ) {
                DetailMenuItem(
                    title = "Cari Jadwal Kapal",
                    subtitle = "Mencari jadwal berdasarkan pelabuhan asal, tujuan, dan tanggal.",
                    icon = Icons.Outlined.CheckCircle,
                    onClick = {},
                )

                DetailMenuItem(
                    title = "Booking dan Pembayaran",
                    subtitle = "Melakukan pemesanan tiket dan melihat instruksi pembayaran.",
                    icon = Icons.Outlined.CheckCircle,
                    onClick = {},
                )

                DetailMenuItem(
                    title = "E-Ticket",
                    subtitle = "Menampilkan tiket digital setelah pembayaran berhasil.",
                    icon = Icons.Outlined.CheckCircle,
                    onClick = {},
                )

                DetailMenuItem(
                    title = "Data Penumpang",
                    subtitle = "Menyimpan data penumpang untuk mempercepat proses booking.",
                    icon = Icons.Outlined.CheckCircle,
                    onClick = {},
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