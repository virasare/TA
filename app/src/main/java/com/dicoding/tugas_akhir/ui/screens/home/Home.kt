package com.dicoding.tugas_akhir.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.DirectionsBoat
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material3.rememberDatePickerState
import com.dicoding.tugas_akhir.data.dummy.PopularRoute
import com.dicoding.tugas_akhir.data.dummy.Port
import com.dicoding.tugas_akhir.data.dummy.popularRoutes
import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.ui.components.dialog.PopularRouteSection
import com.dicoding.tugas_akhir.ui.components.cards.SearchScheduleCard
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import androidx.compose.material3.DatePickerDefaults
import com.dicoding.tugas_akhir.data.recomendation.findItemBasedPopularRoutes
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.White

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
    onPopularRouteClick: (PopularRoute) -> Unit,
    ticketOverview: Booking? = null,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val strings = LocalAppStrings.current

    val canSearch = originPort != null &&
            destinationPort != null &&
            selectedDate.isNotEmpty()

    val recommendedPopularRoutes = remember(popularRoutes) {
        findItemBasedPopularRoutes(
            routes = popularRoutes,
            currentUserId = "USER_CURRENT",
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 18.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            HomeGreeting()

            SearchScheduleCard(
                originText = originPort?.name ?: strings.chooseOrigin,
                destinationText = destinationPort?.name ?: strings.chooseDestination,
                dateText = selectedDate.ifEmpty { strings.chooseDate },
                canSearch = canSearch,
                onOriginClick = onOriginClick,
                onDestinationClick = onDestinationClick,
                onDateClick = {
                    showDatePicker = true
                },
                onSearchClick = onSearchScheduleClick
            )

            HomeTicketOverview(
                booking = ticketOverview,
            )

            SectionHeading(
                title = strings.popularRoute,
                subtitle = strings.popularRouteSubtitle,
            )

            PopularRouteSection(
                routes = recommendedPopularRoutes,
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
    val strings = LocalAppStrings.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = strings.homeGreetingTitle,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            text = strings.homeGreetingSubtitle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun HomeTicketOverview(
    booking: Booking?,
) {
    val colors = MaterialTheme.colorScheme
    val strings = LocalAppStrings.current

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = colors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.outlineVariant),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                TicketInfoPill(
                    icon = Icons.Outlined.ConfirmationNumber,
                    text = booking?.id ?: strings.noTicketYet,
                    modifier = Modifier.weight(1f),
                )

                TicketInfoPill(
                    icon = Icons.Outlined.AccessTime,
                    text = booking?.departureReminderLabel() ?: strings.monitorActiveTicket,
                    modifier = Modifier.weight(1f),
                )
            }

            HorizontalDivider(
                color = colors.outlineVariant,
            )

            if (booking == null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = strings.noActiveTicket,
                        color = colors.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Text(
                        text = strings.ticketSummaryEmptyDesc,
                        color = colors.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            } else {
                Text(
                    text = booking.shipName,
                    color = colors.onSurface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TicketCityLabel(
                        city = booking.origin,
                        label = "Asal",
                        modifier = Modifier.weight(1f),
                    )

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                color = colors.primaryContainer,
                                shape = CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DirectionsBoat,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(22.dp),
                        )
                    }

                    TicketCityLabel(
                        city = booking.destination,
                        label = "Tujuan",
                        modifier = Modifier.weight(1f),
                        alignEnd = true,
                    )
                }

                Text(
                    text = "${booking.departureDate}, ${booking.departureTime}",
                    color = colors.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun TicketInfoPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = modifier.height(34.dp),
        shape = RoundedCornerShape(50.dp),
        color = colors.surfaceVariant,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(15.dp),
            )

            Text(
                text = text,
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun TicketCityLabel(
    city: String,
    label: String,
    modifier: Modifier = Modifier,
    alignEnd: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall,
        )

        Text(
            text = city.ifBlank { "-" },
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun SectionHeading(
    title: String,
    subtitle: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            text = subtitle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
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
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Primary2,
                ),
            ) {
                Text("Pilih")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Primary2,
                ),
            ) {
                Text("Batal")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                headlineContentColor = MaterialTheme.colorScheme.onSurface,
                weekdayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                subheadContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                navigationContentColor = Primary2,
                yearContentColor = MaterialTheme.colorScheme.onSurface,
                currentYearContentColor = Primary2,
                selectedYearContentColor = White,
                selectedYearContainerColor = Primary2,
                dayContentColor = MaterialTheme.colorScheme.onSurface,
                selectedDayContentColor = White,
                selectedDayContainerColor = Primary2,
                todayContentColor = Primary2,
                todayDateBorderColor = Primary2,
            )
        )
    }
}

private fun Booking.departureReminderLabel(): String {
    val daysUntilDeparture = departureDate.toStartOfDayMillis()?.let { departureMillis ->
        val todayMillis = Calendar.getInstance().startOfDayMillis()
        TimeUnit.MILLISECONDS.toDays(departureMillis - todayMillis)
    }

    return when (daysUntilDeparture) {
        0L -> "Hari ini"
        1L -> "H-1"
        2L -> "H-2"
        3L -> "H-3"
        else -> "Aktif"
    }
}

private fun String.toStartOfDayMillis(): Long? {
    val formats = listOf(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
        SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID")),
        SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID")),
    )

    formats.forEach { formatter ->
        try {
            formatter.isLenient = false
            val date = formatter.parse(this)

            if (date != null) {
                return Calendar.getInstance().apply {
                    time = date
                }.startOfDayMillis()
            }
        } catch (exception: Exception) {
            // Try the next supported date format.
        }
    }

    return null
}

private fun Calendar.startOfDayMillis(): Long {
    return apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

private fun Long.toFormattedDate(): String {
    val formatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.forLanguageTag("id-ID")
    )

    return formatter.format(Date(this))
}
