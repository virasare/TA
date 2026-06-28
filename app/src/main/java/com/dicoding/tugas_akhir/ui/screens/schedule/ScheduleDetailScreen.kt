package com.dicoding.tugas_akhir.ui.screens.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DirectionsBoat
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Sailing
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.core.utils.DateFormatter
import com.dicoding.tugas_akhir.core.utils.PriceFormatter
import com.dicoding.tugas_akhir.domain.model.ShipSchedule
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleStatus
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.dialog.feedback.BadgeVariant
import com.dicoding.tugas_akhir.ui.components.dialog.feedback.InfoBox
import com.dicoding.tugas_akhir.ui.components.dialog.feedback.InfoBoxVariant
import com.dicoding.tugas_akhir.ui.components.dialog.feedback.StatusBadge
import com.dicoding.tugas_akhir.ui.components.loading.ScheduleDetailPlaceholder
import com.dicoding.tugas_akhir.ui.state.ScheduleDetailUiState
import com.dicoding.tugas_akhir.ui.viewmodel.ScheduleViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

private data class TicketClass(
    val name: String,
    val description: String,
    val price: String,
    val quota: String,
    val facilities: String,
)

@Composable
fun ScheduleDetailScreen(
    scheduleId: String,
    onBackClick: () -> Unit,
    onBookTicketClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    )
) {
    val detailUiState by viewModel.scheduleDetailUiState.collectAsStateWithLifecycle()

    LaunchedEffect(scheduleId) {
        viewModel.getScheduleDetail(scheduleId)
    }

    when (val state = detailUiState) {
        is ScheduleDetailUiState.Loading -> {
            ScheduleDetailLoadingState(
                modifier = modifier
            )
        }

        is ScheduleDetailUiState.Success -> {
            ScheduleDetailContent(
                schedule = state.schedule,
                onBookTicketClick = onBookTicketClick,
                modifier = modifier
            )
        }

        is ScheduleDetailUiState.Error -> {
            ScheduleNotFoundState(
                message = state.message
            )
        }
    }
}

@Composable
private fun ScheduleDetailContent(
    schedule: ShipSchedule,
    onBookTicketClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val routeDirection = RouteDirection(
        origin = schedule.origin,
        destination = schedule.destination
    )

    val ticketClasses = schedule.toTicketClasses()

    val facilities = schedule.facilities.ifEmpty {
        listOf(
            "Area Bersantai",
            "Mushola",
            "Toilet",
            "Klinik Kesehatan",
            "Area Bermain Anak",
            "Ruang Laktasi",
            "Ruang Baca",
            "Kantin"
        )
    }

    val uiStatus = schedule.toUiStatus()
    val isTicketAvailable = uiStatus != ShipScheduleStatus.Unavailable

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                ScheduleDetailHeroCard(
                    schedule = schedule
                )
            }

            item {
                ScheduleRouteInfoCard(
                    originCity = routeDirection.origin,
                    destinationCity = routeDirection.destination,
                    departureDate = DateFormatter.formatDate(schedule.departureDate),
                    departureTime = schedule.departureTime,
                    arrivalDate = DateFormatter.formatDate(schedule.arrivalDate),
                    arrivalTime = schedule.arrivalTime,
                    duration = schedule.duration
                )
            }

            item {
                TravelDetailCard(
                    schedule = schedule,
                    routeDirection = routeDirection,
                )
            }

            item {
                TicketInformationCard(
                    price = schedule.startingPriceText(),
                    quota = schedule.quotaText(),
                    status = uiStatus
                )
            }

            item {
                TicketClassCard(
                    ticketClasses = ticketClasses
                )
            }

            item {
                FacilitySection(
                    facilities = facilities
                )
            }

            item {
                InfoBox(
                    title = "Ketentuan tiket",
                    description = "Refund dan reschedule tersedia sesuai ketentuan tiket. Pastikan data penumpang sudah benar sebelum melakukan pembayaran.",
                    variant = InfoBoxVariant.Info,
                    iconRes = null
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shadowElevation = 8.dp
        ) {
            PrimaryButton(
                text = if (isTicketAvailable) "Pesan Tiket" else "Tiket Habis",
                onClick = {
                    onBookTicketClick(schedule.id)
                },
                enabled = isTicketAvailable,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun ScheduleDetailLoadingState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScheduleDetailPlaceholder(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ScheduleDetailHeroCard(
    schedule: ShipSchedule
) {
    val colors = MaterialTheme.colorScheme
    val uiStatus = schedule.toUiStatus()
    val badgeText = when (uiStatus) {
        ShipScheduleStatus.Available -> "Tersedia"
        ShipScheduleStatus.Limited -> "Terbatas"
        ShipScheduleStatus.Unavailable -> "Habis"
    }

        val badgeVariant = when (uiStatus) {
        ShipScheduleStatus.Available -> BadgeVariant.Success
        ShipScheduleStatus.Limited -> BadgeVariant.Warning
        ShipScheduleStatus.Unavailable -> BadgeVariant.Error
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = colors.surface,
        border = BorderStroke(1.dp, colors.outlineVariant),
        shadowElevation = 3.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colors.primaryContainer.copy(alpha = 0.76f),
                            colors.surface,
                            colors.surface
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        modifier = Modifier.size(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = colors.surface,
                        border = BorderStroke(1.dp, colors.outlineVariant),
                        shadowElevation = 1.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DirectionsBoat,
                                contentDescription = null,
                                tint = colors.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = schedule.shipName,
                            color = colors.onSurface,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = colors.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = schedule.routeText(),
                                color = colors.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    StatusBadge(
                        text = badgeText,
                        variant = badgeVariant
                    )
                }

                Divider(color = colors.outlineVariant)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HeroMiniInfo(
                        label = "Durasi",
                        value = schedule.duration,
                        icon = Icons.Outlined.AccessTime
                    )

                    HeroMiniInfo(
                        label = "Harga mulai",
                        value = schedule.startingPriceText(),
                        icon = Icons.Outlined.Payments
                    )

                    HeroMiniInfo(
                        label = "Kuota",
                        value = schedule.quotaText(),
                        icon = Icons.Outlined.EventSeat
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroMiniInfo(
    label: String,
    value: String,
    icon: ImageVector
) {
    val colors = MaterialTheme.colorScheme

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Surface(
            modifier = Modifier.size(30.dp),
            shape = CircleShape,
            color = colors.surface,
            border = BorderStroke(1.dp, colors.outlineVariant)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Text(
            text = label,
            color = colors.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = value,
            color = colors.onSurface,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ScheduleRouteInfoCard(
    originCity: String,
    destinationCity: String,
    departureDate: String,
    departureTime: String,
    arrivalDate: String,
    arrivalTime: String,
    duration: String
) {
    DetailSectionCard(
        title = "Detail Perjalanan"
    ) {
        RouteSummaryRow(
            originCity = originCity,
            destinationCity = destinationCity
        )

        Divider(color = MaterialTheme.colorScheme.outlineVariant)

        DetailInfoRow(
            icon = Icons.Outlined.LocationOn,
            label = "Pelabuhan Asal",
            value = cityToPortName(originCity)
        )

        DetailInfoRow(
            icon = Icons.Outlined.LocationOn,
            label = "Pelabuhan Tujuan",
            value = cityToPortName(destinationCity)
        )

        DetailInfoRow(
            icon = Icons.Outlined.CalendarMonth,
            label = "Tanggal Keberangkatan",
            value = departureDate
        )

        DetailInfoRow(
            icon = Icons.Outlined.AccessTime,
            label = "Jam Keberangkatan",
            value = departureTime
        )

        DetailInfoRow(
            icon = Icons.Outlined.Sailing,
            label = "Estimasi Tiba",
            value = "$arrivalDate, $arrivalTime"
        )

        DetailInfoRow(
            icon = Icons.Outlined.Info,
            label = "Durasi Perjalanan",
            value = duration
        )
    }
}

@Composable
private fun RouteSummaryRow(
    originCity: String,
    destinationCity: String
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoutePoint(
            city = originCity,
            label = "Asal",
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier.weight(0.72f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = colors.primary.copy(alpha = 0.72f),
            )

            Surface(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(30.dp),
                shape = CircleShape,
                color = colors.primaryContainer.copy(alpha = 0.72f),
                border = BorderStroke(1.dp, colors.outlineVariant),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Sailing,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = colors.primary.copy(alpha = 0.72f),
            )
        }

        RoutePoint(
            city = destinationCity,
            label = "Tujuan",
            modifier = Modifier.weight(1f),
            alignEnd = true
        )
    }
}

@Composable
private fun RoutePoint(
    city: String,
    label: String,
    modifier: Modifier = Modifier,
    alignEnd: Boolean = false
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier,
        horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = label,
            color = colors.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = city.ifBlank { "-" },
            color = colors.onSurface,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun TravelDetailCard(
    schedule: ShipSchedule,
    routeDirection: RouteDirection,
) {
    DetailSectionCard(
        title = "Rute & Pelayaran"
    ) {
        DetailInfoRow(
            icon = Icons.Outlined.Sailing,
            label = "Operator/Pelayaran",
            value = schedule.operatorName(),
        )

        DetailInfoRow(
            icon = Icons.Outlined.LocationOn,
            label = "Pelabuhan Asal",
            value = cityToPortName(routeDirection.origin),
        )

        DetailInfoRow(
            icon = Icons.Outlined.LocationOn,
            label = "Pelabuhan Tujuan",
            value = cityToPortName(routeDirection.destination),
        )

        DetailInfoRow(
            icon = Icons.Outlined.AccessTime,
            label = "Estimasi Waktu",
            value = "${DateFormatter.formatDate(schedule.arrivalDate)}, ${schedule.arrivalTime}",
        )

        TransitTimeline(
            stops = schedule.transitStops(routeDirection),
        )
    }
}

@Composable
private fun TicketInformationCard(
    price: String,
    quota: String,
    status: ShipScheduleStatus
) {
    DetailSectionCard(
        title = "Informasi Tiket"
    ) {
        DetailInfoRow(
            icon = Icons.Outlined.Payments,
            label = "Harga Mulai",
            value = price,
            valueColor = MaterialTheme.colorScheme.primary
        )

        DetailInfoRow(
            icon = Icons.Outlined.EventSeat,
            label = "Kuota Tersedia",
            value = quota
        )

        DetailInfoRow(
            icon = Icons.Outlined.Info,
            label = "Status Tiket",
            value = when (status) {
                ShipScheduleStatus.Available -> "Masih tersedia"
                ShipScheduleStatus.Limited -> "Kuota terbatas"
                ShipScheduleStatus.Unavailable -> "Tiket habis"
            }
        )
    }
}

@Composable
private fun TicketClassCard(
    ticketClasses: List<TicketClass>
) {
    DetailSectionCard(
        title = "Pilihan Kelas"
    ) {
        ticketClasses.forEachIndexed { index, ticketClass ->
            TicketClassItem(ticketClass = ticketClass)

            if (index != ticketClasses.lastIndex) {
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}

@Composable
private fun TicketClassItem(
    ticketClass: TicketClass
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(38.dp),
            shape = RoundedCornerShape(12.dp),
            color = colors.primaryContainer.copy(alpha = 0.62f)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.EventSeat,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = ticketClass.name,
                color = colors.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = ticketClass.description,
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "${ticketClass.facilities} • ${ticketClass.quota}",
                color = colors.onSurface,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = ticketClass.price,
            color = colors.primary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FacilitySection(
    facilities: List<String>
) {
    DetailSectionCard(
        title = "Fasilitas"
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            facilities.forEach { facility ->
                FacilityChip(text = facility)
            }
        }
    }
}

@Composable
private fun FacilityChip(
    text: String
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(50.dp),
        color = colors.surfaceVariant.copy(alpha = 0.72f),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Shield,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = text,
                color = colors.onSurface,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun TransitTimeline(
    stops: List<String>,
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(28.dp),
                shape = CircleShape,
                color = colors.primaryContainer.copy(alpha = 0.62f),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = if (stops.size > 2) "Rencana Transit" else "Rute Perjalanan",
                color = colors.onSurface,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Column(
            modifier = Modifier.padding(start = 8.dp, top = 2.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            stops.forEachIndexed { index, stop ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Surface(
                            modifier = Modifier.size(10.dp),
                            shape = CircleShape,
                            color = if (index == 0 || index == stops.lastIndex) colors.primary else colors.onSurfaceVariant,
                        ) {}

                        if (index != stops.lastIndex) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(22.dp)
                                    .background(colors.outlineVariant),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = stop,
                        modifier = Modifier.weight(1f),
                        color = colors.onSurface,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = colors.surface,
        border = BorderStroke(1.dp, colors.outlineVariant),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = title,
                color = colors.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            content()
        }
    }
}

@Composable
private fun DetailInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = Color.Unspecified
) {
    val colors = MaterialTheme.colorScheme
    val resolvedValueColor = if (valueColor == Color.Unspecified) colors.onSurface else valueColor

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = CircleShape,
            color = colors.primaryContainer.copy(alpha = 0.62f)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = colors.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = value,
            color = resolvedValueColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun ScheduleNotFoundState(
    message: String = "Detail jadwal tidak ditemukan"
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = colors.surface,
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(1.dp, colors.outlineVariant)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Detail jadwal tidak ditemukan",
                    color = colors.onSurface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = message,
                    color = colors.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private data class RouteDirection(
    val origin: String,
    val destination: String
)

private fun cityToPortName(city: String): String {
    return when (city.lowercase()) {
        "ende" -> "Pelabuhan Ende"
        "surabaya" -> "Pelabuhan Tanjung Perak"
        "denpasar", "bali" -> "Pelabuhan Benoa"
        "kupang" -> "Pelabuhan Tenau Kupang"
        "labuan bajo" -> "Pelabuhan Labuan Bajo"
        "maumere" -> "Pelabuhan Laurens Say"
        "makassar" -> "Pelabuhan Makassar"
        else -> if (city.isBlank()) "-" else "Pelabuhan $city"
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

private fun ShipSchedule.toTicketClasses(): List<TicketClass> {
    return listOf(
        TicketClass(
            name = "Ekonomi",
            description = "Area duduk reguler untuk perjalanan hemat.",
            price = economyPrice.toRupiah(),
            quota = quotaText(),
            facilities = "Toilet, mushola, kantin, bagasi kabin. Makan belum termasuk."
        ),
        TicketClass(
            name = "Bisnis",
            description = "Kursi lebih lega dan area lebih tenang.",
            price = businessPrice.toRupiah(),
            quota = if (quota <= 0) "Habis" else "12 kursi",
            facilities = "Kursi reclining, prioritas boarding, bagasi kabin."
        ),
        TicketClass(
            name = "Kelas I",
            description = "Ruang istirahat lebih privat untuk perjalanan jauh.",
            price = firstClassPrice.toRupiah(),
            quota = if (quota <= 0) "Habis" else "6 kursi",
            facilities = "Ruang istirahat, stopkontak, prioritas layanan terbatas."
        )
    )
}

private fun ShipSchedule.operatorName(): String {
    return when {
        shipName.contains("Nusa", ignoreCase = true) -> "PT Nusa Lautan Sejahtera"
        shipName.contains("Flores", ignoreCase = true) -> "PT Flores Bahari Mandiri"
        shipName.contains("Samudra", ignoreCase = true) -> "PT Samudra Timur Line"
        shipName.contains("Lintas", ignoreCase = true) -> "PT Lintas Pulau Nusantara"
        else -> "PT Pelayaran Nusantara"
    }
}

private fun ShipSchedule.transitStops(
    routeDirection: RouteDirection,
): List<String> {
    val route = routeText().lowercase()
    val origin = "${routeDirection.origin} (${departureTime})"
    val destination = "${routeDirection.destination} (${arrivalTime})"

    return when {
        route.contains("ende") && (route.contains("denpasar") || route.contains("bali")) -> {
            listOf(origin, "Waingapu (transit)", "Bima (transit)", destination)
        }

        route.contains("ende") && route.contains("surabaya") -> {
            listOf(origin, "Labuan Bajo (transit)", "Bima (transit)", destination)
        }

        route.contains("kupang") && route.contains("surabaya") -> {
            listOf(origin, "Ende (transit)", "Makassar (transit)", destination)
        }

        route.contains("labuan bajo") && (route.contains("denpasar") || route.contains("bali")) -> {
            listOf(origin, "Bima (transit)", destination)
        }

        route.contains("maumere") && route.contains("makassar") -> {
            listOf(origin, "Baubau (transit)", destination)
        }

        else -> listOf(origin, destination)
    }
}

private fun Int.toRupiah(): String {
    return PriceFormatter.formatToRupiah(this)
}
