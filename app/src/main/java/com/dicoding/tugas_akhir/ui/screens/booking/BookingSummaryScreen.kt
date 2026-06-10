package com.dicoding.tugas_akhir.ui.screens.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BookingSummaryScreen(
    onBackClick: () -> Unit,
    onPaymentClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Booking Summary Screen")

        Button(onClick = onPaymentClick) {
            Text("Lanjut Pembayaran")
        }

        Button(onClick = onBackClick) {
            Text("Kembali")
        }
    }
}