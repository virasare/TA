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
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.core.utils.DateFormatter
import com.dicoding.tugas_akhir.core.utils.PriceFormatter
import com.dicoding.tugas_akhir.domain.model.ShipSchedule
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleCard
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleStatus
import com.dicoding.tugas_akhir.ui.components.dialog.filters.ScheduleFilter
import com.dicoding.tugas_akhir.ui.components.dialog.filters.ScheduleFilterSection
import com.dicoding.tugas_akhir.ui.components.loading.ScheduleListPlaceholder
import com.dicoding.tugas_akhir.ui.state.ScheduleUiState
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White
import com.dicoding.tugas_akhir.ui.viewmodel.ScheduleViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ScheduleScreen(
    onScheduleClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    )
) {
    val scheduleUiState by viewModel.scheduleUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getUpcomingSchedules()
    }

    ScheduleScreenContent(
        scheduleUiState = scheduleUiState,
        onScheduleClick = onScheduleClick,
        modifier = modifier
    )
}

@Composable
private fun ScheduleScreenContent(
    scheduleUiState: ScheduleUiState,
    onScheduleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember {
        mutableStateOf(ScheduleFilter.All)
    }

    val schedules = remember(scheduleUiState, selectedFilter) {
        when (scheduleUiState) {
            is ScheduleUiState.Success -> {
                scheduleUiState.schedules
                    .filterWithinNextDays(days = 60)
                    .filterBySelectedFilter(selectedFilter)
                    .sortBySelectedFilter(selectedFilter)
            }

            else -> emptyList()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .testTag("schedule_screen")
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

        when (scheduleUiState) {
            is ScheduleUiState.Loading -> {
                ScheduleLoadingState()
            }

            is ScheduleUiState.Success -> {
                if (schedules.isEmpty()) {
                    EmptyScheduleState()
                } else {
                    ScheduleListContent(
                        schedules = schedules,
                        onScheduleClick = onScheduleClick
                    )
                }
            }

            is ScheduleUiState.Empty -> {
                EmptyScheduleState(
                    title = "Jadwal tidak tersedia",
                    description = scheduleUiState.message
                )
            }

            is ScheduleUiState.Error -> {
                ErrorScheduleState(
                    message = scheduleUiState.message
                )
            }
        }
    }
}

@Composable
private fun ScheduleListContent(
    schedules: List<ShipSchedule>,
    onScheduleClick: (String) -> Unit
) {
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
        items(
            items = schedules,
            key = { schedule -> schedule.id }
        ) { schedule ->
            ShipScheduleCard(
                shipName = schedule.shipName,
                route = schedule.routeText(),
                departureDate = DateFormatter.formatDate(schedule.departureDate),
                departureTime = schedule.departureTime,
                arrivalTime = "${DateFormatter.formatDate(schedule.arrivalDate)}, ${schedule.arrivalTime}",
                duration = schedule.duration,
                price = schedule.startingPriceText(),
                quota = schedule.quotaText(),
                status = schedule.toUiStatus(),
                onClick = {
                    onScheduleClick(schedule.id)
                }
            )
        }
    }
}

@Composable
private fun ScheduleLoadingState() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            top = 16.dp,
            bottom = 24.dp
        )
    ) {
        item {
            ScheduleListPlaceholder(
                itemCount = 4
            )
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
private fun EmptyScheduleState(
    title: String = "Jadwal tidak tersedia",
    description: String = "Belum ada jadwal kapal pada filter yang dipilih. Coba pilih filter lain."
) {
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
            }
        }
    }
}

@Composable
private fun ErrorScheduleState(
    message: String
) {
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
                        imageVector = Icons.Outlined.WarningAmber,
                        contentDescription = null,
                        tint = Primary2,
                        modifier = Modifier.padding(18.dp)
                    )
                }

                Text(
                    text = "Gagal memuat jadwal",
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = message,
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
            it.toUiStatus() == ShipScheduleStatus.Available
        }

        ScheduleFilter.Limited -> this.filter {
            it.toUiStatus() == ShipScheduleStatus.Limited
        }

        ScheduleFilter.Unavailable -> this.filter {
            it.toUiStatus() == ShipScheduleStatus.Unavailable
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
                    it.economyPrice
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
                    it.toUiStatus().toStatusRank()
                }
            )
        }
    }
}

private fun ShipSchedule.routeText(): String {
    return "$origin → $destination"
}

private fun ShipSchedule.startingPriceText(): String {
    return PriceFormatter.formatToRupiah(economyPrice)
}

private fun ShipSchedule.quotaText(): String {
    return if (quota <= 0) {
        "Habis"
    } else {
        "$quota kursi"
    }
}

private fun ShipSchedule.toUiStatus(): ShipScheduleStatus {
    return when {
        quota <= 0 -> ShipScheduleStatus.Unavailable

        status.contains("habis", ignoreCase = true) -> {
            ShipScheduleStatus.Unavailable
        }

        status.contains("terbatas", ignoreCase = true) -> {
            ShipScheduleStatus.Limited
        }

        quota <= 10 -> ShipScheduleStatus.Limited

        else -> ShipScheduleStatus.Available
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
            "yyyy-MM-dd",
            Locale.getDefault()
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
            .replace(":", ".")
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