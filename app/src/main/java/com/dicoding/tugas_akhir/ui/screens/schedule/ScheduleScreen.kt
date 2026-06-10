package com.dicoding.tugas_akhir.ui.screens.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.data.dummy.dummyShipSchedules
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleCard
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleStatus
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Locale
import com.dicoding.tugas_akhir.ui.components.filters.ScheduleFilter
import com.dicoding.tugas_akhir.ui.components.filters.ScheduleFilterSection

@Composable
fun ScheduleScreen(
    onScheduleClick: (Int) -> Unit
) {
    var selectedFilter by remember {
        mutableStateOf(ScheduleFilter.All)
    }

    val schedules = remember(selectedFilter) {
        dummyShipSchedules
            .filterWithinNextDays(days = 60)
            .filterBySelectedFilter(selectedFilter)
            .sortBySelectedFilter(selectedFilter)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        ScheduleHeader(
            totalSchedule = schedules.size
        )

        ScheduleFilterSection(
            selectedFilter = selectedFilter,
            onFilterSelected = {
                selectedFilter = it
            }
        )

        if (schedules.isEmpty()) {
            EmptyScheduleState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = 16.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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
    }
}

@Composable
private fun ScheduleHeader(
    totalSchedule: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Jadwal Kapal Terdekat",
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Menampilkan jadwal kapal dalam 60 hari ke depan. $totalSchedule jadwal ditemukan.",
            color = Neutral500,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun EmptyScheduleState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = White,
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(1.dp, Neutral200)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    color = Primary3,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.EventBusy,
                        contentDescription = null,
                        tint = Primary2,
                        modifier = Modifier.padding(18.dp)
                    )
                }

                Text(
                    text = "Jadwal tidak tersedia",
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Belum ada jadwal kapal pada filter yang dipilih. Coba pilih filter lain.",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun List<ShipSchedule>.filterWithinNextDays(
    days: Int
): List<ShipSchedule> {
    val nowMillis = System.currentTimeMillis()
    val maxMillis = nowMillis + days * 24L * 60L * 60L * 1000L

    return filter { schedule ->
        val departureMillis = schedule.departureDate.toDateMillis()

        departureMillis != null &&
                departureMillis >= nowMillis &&
                departureMillis <= maxMillis
    }
}

private fun List<ShipSchedule>.filterBySelectedFilter(
    filter: ScheduleFilter
): List<ShipSchedule> {
    return when (filter) {
        ScheduleFilter.All -> this
        ScheduleFilter.Available -> this.filter {
            it.status == ShipScheduleStatus.Available
        }
        ScheduleFilter.Limited -> this.filter {
            it.status == ShipScheduleStatus.Limited
        }
        ScheduleFilter.Unavailable -> this.filter {
            it.status == ShipScheduleStatus.Unavailable
        }
        ScheduleFilter.Cheapest -> this
    }
}

private fun List<ShipSchedule>.sortBySelectedFilter(
    filter: ScheduleFilter
): List<ShipSchedule> {
    return when (filter) {
        ScheduleFilter.Cheapest -> {
            sortedWith(
                compareBy<ShipSchedule> {
                    it.price.toPriceNumber()
                }.thenBy {
                    it.departureDate.toDateMillis() ?: Long.MAX_VALUE
                }.thenBy {
                    it.departureTime.toTimeRank()
                }
            )
        }

        else -> {
            sortedWith(
                compareBy<ShipSchedule> {
                    it.departureDate.toDateMillis() ?: Long.MAX_VALUE
                }.thenBy {
                    it.departureTime.toTimeRank()
                }.thenBy {
                    it.status.toStatusRank()
                }
            )
        }
    }
}

private fun ShipScheduleStatus.toStatusRank(): Int {
    return when (this) {
        ShipScheduleStatus.Available -> 0
        ShipScheduleStatus.Limited -> 1
        ShipScheduleStatus.Unavailable -> 2
    }
}

private fun String.toDateMillis(): Long? {
    return try {
        val formatter = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.forLanguageTag("id-ID")
        )

        formatter.parse(this)?.time
    } catch (e: Exception) {
        null
    }
}

private fun String.toTimeRank(): Int {
    return try {
        val cleanTime = this
            .replace("WITA", "")
            .replace("WIB", "")
            .replace("WIT", "")
            .trim()

        val parts = cleanTime.split(".")

        val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0

        hour * 60 + minute
    } catch (e: Exception) {
        Int.MAX_VALUE
    }
}

private fun String.toPriceNumber(): Int {
    return replace("Rp", "")
        .replace(".", "")
        .replace(",", "")
        .trim()
        .toIntOrNull() ?: Int.MAX_VALUE
}