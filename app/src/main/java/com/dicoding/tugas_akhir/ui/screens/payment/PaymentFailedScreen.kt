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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
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
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun PaymentFailedScreen(
    paymentId: String,
    onRetryClick: () -> Unit,
    onBackHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .testTag("payment_failed_screen"),
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
                PaymentFailedCard(
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
                    onClick = onRetryClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = White,
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Text("Cek Ulang Status")
                }

                OutlinedButton(
                    onClick = onBackHomeClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Primary2,
                        containerColor = White,
                    ),
                    border = BorderStroke(1.dp, Neutral200),
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
private fun PaymentFailedCard(
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
                animationFile = "failed.json",
                title = "Pembayaran Belum Berhasil",
                message = "Pembayaran belum terverifikasi. Silakan cek ulang status pembayaran atau coba beberapa saat lagi.",
                modifier = Modifier.fillMaxWidth(),
            )

            HorizontalDivider(color = Neutral200)

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "ID Pembayaran",
                        color = MaterialTheme.colorScheme.error,
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
                text = "Jangan lakukan pembayaran ulang sebelum status dicek kembali.",
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}