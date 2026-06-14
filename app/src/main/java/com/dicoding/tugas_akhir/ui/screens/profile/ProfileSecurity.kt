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
import com.dicoding.tugas_akhir.ui.components.profile.DetailMenuItem
import com.dicoding.tugas_akhir.ui.components.profile.InfoNote
import com.dicoding.tugas_akhir.ui.components.profile.ProfileFormCard
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings

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
            ProfileFormCard(
                title = strings.security,
            ) {
                DetailMenuItem(
                    title = strings.loginStatus,
                    subtitle = strings.accountActive,
                    icon = Icons.Outlined.VerifiedUser,
                    onClick = {},
                )

                DetailMenuItem(
                    title = strings.loginProvider,
                    subtitle = strings.googleAccount,
                    icon = Icons.Outlined.Security,
                    onClick = {},
                )

                DetailMenuItem(
                    title = strings.protectedFeature,
                    subtitle = strings.protectedFeatureDesc,
                    icon = Icons.Outlined.Lock,
                    onClick = {},
                )
            }
        }
    }
}