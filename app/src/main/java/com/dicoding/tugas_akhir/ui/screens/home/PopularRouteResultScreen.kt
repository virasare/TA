package com.dicoding.tugas_akhir.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.core.utils.DateFormatter
import com.dicoding.tugas_akhir.core.utils.PriceFormatter
import com.dicoding.tugas_akhir.data.dummy.DummyShipScheduleApiData
import com.dicoding.tugas_akhir.data.dummy.PopularRoute
import com.dicoding.tugas_akhir.data.remote.response.ShipScheduleResponse
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleCard
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleStatus
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700

@Composable
fun PopularRouteResultScreen(
    popularRoute: PopularRoute?,
    onScheduleClick: (String) -> Unit
) {
    if (popularRoute == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background),
            contentAlignment = Alignment.Center
        ) {
            LottieStateView(
                animationFile = "empty.json",
                title = "Rute populer tidak ditemukan",
                message = "Data rute populer belum tersedia.",
            )
        }
        return
    }

    val schedules = remember(popularRoute) {
        DummyShipScheduleApiData.schedules
            .filter { schedule ->
                schedule.origin.equals(popularRoute.originCity, ignoreCase = true) &&
                        schedule.destination.equals(popularRoute.destinationCity, ignoreCase = true)
            }
            .sortedWith(
                compareBy<ShipScheduleResponse> { it.economyPrice }
                    .thenBy { it.departureDate }
                    .thenBy { it.departureTime }
            )
    }

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
                    text = popularRoute.route,
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Jadwal rute populer, diurutkan dari harga termurah.",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (schedules.isEmpty()) {
            item {
                LottieStateView(
                    animationFile = "empty.json",
                    title = "Jadwal belum tersedia",
                    message = "Belum ada jadwal untuk rute ${popularRoute.route}.",
                )
            }
        } else {
            items(
                items = schedules,
                key = { schedule -> schedule.id }
            ) { schedule ->
                ShipScheduleCard(
                    shipName = schedule.shipName,
                    route = "${schedule.origin} - ${schedule.destination}",
                    departureDate = DateFormatter.formatDate(schedule.departureDate),
                    departureTime = schedule.departureTime,
                    arrivalTime = "${DateFormatter.formatDate(schedule.arrivalDate)}, ${schedule.arrivalTime}",
                    duration = schedule.duration,
                    price = PriceFormatter.formatToRupiah(schedule.economyPrice),
                    quota = if (schedule.quota <= 0) "Habis" else "${schedule.quota} kursi",
                    status = schedule.toUiStatus(),
                    onClick = {
                        onScheduleClick(schedule.id)
                    }
                )
            }
        }
    }
}

private fun ShipScheduleResponse.toUiStatus(): ShipScheduleStatus {
    return when {
        quota <= 0 -> ShipScheduleStatus.Unavailable
        status.contains("habis", ignoreCase = true) -> ShipScheduleStatus.Unavailable
        status.contains("terbatas", ignoreCase = true) -> ShipScheduleStatus.Limited
        quota <= 10 -> ShipScheduleStatus.Limited
        else -> ShipScheduleStatus.Available
    }
}