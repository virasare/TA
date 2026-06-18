package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.profile.InfoNote
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings
import com.dicoding.tugas_akhir.ui.components.profile.StaticInfoItem
import com.dicoding.tugas_akhir.ui.components.profile.StaticInfoSection
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.MaterialTheme
import com.dicoding.tugas_akhir.ui.components.profile.ProfileFormCard
import com.dicoding.tugas_akhir.ui.components.profile.StepCard

@Composable
fun ProfileSecurityScreen(
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            InfoNote(
                title = strings.securityTitle,
                text = strings.securityIntro,
            )
        }

        item {
            StaticInfoSection(title = "Status Akun") {
                StaticInfoItem(
                    title = strings.loginStatus,
                    subtitle = strings.accountActive,
                    icon = Icons.Outlined.VerifiedUser,
                )

                StaticInfoItem(
                    title = strings.loginProvider,
                    subtitle = strings.googleAccount,
                    icon = Icons.Outlined.Security,
                )

                StaticInfoItem(
                    title = strings.protectedFeature,
                    subtitle = strings.protectedFeatureDesc,
                    icon = Icons.Outlined.Lock,
                )
            }
        }

        item {
            StaticInfoSection(title = "Perlindungan Data") {
                StaticInfoItem(
                    title = "Data Penumpang",
                    subtitle = "Gunakan data penumpang hanya untuk kebutuhan pemesanan tiket.",
                    icon = Icons.Outlined.PrivacyTip,
                )

                StaticInfoItem(
                    title = "Kode Pembayaran",
                    subtitle = "Jangan bagikan kode pembayaran atau bukti transaksi ke orang lain.",
                    icon = Icons.Outlined.Key,
                )

                StaticInfoItem(
                    title = "Perangkat Bersama",
                    subtitle = "Logout setelah menggunakan aplikasi di perangkat yang bukan milik pribadi.",
                    icon = Icons.Outlined.Devices,
                )
            }
        }

        item {
            ProfileFormCard(
                title = "Tips Keamanan",
            ) {
                StepCard(
                    number = 1,
                    title = "Gunakan akun pribadi",
                    description = "Masuk menggunakan akun yang hanya kamu gunakan sendiri.",
                )

                StepCard(
                    number = 2,
                    title = "Periksa detail transaksi",
                    description = "Pastikan nama kapal, tanggal, rute, dan nominal pembayaran sudah benar.",
                )

                StepCard(
                    number = 3,
                    title = "Simpan e-ticket dengan aman",
                    description = "E-ticket berisi informasi perjalanan yang sebaiknya tidak dibagikan sembarangan.",
                )
            }
        }

        item {
            InfoNote(
                title = "Peringatan",
                text = "Aplikasi tidak pernah meminta password, PIN, atau kode OTP melalui chat pribadi.",
            )
        }
    }
}