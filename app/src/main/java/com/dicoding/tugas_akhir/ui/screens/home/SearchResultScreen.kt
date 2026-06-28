package com.dicoding.tugas_akhir.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Sort
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.dicoding.tugas_akhir.data.dummy.Port
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.data.dummy.dummyShipSchedules
import com.dicoding.tugas_akhir.data.dummy.filterUpcomingSchedules
import com.dicoding.tugas_akhir.data.recomendation.findExactSchedules
import com.dicoding.tugas_akhir.data.recomendation.findRecommendedSchedules
import com.dicoding.tugas_akhir.data.recomendation.findSchedulesWithinDateRange
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleCard
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleStatus
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.SecondaryButton
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView
import java.text.SimpleDateFormat
import java.util.Locale

private enum class SearchScheduleSort(
    val label: String,
) {
    Nearest("Terdekat"),
    Cheapest("Termurah"),
    Fastest("Durasi tercepat"),
}

@Composable
fun SearchResultScreen(
    originPort: Port?,
    destinationPort: Port?,
    selectedDate: String,
    onScheduleClick: (Int) -> Unit,
    onBackToHomeClick: () -> Unit,
    onSeeAllSchedulesClick: () -> Unit
) {
    var selectedSort by remember {
        mutableStateOf(SearchScheduleSort.Nearest)
    }
    val availableSchedules = remember {
        dummyShipSchedules.filterUpcomingSchedules()
    }

    val results = remember(
        originPort,
        destinationPort,
        selectedDate,
        selectedSort,
        availableSchedules,
    ) {
        findExactSchedules(
            schedules = availableSchedules,
            originPort = originPort,
            destinationPort = destinationPort,
            selectedDate = selectedDate,
        ).sortBySearchSort(selectedSort)
    }

    val recommendations = remember(
        originPort,
        destinationPort,
        selectedDate,
        selectedSort,
        availableSchedules,
    ) {
        val sameRouteRecommendations = findSchedulesWithinDateRange(
            schedules = availableSchedules,
            originPort = originPort,
            destinationPort = destinationPort,
            selectedDate = selectedDate,
            rangeDays = 14,
        ).filterNot { schedule ->
            schedule.departureDate == selectedDate
        }

        sameRouteRecommendations.ifEmpty {
            findRecommendedSchedules(
                schedules = availableSchedules,
                originPort = originPort,
                destinationPort = destinationPort,
                selectedDate = selectedDate,
            )
        }.filterNot { schedule ->
            results.any { result -> result.id == schedule.id }
        }.sortBySearchSort(selectedSort)
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
            SearchResultRecommendationContent(
                originPort = originPort,
                destinationPort = destinationPort,
                selectedDate = selectedDate,
                selectedSort = selectedSort,
                recommendations = recommendations,
                onSortSelected = { selectedSort = it },
                onScheduleClick = onScheduleClick,
                onBackToHomeClick = onBackToHomeClick,
                onSeeAllSchedulesClick = onSeeAllSchedulesClick
            )
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SearchResultHeader(
                        originPort = originPort,
                        destinationPort = destinationPort,
                        selectedDate = selectedDate,
                        scheduleCount = results.size,
                    )
                }

                item {
                    SearchSortSection(
                        selectedSort = selectedSort,
                        onSortSelected = {
                            selectedSort = it
                        },
                    )
                }

                items(
                    items = results,
                    key = { schedule -> schedule.id },
                ) { schedule ->
                    SearchScheduleItem(
                        schedule = schedule,
                        onScheduleClick = onScheduleClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultHeader(
    originPort: Port,
    destinationPort: Port,
    selectedDate: String,
    scheduleCount: Int,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.large,
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "${originPort.city} → ${destinationPort.city}",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "$scheduleCount jadwal ditemukan",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = "Menampilkan jadwal pada $selectedDate.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun SearchSortSection(
    selectedSort: SearchScheduleSort,
    onSortSelected: (SearchScheduleSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Sort,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp),
            )

            Text(
                text = "Urutkan jadwal",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleSmall,
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(SearchScheduleSort.entries.toList()) { sort ->
                FilterChip(
                    selected = selectedSort == sort,
                    onClick = {
                        onSortSelected(sort)
                    },
                    label = {
                        Text(sort.label)
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedSort == sort,
                        borderColor = MaterialTheme.colorScheme.outlineVariant,
                        selectedBorderColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            }
        }
    }
}

@Composable
private fun SearchScheduleItem(
    schedule: ShipSchedule,
    onScheduleClick: (Int) -> Unit,
) {
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
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.large,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.large
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(18.dp)
                    )
                }

                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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

@Composable
private fun SearchResultRecommendationContent(
    originPort: Port,
    destinationPort: Port,
    selectedDate: String,
    selectedSort: SearchScheduleSort,
    recommendations: List<ShipSchedule>,
    onSortSelected: (SearchScheduleSort) -> Unit,
    onScheduleClick: (Int) -> Unit,
    onBackToHomeClick: () -> Unit,
    onSeeAllSchedulesClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillParentMaxHeight(0.36f),
                contentAlignment = Alignment.Center,
            ) {
                LottieStateView(
                    animationFile = "empty_schedule.json",
                    title = "Jadwal belum tersedia",
                    message = "Belum ada jadwal ${originPort.city} ke ${destinationPort.city} pada $selectedDate.",
                )
            }
        }

        item {
            SecondaryButton(
                text = "Ubah Pencarian",
                onClick = onBackToHomeClick,
            )
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.large,
                shadowElevation = 1.dp,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Rekomendasi 14 Hari ke Depan",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Alternatif jadwal untuk rute ${originPort.city} ke ${destinationPort.city} dengan tanggal atau kapal yang berbeda.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }

        item {
            SearchSortSection(
                selectedSort = selectedSort,
                onSortSelected = onSortSelected,
            )
        }

        if (recommendations.isEmpty()) {
            item {
                RecommendationEmptyCard(
                    destinationPort = destinationPort,
                    onSeeAllSchedulesClick = onSeeAllSchedulesClick,
                    onBackToHomeClick = onBackToHomeClick,
                )
            }
        } else {
            items(
                items = recommendations,
                key = { schedule -> schedule.id },
            ) { schedule ->
                SearchScheduleItem(
                    schedule = schedule,
                    onScheduleClick = onScheduleClick,
                )
            }
        }
    }
}

@Composable
private fun RecommendationEmptyCard(
    destinationPort: Port,
    onSeeAllSchedulesClick: () -> Unit,
    onBackToHomeClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.large,
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Rekomendasi belum tersedia",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )

            Text(
                text = "Belum ada jadwal alternatif menuju ${destinationPort.city} saat ini.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )

            PrimaryButton(
                text = "Lihat Semua Jadwal",
                onClick = onSeeAllSchedulesClick,
            )

            SecondaryButton(
                text = "Ubah Pencarian",
                onClick = onBackToHomeClick,
            )
        }
    }
}

private fun List<ShipSchedule>.sortBySearchSort(
    sort: SearchScheduleSort,
): List<ShipSchedule> {
    return when (sort) {
        SearchScheduleSort.Nearest -> sortedWith(
            compareBy<ShipSchedule> { it.status.toAvailabilityRank() }
                .thenBy { it.departureDate.toDateMillis() ?: Long.MAX_VALUE }
                .thenBy { it.departureTime.toTimeRank() }
        )

        SearchScheduleSort.Cheapest -> sortedWith(
            compareBy<ShipSchedule> { it.status.toAvailabilityRank() }
                .thenBy { it.price.toPriceNumber() }
                .thenBy { it.departureDate.toDateMillis() ?: Long.MAX_VALUE }
        )

        SearchScheduleSort.Fastest -> sortedWith(
            compareBy<ShipSchedule> { it.status.toAvailabilityRank() }
                .thenBy { it.duration.toDurationMinutes() }
                .thenBy { it.departureDate.toDateMillis() ?: Long.MAX_VALUE }
        )
    }
}

private fun ShipScheduleStatus.toAvailabilityRank(): Int {
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
    val cleanTime = replace("WITA", "")
        .replace("WIB", "")
        .replace("WIT", "")
        .trim()

    val parts = cleanTime.split(".")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: return Int.MAX_VALUE
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0

    return hour * 60 + minute
}

private fun String.toPriceNumber(): Int {
    return filter { it.isDigit() }.toIntOrNull() ?: Int.MAX_VALUE
}

private fun String.toDurationMinutes(): Int {
    val cleanText = replace("±", "")
        .replace("+/-", "")
        .trim()
        .lowercase(Locale.getDefault())

    val number = cleanText.filter { it.isDigit() }.toIntOrNull() ?: return Int.MAX_VALUE

    return when {
        cleanText.contains("hari") -> number * 24 * 60
        cleanText.contains("jam") -> number * 60
        else -> number
    }
}
