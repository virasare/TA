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

@Composable
fun TicketManageActionCard(
    canManageTicket: Boolean,
    onRefundClick: () -> Unit,
    onRescheduleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = colors.outlineVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
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
                    tint = colors.primary,
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = "Kelola Tiket",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface,
                    )

                    Text(
                        text = if (canManageTicket) {
                            "Kamu dapat mengajukan refund atau reschedule sesuai ketentuan tiket."
                        } else {
                            "Tiket ini tidak dapat dikelola karena statusnya sudah tidak aktif."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant,
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
                        contentColor = colors.primary,
                        disabledContentColor = colors.onSurfaceVariant,
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (canManageTicket) {
                            colors.primary
                        } else {
                            colors.outlineVariant
                        },
                    ),
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
                        contentColor = colors.primary,
                        disabledContentColor = colors.onSurfaceVariant,
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (canManageTicket) {
                            colors.primary
                        } else {
                            colors.outlineVariant
                        },
                    ),
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
                        containerColor = colors.surfaceVariant,
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = colors.outlineVariant,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                ) {
                    Text(
                        text = "Refund dan reschedule hanya tersedia untuk tiket dengan status aktif.",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant,
                    )
                }
            }
        }
    }
}