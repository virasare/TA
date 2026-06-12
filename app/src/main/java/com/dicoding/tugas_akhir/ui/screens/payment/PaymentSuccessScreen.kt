package com.dicoding.tugas_akhir.ui.screens.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.SecondaryButton
import com.dicoding.tugas_akhir.ui.theme.*

@Composable
fun PaymentSuccessScreen(
    bookingCode: String = "NKP12345",
    onSeeTicketClick: () -> Unit,
    onBackHomeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Surface(
            shape = CircleShape,
            color = Primary2
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .padding(24.dp)
                    .size(54.dp)
            )
        }

        Text(
            text = "Pembayaran Berhasil",
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Tiket berhasil diterbitkan dan dapat dilihat pada halaman Tiket Saya.",
            color = Neutral500,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
            color = White,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Neutral200),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Kode Booking",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = bookingCode,
                    color = Primary2,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                text = "Kembali ke Beranda",
                onClick = onBackHomeClick,
                modifier = Modifier.weight(1f)
            )

            PrimaryButton(
                text = "Lihat E-Ticket",
                onClick = onSeeTicketClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}