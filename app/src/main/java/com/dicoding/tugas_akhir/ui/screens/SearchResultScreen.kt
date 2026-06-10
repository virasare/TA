package com.dicoding.tugas_akhir.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
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
import com.dicoding.tugas_akhir.data.dummy.Port
import com.dicoding.tugas_akhir.data.dummy.dummyShipSchedules
import com.dicoding.tugas_akhir.ui.components.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.buttons.SecondaryButton
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleCard
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun SearchResultScreen(
    originPort: Port?,
    destinationPort: Port?,
    selectedDate: String,
    onScheduleClick: () -> Unit,
    onBackToHomeClick: () -> Unit,
    onSeeAllSchedulesClick: () -> Unit
) {
    val results = dummyShipSchedules.filter { schedule ->
        val originMatch = originPort?.city?.let { originCity ->
            schedule.route.contains(originCity, ignoreCase = true)
        } ?: false

        val destinationMatch = destinationPort?.city?.let { destinationCity ->
            schedule.route.contains(destinationCity, ignoreCase = true)
        } ?: false

        val dateMatch = schedule.departureDate == selectedDate

        originMatch && destinationMatch && dateMatch
    }

    when {
        originPort == null || destinationPort == null || selectedDate.isEmpty() -> {
            EmptySearchResult(
                title = "Pencarian belum lengkap",
                description = "Pilih pelabuhan asal, tujuan, dan tanggal keberangkatan terlebih dahulu agar sistem dapat menampilkan jadwal yang sesuai.",
                primaryButtonText = "Lengkapi Pencarian",
                onPrimaryClick = onBackToHomeClick,
                secondaryButtonText = "Lihat Semua Jadwal",
                onSecondaryClick = onSeeAllSchedulesClick
            )
        }

        results.isEmpty() -> {
            EmptySearchResult(
                title = "Jadwal belum tersedia",
                description = "Belum ada jadwal kapal untuk rute ${originPort.city} ke ${destinationPort.city} pada $selectedDate. Coba ubah tanggal atau lihat semua jadwal kapal yang tersedia.",
                primaryButtonText = "Ubah Pencarian",
                onPrimaryClick = onBackToHomeClick,
                secondaryButtonText = "Lihat Semua Jadwal",
                onSecondaryClick = onSeeAllSchedulesClick
            )
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${originPort.city} → ${destinationPort.city}",
                            color = Neutral700,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "$selectedDate • ${results.size} jadwal ditemukan",
                            color = Neutral500,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                items(results) { schedule ->
                    ShipScheduleCard(
                        shipName = schedule.shipName,
                        route = schedule.route,
                        departureDate = schedule.departureDate,
                        departureTime = schedule.departureTime,
                        arrivalTime = "${schedule.arrivalDate}, ${schedule.arrivalTime}",
                        duration = schedule.duration,
                        price = schedule.price,
                        quota = schedule.quota,
                        status = schedule.status,
                        onClick = onScheduleClick
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptySearchResult(
    title: String,
    description: String,
    primaryButtonText: String,
    onPrimaryClick: () -> Unit,
    secondaryButtonText: String,
    onSecondaryClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = White,
            shape = MaterialTheme.shapes.large,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    color = Primary3,
                    shape = MaterialTheme.shapes.large
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = Primary2,
                        modifier = Modifier.padding(18.dp)
                    )
                }

                Text(
                    text = title,
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = description,
                    color = Neutral500,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                PrimaryButton(
                    text = primaryButtonText,
                    onClick = onPrimaryClick
                )

                SecondaryButton(
                    text = secondaryButtonText,
                    onClick = onSecondaryClick
                )
            }
        }
    }
}