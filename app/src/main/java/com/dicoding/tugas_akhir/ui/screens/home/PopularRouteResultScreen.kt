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
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.data.dummy.dummyShipSchedules
import com.dicoding.tugas_akhir.data.dummy.filterUpcomingSchedules

@Composable
fun PopularRouteResultScreen(
    popularRoute: PopularRoute?,
    onScheduleClick: (String) -> Unit
) {
    if (popularRoute == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
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
        dummyShipSchedules
            .filterUpcomingSchedules()
            .filter { schedule ->
                val route = schedule.toRouteDirection()

                route.origin.equals(popularRoute.originCity, ignoreCase = true) &&
                        route.destination.equals(popularRoute.destinationCity, ignoreCase = true)
            }
            .sortedWith(
                compareBy<ShipSchedule> { it.price.toPriceNumber() }
                    .thenBy { it.departureDate }
                    .thenBy { it.departureTime }
            )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = popularRoute.route,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Jadwal rute populer, diurutkan dari harga termurah.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (schedules.isEmpty()) {
            item {
                LottieStateView(
                    animationFile = "empty_schedule.json",
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
                    route = schedule.route,
                    departureDate = schedule.departureDate,
                    departureTime = schedule.departureTime,
                    arrivalTime = "${schedule.arrivalDate}, ${schedule.arrivalTime}",
                    duration = schedule.duration,
                    price = schedule.price,
                    quota = schedule.quota,
                    status = schedule.status,
                    onClick = {
                        onScheduleClick(schedule.id.toString())
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

private data class RouteDirection(
    val origin: String,
    val destination: String,
)

private fun ShipSchedule.toRouteDirection(): RouteDirection {
    val parts = when {
        route.contains("â†’") -> route.split("â†’")
        route.contains("Ã¢â€ â€™") -> route.split("Ã¢â€ â€™")
        route.contains("→") -> route.split("→")
        else -> route.split("-")
    }

    return RouteDirection(
        origin = parts.getOrNull(0)?.trim().orEmpty(),
        destination = parts.getOrNull(1)?.trim().orEmpty(),
    )
}

private fun String.toPriceNumber(): Int {
    return filter { it.isDigit() }.toIntOrNull() ?: Int.MAX_VALUE
}
