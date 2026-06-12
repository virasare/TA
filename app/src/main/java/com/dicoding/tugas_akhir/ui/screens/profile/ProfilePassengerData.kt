package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.PassengerData
import com.dicoding.tugas_akhir.ui.components.profile.*
import com.dicoding.tugas_akhir.ui.theme.Background

@Composable
fun PassengerDataScreen(
    onAddPassengerClick: () -> Unit
) {
    val passengers = listOf(
        PassengerData(
            fullName = "Vira Sare",
            nik = "5308123456789000",
            phoneNumber = "081234567890",
            gender = "Perempuan"
        ),
        PassengerData(
            fullName = "Lusia Elvira",
            nik = "5308xxxxxxxxxxxx",
            phoneNumber = "0812xxxxxxxx",
            gender = "Perempuan"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            InfoNote(
                text = "Data penumpang tersimpan dapat digunakan kembali saat melakukan pemesanan tiket."
            )
        }

        item {
            AddPassengerCard(
                onClick = onAddPassengerClick
            )
        }

        passengers.forEachIndexed { index, passenger ->
            item {
                PassengerDataCard(
                    title = "Penumpang ${index + 1}",
                    passenger = passenger
                )
            }
        }
    }
}