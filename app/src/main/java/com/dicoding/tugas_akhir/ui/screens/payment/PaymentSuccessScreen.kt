package com.dicoding.tugas_akhir.ui.screens.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.OutlinedButtonDefaults
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun PaymentSuccessScreen(
    paymentId: String,
    onViewTicketClick: () -> Unit,
    onBackHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .testTag("payment_success_screen"),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 28.dp,
                bottom = 24.dp,
            ),
        ) {
            item {
                PaymentResultCard(
                    animationFile = "success.json",
                    title = "Pembayaran Berhasil",
                    message = "Tiket berhasil diterbitkan. Kamu bisa melihat e-ticket pada halaman Tiket Saya.",
                    paymentId = paymentId,
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = White,
            border = BorderStroke(1.dp, Neutral200),
            shadowElevation = 8.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Button(
                    onClick = onViewTicketClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary2,
                        contentColor = White,
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ConfirmationNumber,
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Text("Lihat Tiket")
                }

                OutlinedButton(
                    onClick = onBackHomeClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Primary2,
                        containerColor = White,
                    ),
                    border = BorderStroke(1.dp, Primary2),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Text("Kembali ke Beranda")
                }
            }
        }
    }
}

@Composable
private fun PaymentResultCard(
    animationFile: String,
    title: String,
    message: String,
    paymentId: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 3.dp,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            LottieStateView(
                animationFile = animationFile,
                title = title,
                message = message,
                modifier = Modifier.fillMaxWidth(),
            )

            HorizontalDivider(color = Neutral200)

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Primary3,
                border = BorderStroke(1.dp, Neutral200),
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "ID Pembayaran",
                        color = Neutral500,
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Text(
                        text = paymentId,
                        color = Neutral700,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Text(
                text = "Simpan informasi ini apabila dibutuhkan untuk bantuan pelanggan.",
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}