package com.dicoding.tugas_akhir.ui.components.ticket

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun TicketManageActionCard(
    canManageTicket: Boolean,
    onRefundClick: () -> Unit,
    onRescheduleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = White,
        ),
        border = BorderStroke(1.dp, Neutral200),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Primary2,
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = "Kelola Tiket",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Neutral700,
                    )

                    Text(
                        text = if (canManageTicket) {
                            "Kamu dapat mengajukan refund atau reschedule sesuai ketentuan tiket."
                        } else {
                            "Tiket ini tidak dapat dikelola karena statusnya sudah tidak aktif."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral500,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedButton(
                    onClick = onRefundClick,
                    enabled = canManageTicket,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Primary2,
                        disabledContentColor = Neutral500,
                    ),
                    border = BorderStroke(1.dp, if (canManageTicket) Primary2 else Neutral200),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Payments,
                        contentDescription = null,
                    )

                    Text(
                        text = "Refund",
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }

                OutlinedButton(
                    onClick = onRescheduleClick,
                    enabled = canManageTicket,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Primary2,
                        disabledContentColor = Neutral500,
                    ),
                    border = BorderStroke(1.dp, if (canManageTicket) Primary2 else Neutral200),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                    )

                    Text(
                        text = "Reschedule",
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
            }

            if (!canManageTicket) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Primary3,
                    ),
                    border = BorderStroke(1.dp, Neutral200),
                ) {
                    Text(
                        text = "Refund dan reschedule hanya tersedia untuk tiket dengan status aktif.",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral700,
                    )
                }
            }
        }
    }
}