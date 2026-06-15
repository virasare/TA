package com.dicoding.tugas_akhir.ui.components.ticket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun ManageTicketInfoCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary3,
        ),
        border = BorderStroke(1.dp, Color(0xFFD7E9FF)),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = Primary2,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Neutral700,
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                )
            }
        }
    }
}

@Composable
fun RefundReasonCard(
    selectedReason: String,
    customReason: String,
    onReasonSelected: (String) -> Unit,
    onCustomReasonChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val reasons = listOf(
        "Perubahan rencana perjalanan",
        "Jadwal tidak sesuai",
        "Salah memilih tiket",
        "Alasan lain",
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(1.dp, Color(0xFFE3EAF2)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Payments,
                    contentDescription = null,
                    tint = Primary2,
                )

                Text(
                    text = "Alasan Refund",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Neutral700,
                )
            }

            HorizontalDivider(color = Color(0xFFE9EEF5))

            reasons.forEach { reason ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onReasonSelected(reason)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = selectedReason == reason,
                        onClick = {
                            onReasonSelected(reason)
                        },
                    )

                    Text(
                        text = reason,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Neutral700,
                    )
                }
            }

            if (selectedReason == "Alasan lain") {
                TextField(
                    value = customReason,
                    onValueChange = onCustomReasonChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Tulis alasan refund")
                    },
                    minLines = 3,
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF7FAFC),
                        unfocusedContainerColor = Color(0xFFF7FAFC),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                )
            }
        }
    }
}

@Composable
fun RescheduleOptionCard(
    selectedScheduleId: String,
    onScheduleSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val schedules = listOf(
        RescheduleOption(
            id = "RS-001",
            shipName = "KM Nusa Bahari",
            date = "15 Jul 2026",
            time = "18.00 WITA",
            route = "Ende → Surabaya",
        ),
        RescheduleOption(
            id = "RS-002",
            shipName = "KM Laut Flores",
            date = "18 Jul 2026",
            time = "09.00 WITA",
            route = "Ende → Surabaya",
        ),
        RescheduleOption(
            id = "RS-003",
            shipName = "KM Citra Samudra",
            date = "21 Jul 2026",
            time = "14.30 WITA",
            route = "Ende → Surabaya",
        ),
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(1.dp, Color(0xFFE3EAF2)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = Primary2,
                )

                Text(
                    text = "Pilih Jadwal Baru",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Neutral700,
                )
            }

            HorizontalDivider(color = Color(0xFFE9EEF5))

            schedules.forEach { schedule ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onScheduleSelected(schedule.id)
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    RadioButton(
                        selected = selectedScheduleId == schedule.id,
                        onClick = {
                            onScheduleSelected(schedule.id)
                        },
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = schedule.shipName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Neutral700,
                        )

                        Text(
                            text = schedule.route,
                            style = MaterialTheme.typography.bodySmall,
                            color = Neutral500,
                        )

                        Text(
                            text = "${schedule.date}, ${schedule.time}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Primary2,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ManageTicketSuccessCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(1.dp, Color(0xFFE3EAF2)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = null,
                tint = Primary2,
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Neutral700,
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Neutral500,
            )
        }
    }
}

data class RescheduleOption(
    val id: String,
    val shipName: String,
    val date: String,
    val time: String,
    val route: String,
)