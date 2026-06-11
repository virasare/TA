package com.dicoding.tugas_akhir.ui.screens.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.RemoveCircle
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.data.dummy.TicketClassOption
import com.dicoding.tugas_akhir.data.dummy.toPriceNumber
import com.dicoding.tugas_akhir.data.dummy.toRupiah
import com.dicoding.tugas_akhir.ui.components.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleStatus
import com.dicoding.tugas_akhir.ui.components.feedback.BadgeVariant
import com.dicoding.tugas_akhir.ui.components.feedback.StatusBadge
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun SelectTicketScreen(
    schedule: ShipSchedule?,
    onContinueClick: (TicketClassOption) -> Unit
) {
    if (schedule == null) {
        Text(text = "Data jadwal tidak ditemukan")
        return
    }

    val basePrice = schedule.price.toPriceNumber()

    val ticketClasses = remember {
        mutableStateListOf(
            TicketClassOption("Ekonomi", basePrice, 1),
            TicketClassOption("Bisnis", basePrice + 150000, 0),
            TicketClassOption("Kelas I", basePrice + 400000, 0)
        )
    }

    val selectedTicket = ticketClasses.firstOrNull { it.passengerCount > 0 }
    val totalPassenger = ticketClasses.sumOf { it.passengerCount }
    val totalPrice = ticketClasses.sumOf { it.price * it.passengerCount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BookingScheduleCard(schedule = schedule)
            }

            item {
                Text(
                    text = "Pilihan Tiket",
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Pilih kelas tiket dan jumlah penumpang",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = White,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Neutral200),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        ticketClasses.forEachIndexed { index, ticket ->
                            TicketClassRow(
                                ticket = ticket,
                                onMinusClick = {
                                    if (ticket.passengerCount > 0) {
                                        ticketClasses[index] = ticket.copy(
                                            passengerCount = ticket.passengerCount - 1
                                        )
                                    }
                                },
                                onPlusClick = {
                                    ticketClasses[index] = ticket.copy(
                                        passengerCount = ticket.passengerCount + 1
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = White,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryRow(
                    label = "Jumlah penumpang",
                    value = totalPassenger.toString()
                )

                SummaryRow(
                    label = "Total sementara",
                    value = totalPrice.toRupiah(),
                    valueColor = Primary2
                )

                PrimaryButton(
                    text = "Lanjut",
                    enabled = selectedTicket != null,
                    onClick = {
                        selectedTicket?.let {
                            onContinueClick(it)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BookingScheduleCard(
    schedule: ShipSchedule
) {
    val badgeText = when (schedule.status) {
        ShipScheduleStatus.Available -> "Aktif"
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
        color = White,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = schedule.shipName,
                        color = Neutral700,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = Primary2,
                            modifier = Modifier.size(18.dp)
                        )

                        Text(
                            text = schedule.route,
                            color = Neutral500,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                StatusBadge(
                    text = badgeText,
                    variant = badgeVariant
                )
            }
        }
    }
}

@Composable
private fun TicketClassRow(
    ticket: TicketClassOption,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Primary3,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ConfirmationNumber,
                contentDescription = null,
                tint = Primary2,
                modifier = Modifier
                    .padding(10.dp)
                    .size(22.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = ticket.name,
                color = Neutral700,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = ticket.price.toRupiah(),
                color = Primary2,
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(onClick = onMinusClick) {
            Icon(
                imageVector = Icons.Outlined.RemoveCircle,
                contentDescription = null,
                tint = if (ticket.passengerCount > 0) Primary2 else Neutral200
            )
        }

        Text(
            text = ticket.passengerCount.toString(),
            color = Neutral700,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium
        )

        IconButton(onClick = onPlusClick) {
            Icon(
                imageVector = Icons.Outlined.AddCircle,
                contentDescription = null,
                tint = Primary2
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = Neutral700
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Neutral500,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            color = valueColor,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}