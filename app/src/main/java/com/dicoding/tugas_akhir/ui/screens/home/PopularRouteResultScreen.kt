package com.dicoding.tugas_akhir.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.PopularRoute
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.data.dummy.dummyShipSchedules
import com.dicoding.tugas_akhir.ui.components.dialog.cards.ShipScheduleCard
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700

@Composable
fun PopularRouteResultScreen(
    popularRoute: PopularRoute?,
    onScheduleClick: (Int) -> Unit
) {
    if (popularRoute == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Rute populer tidak ditemukan",
                color = Neutral700,
                style = MaterialTheme.typography.titleMedium
            )
        }
        return
    }

    val schedules = remember(popularRoute) {
        dummyShipSchedules
            .filter { schedule ->
                val routeDirection = schedule.route.toRouteDirection()

                routeDirection.origin.equals(
                    popularRoute.originCity,
                    ignoreCase = true
                ) && routeDirection.destination.equals(
                    popularRoute.destinationCity,
                    ignoreCase = true
                )
            }
            .sortedWith(
                compareBy<ShipSchedule> { schedule ->
                    schedule.price.toPriceNumber()
                }.thenBy { schedule ->
                    schedule.departureDate
                }.thenBy { schedule ->
                    schedule.departureTime
                }
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

        items(schedules) { schedule ->
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
                    onScheduleClick(schedule.id)
                }
            )
        }
    }
}

private data class RouteDirection(
    val origin: String,
    val destination: String
)

private fun String.toRouteDirection(): RouteDirection {
    val parts = split("→")

    return RouteDirection(
        origin = parts.getOrNull(0)?.trim().orEmpty(),
        destination = parts.getOrNull(1)?.trim().orEmpty()
    )
}

private fun String.toPriceNumber(): Int {
    return replace("Rp", "")
        .replace(".", "")
        .replace(",", "")
        .trim()
        .toIntOrNull() ?: Int.MAX_VALUE
}