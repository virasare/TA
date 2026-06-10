package com.dicoding.tugas_akhir.ui.screens.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ScheduleDetailScreen(
    onBackClick: () -> Unit,
    onBookTicketClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Schedule Detail Screen")

        Button(onClick = onBookTicketClick) {
            Text("Pesan Tiket")
        }

        Button(onClick = onBackClick) {
            Text("Kembali")
        }
    }
}