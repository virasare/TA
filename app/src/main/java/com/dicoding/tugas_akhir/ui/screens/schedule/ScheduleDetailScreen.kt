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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
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
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleStatus
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.dialog.feedback.BadgeVariant
import com.dicoding.tugas_akhir.ui.components.dialog.feedback.InfoBox
import com.dicoding.tugas_akhir.ui.components.dialog.feedback.InfoBoxVariant
import com.dicoding.tugas_akhir.ui.components.dialog.feedback.StatusBadge
import com.dicoding.tugas_akhir.ui.components.loading.ScheduleDetailPlaceholder
import com.dicoding.tugas_akhir.ui.state.ScheduleDetailUiState
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White
import com.dicoding.tugas_akhir.ui.viewmodel.ScheduleViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

private data class TicketClass(
    val name: String,
    val description: String,
    val price: String,
    val quota: String
)

@Composable
fun ScheduleDetailScreen(
    scheduleId: String,
    onBackClick: () -> Unit,
    onBookTicketClick: () -> Unit,
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
    onBookTicketClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val routeDirection = schedule.route.toRouteDirection()

    val ticketClasses = schedule.toTicketClasses()

    val facilities = listOf(
        "Area Bersantai",
        "Mushola",
        "Toilet",
        "Klinik Kesehatan",
        "Area Bermain Anak",
        "Ruang Laktasi",
        "Ruang Baca",
        "Kantin"
    )

    val isTicketAvailable = schedule.status != ShipScheduleStatus.Unavailable

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
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
                    departureDate = schedule.departureDate,
                    departureTime = schedule.departureTime,
                    arrivalDate = schedule.arrivalDate,
                    arrivalTime = schedule.arrivalTime,
                    duration = schedule.duration
                )
            }

            item {
                TicketInformationCard(
                    price = schedule.price,
                    quota = schedule.quota,
                    status = schedule.status
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
            color = White,
            border = BorderStroke(1.dp, Neutral200),
            shadowElevation = 8.dp
        ) {
            PrimaryButton(
                text = if (isTicketAvailable) "Pesan Tiket" else "Tiket Habis",
                onClick = onBookTicketClick,
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
            .background(Background)
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
    val badgeText = when (schedule.status) {
        ShipScheduleStatus.Available -> "Tersedia"
        ShipScheduleStatus.Limited -> "Terbatas"
        ShipScheduleStatus.Unavailable -> "Habis"
    }

    val badgeVariant = when (schedule.status) {
        ShipScheduleStatus.Available -> BadgeVariant.Success
        ShipScheduleStatus.Limited -> BadgeVariant.Warning
        ShipScheduleStatus.Unavailable -> BadgeVariant.Error
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 3.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Primary3,
                            White,
                            White
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
                        color = White,
                        border = BorderStroke(1.dp, Neutral200),
                        shadowElevation = 1.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DirectionsBoat,
                                contentDescription = null,
                                tint = Primary2,
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
                            color = Neutral700,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = Neutral500,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = schedule.route,
                                color = Neutral500,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    StatusBadge(
                        text = badgeText,
                        variant = badgeVariant
                    )
                }

                Divider(color = Neutral200)

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
                        value = schedule.price,
                        icon = Icons.Outlined.Payments
                    )

                    HeroMiniInfo(
                        label = "Kuota",
                        value = schedule.quota,
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Surface(
            modifier = Modifier.size(30.dp),
            shape = CircleShape,
            color = White,
            border = BorderStroke(1.dp, Neutral200)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Primary2,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Text(
            text = label,
            color = Neutral500,
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = value,
            color = Neutral700,
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

        Divider(color = Neutral200)

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
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoutePoint(
            city = originCity,
            label = "Asal",
            modifier = Modifier.weight(1f)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Divider(
                modifier = Modifier.width(44.dp),
                color = Primary2
            )

            Text(
                text = "ke",
                color = Neutral500,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 2.dp)
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
    Column(
        modifier = modifier,
        horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = label,
            color = Neutral500,
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = city.ifBlank { "-" },
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
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
            valueColor = Primary2
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
                Divider(color = Neutral200)
            }
        }
    }
}

@Composable
private fun TicketClassItem(
    ticketClass: TicketClass
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(38.dp),
            shape = RoundedCornerShape(12.dp),
            color = Primary3
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.EventSeat,
                    contentDescription = null,
                    tint = Primary2,
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
                color = Neutral700,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = ticketClass.description,
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = ticketClass.quota,
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = ticketClass.price,
            color = Primary2,
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
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Neutral200)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Shield,
                contentDescription = null,
                tint = Primary2,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = text,
                color = Neutral700,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun DetailSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = title,
                color = Neutral700,
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
    valueColor: androidx.compose.ui.graphics.Color = Neutral700
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = CircleShape,
            color = Primary3
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Primary2,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = Neutral500,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = value,
            color = valueColor,
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
            border = BorderStroke(1.dp, Neutral200)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Detail jadwal tidak ditemukan",
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

private fun cityToPortName(city: String): String {
    return when (city.lowercase()) {
        "ende" -> "Pelabuhan Ende"
        "surabaya" -> "Pelabuhan Tanjung Perak"
        "denpasar", "bali" -> "Pelabuhan Benoa"
        "kupang" -> "Pelabuhan Tenau Kupang"
        "labuan bajo" -> "Pelabuhan Labuan Bajo"
        "maumere" -> "Pelabuhan Laurens Say"
        "makassar" -> "Pelabuhan Makassar"
        else -> "Pelabuhan $city"
    }
}

private fun ShipSchedule.toTicketClasses(): List<TicketClass> {
    val basePrice = price.toPriceNumber()

    return listOf(
        TicketClass(
            name = "Ekonomi",
            description = "Tempat duduk standar penumpang",
            price = price,
            quota = quota
        ),
        TicketClass(
            name = "Bisnis",
            description = "Tempat duduk lebih nyaman",
            price = (basePrice + 150000).toRupiah(),
            quota = "12 kursi"
        ),
        TicketClass(
            name = "Kelas I",
            description = "Kabin terbatas dengan fasilitas lebih lengkap",
            price = (basePrice + 350000).toRupiah(),
            quota = "6 kursi"
        )
    )
}

private fun String.toPriceNumber(): Int {
    return replace("Rp", "")
        .replace(".", "")
        .replace(",", "")
        .trim()
        .toIntOrNull() ?: 0
}

private fun Int.toRupiah(): String {
    return "Rp%,d".format(this).replace(",", ".")
}