package com.dicoding.tugas_akhir.ui.components.dialog.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DirectionsBoat
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.dummyShipSchedules
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.dialog.feedback.BadgeVariant
import com.dicoding.tugas_akhir.ui.components.dialog.feedback.StatusBadge
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.White

enum class ShipScheduleStatus {
    Available,
    Limited,
    Unavailable
}

@Composable
fun ShipScheduleCard(
    shipName: String,
    route: String,
    departureDate: String,
    departureTime: String,
    arrivalTime: String,
    duration: String,
    price: String,
    quota: String,
    modifier: Modifier = Modifier,
    status: ShipScheduleStatus = ShipScheduleStatus.Available,
    onClick: () -> Unit
) {
    val badgeText = when (status) {
        ShipScheduleStatus.Available -> "Tersedia"
        ShipScheduleStatus.Limited -> "Terbatas"
        ShipScheduleStatus.Unavailable -> "Habis"
    }

    val badgeVariant = when (status) {
        ShipScheduleStatus.Available -> BadgeVariant.Success
        ShipScheduleStatus.Limited -> BadgeVariant.Warning
        ShipScheduleStatus.Unavailable -> BadgeVariant.Error
    }

    val buttonEnabled = status != ShipScheduleStatus.Unavailable

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        border = BorderStroke(1.dp, Neutral200),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DirectionsBoat,
                            contentDescription = null,
                            tint = Primary2
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = shipName,
                            style = MaterialTheme.typography.titleMedium,
                            color = Neutral700,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = null,
                            tint = Neutral500
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = route,
                            style = MaterialTheme.typography.bodySmall,
                            color = Neutral500
                        )
                    }
                }

                StatusBadge(
                    text = badgeText,
                    variant = badgeVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ScheduleInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.CalendarMonth,
                    title = "Tanggal",
                    value = departureDate
                )

                ScheduleInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.AccessTime,
                    title = "Berangkat",
                    value = departureTime
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ScheduleInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.AccessTime,
                    title = "Tiba",
                    value = arrivalTime
                )

                ScheduleInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.EventSeat,
                    title = "Kuota",
                    value = quota
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Mulai dari",
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral500
                    )

                    Text(
                        text = price,
                        style = MaterialTheme.typography.titleMedium,
                        color = Primary2,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = duration,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500
                )
            }

            PrimaryButton(
                text = if (buttonEnabled) "Pilih Jadwal" else "Tiket Habis",
                onClick = onClick,
                enabled = buttonEnabled
            )
        }
    }
}

@Composable
private fun ScheduleInfoItem(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Neutral500
        )

        Spacer(modifier = Modifier.width(6.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Neutral500
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Neutral700,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 900
)
@Composable
fun ShipScheduleListPreview() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(dummyShipSchedules) { schedule ->
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
                onClick = {}
            )
        }
    }
}