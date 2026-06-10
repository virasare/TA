package com.dicoding.tugas_akhir.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    onSearchScheduleClick: () -> Unit,
    onMyTicketClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Home Screen")

        Button(onClick = onSearchScheduleClick) {
            Text("Cari Jadwal")
        }

        Button(onClick = onMyTicketClick) {
            Text("Tiket Saya")
        }

        Button(onClick = onNotificationClick) {
            Text("Notifikasi")
        }
    }
}