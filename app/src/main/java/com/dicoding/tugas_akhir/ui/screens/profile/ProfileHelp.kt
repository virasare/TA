package com.dicoding.tugas_akhir.ui.screens.profile

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.profile.DetailMenuItem
import com.dicoding.tugas_akhir.ui.components.profile.InfoNote
import com.dicoding.tugas_akhir.ui.components.profile.ProfileFormCard
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings

@Composable
fun ProfileHelpScreen(
    onHelpDetailClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            InfoNote(
                title = strings.helpTitle,
                text = strings.helpIntro,
            )
        }

        item {
            ProfileFormCard(
                title = strings.guideTitle,
            ) {
                DetailMenuItem(
                    title = "Cari Jadwal Kapal",
                    subtitle = "Panduan memilih pelabuhan asal, tujuan, dan tanggal.",
                    icon = Icons.Outlined.Schedule,
                    onClick = {
                        onHelpDetailClick("schedule")
                    },
                )

                DetailMenuItem(
                    title = "Pemesanan Tiket",
                    subtitle = "Panduan memilih kelas tiket dan mengisi data penumpang.",
                    icon = Icons.Outlined.EventSeat,
                    onClick = {
                        onHelpDetailClick("booking")
                    },
                )

                DetailMenuItem(
                    title = "Pembayaran",
                    subtitle = "Panduan memilih metode pembayaran dan melihat status pembayaran.",
                    icon = Icons.Outlined.Payment,
                    onClick = {
                        onHelpDetailClick("payment")
                    },
                )

                DetailMenuItem(
                    title = "Akun dan Data Penumpang",
                    subtitle = "Panduan mengelola profil dan data penumpang tersimpan.",
                    icon = Icons.Outlined.HelpOutline,
                    onClick = {
                        onHelpDetailClick("profile")
                    },
                )
            }
        }

        item {
            ProfileFormCard(
                title = strings.contactTitle,
            ) {
                Text(
                    text = strings.contactSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                DetailMenuItem(
                    title = strings.emailSupport,
                    subtitle = "Email Support",
                    icon = Icons.Outlined.Email,
                    onClick = {
                        context.openSupportEmail(strings.emailSupport)
                    },
                )

                DetailMenuItem(
                    title = strings.phoneSupport,
                    subtitle = "WhatsApp / Call Center",
                    icon = Icons.Outlined.Call,
                    onClick = {
                        context.openWhatsAppSupport(strings.phoneSupport)
                    },
                )

                DetailMenuItem(
                    title = strings.serviceHours,
                    subtitle = "Jam Operasional",
                    icon = Icons.Outlined.SupportAgent,
                    onClick = {},
                )
            }
        }
    }
}

private fun Context.openSupportEmail(
    email: String,
) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, "Bantuan Aplikasi NusaKapal")
    }

    runCatching {
        startActivity(intent)
    }
}

private fun Context.openWhatsAppSupport(
    phoneNumber: String,
) {
    val normalizedPhone = phoneNumber
        .filter { it.isDigit() }
        .let { digits ->
            if (digits.startsWith("0")) {
                "62${digits.drop(1)}"
            } else {
                digits
            }
        }

    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://wa.me/$normalizedPhone?text=Saya%20butuh%20bantuan%20terkait%20aplikasi%20NusaKapal")
    )

    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://wa.me/$normalizedPhone")
        )
        startActivity(browserIntent)
    }
}
