package com.dicoding.tugas_akhir.ui.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
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
fun PaymentFailedScreen(
    onHelpClick: () -> Unit,
    onCheckAgainClick: () -> Unit
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
            color = androidx.compose.ui.graphics.Color(0xFFE53935)
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .padding(24.dp)
                    .size(54.dp)
            )
        }

        Text(
            text = "Pembayaran Belum Berhasil",
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Pembayaran belum berhasil atau masih diproses. Silakan cek ulang status pembayaran.",
            color = Neutral500,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            color = androidx.compose.ui.graphics.Color(0xFFFFF4D8),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = androidx.compose.ui.graphics.Color(0xFFD98A00)
                )

                Text(
                    text = "Jika pembayaran sudah dilakukan, sistem mungkin membutuhkan beberapa saat untuk memperbarui status.",
                    color = Neutral700,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                text = "Hubungi Bantuan",
                onClick = onHelpClick,
                modifier = Modifier.weight(1f)
            )

            PrimaryButton(
                text = "Cek Ulang Status",
                onClick = onCheckAgainClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}