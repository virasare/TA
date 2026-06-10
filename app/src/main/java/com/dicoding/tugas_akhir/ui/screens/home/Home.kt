package com.dicoding.tugas_akhir.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.rememberDatePickerState
import com.dicoding.tugas_akhir.data.dummy.PopularRoute
import com.dicoding.tugas_akhir.data.dummy.Port
import com.dicoding.tugas_akhir.data.dummy.popularRoutes
import com.dicoding.tugas_akhir.ui.components.PopularRouteSection
import com.dicoding.tugas_akhir.ui.components.cards.SearchScheduleCard
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    originPort: Port?,
    destinationPort: Port?,
    selectedDate: String,
    onOriginClick: () -> Unit,
    onDestinationClick: () -> Unit,
    onDateSelected: (String) -> Unit,
    onSearchScheduleClick: () -> Unit,
    onPopularRouteClick: (PopularRoute) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val canSearch = originPort != null &&
            destinationPort != null &&
            selectedDate.isNotEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            HomeGreeting()

            SearchScheduleCard(
                originText = originPort?.name ?: "Pilih Kota Asal",
                destinationText = destinationPort?.name ?: "Pilih Tujuan",
                dateText = selectedDate.ifEmpty { "Pilih Tanggal" },
                canSearch = canSearch,
                onOriginClick = onOriginClick,
                onDestinationClick = onDestinationClick,
                onDateClick = {
                    showDatePicker = true
                },
                onSearchClick = onSearchScheduleClick
            )

            PopularRouteSection(
                routes = popularRoutes,
                onRouteClick = { route ->
                    onPopularRouteClick(route)
                }
            )
        }

        if (showDatePicker) {
            HomeDatePickerDialog(
                onDismiss = {
                    showDatePicker = false
                },
                onDateSelected = { date ->
                    onDateSelected(date)
                    showDatePicker = false
                }
            )
        }
    }
}

@Composable
private fun HomeGreeting() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Mau berangkat ke mana?",
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Cari jadwal kapal dan lihat detail perjalanan dengan mudah.",
            color = Neutral500,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeDatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis

                    if (selectedMillis != null) {
                        onDateSelected(selectedMillis.toFormattedDate())
                    } else {
                        onDismiss()
                    }
                }
            ) {
                Text("Pilih")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Batal")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

private fun Long.toFormattedDate(): String {
    val formatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.forLanguageTag("id-ID")
    )

    return formatter.format(Date(this))
}
