package com.dicoding.tugas_akhir.ui.screens.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PaymentScreen(
    onBackClick: () -> Unit,
    onPaymentSuccessClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Payment Screen")

        Button(onClick = onPaymentSuccessClick) {
            Text("Simulasi Pembayaran Berhasil")
        }

        Button(onClick = onBackClick) {
            Text("Kembali")
        }
    }
}