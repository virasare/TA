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

@Composable
fun ProfileSecurityScreen(
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            InfoNote(
                title = strings.securityTitle,
                text = strings.securityIntro,
            )
        }

        item {
            StaticInfoSection(title = strings.security) {
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
            InfoNote(
                title = "Tips Keamanan",
                text = "Gunakan akun pribadi, jangan bagikan e-ticket atau kode pembayaran, dan selalu logout jika memakai perangkat bersama.",
            )
        }
    }
}