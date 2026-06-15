package com.dicoding.tugas_akhir.ui.components.ticket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

enum class TicketUiStatus {
    WAITING_PAYMENT,
    ACTIVE,
    COMPLETED,
    CANCELED,
    REFUND_PROCESS,
    RESCHEDULE_PROCESS,
}

fun String.toTicketUiStatus(): TicketUiStatus {
    return when (this.lowercase()) {
        "waiting_payment",
        "menunggu pembayaran",
        "pending",
        "belum bayar" -> TicketUiStatus.WAITING_PAYMENT

        "active",
        "aktif" -> TicketUiStatus.ACTIVE

        "completed",
        "selesai" -> TicketUiStatus.COMPLETED

        "canceled",
        "cancelled",
        "dibatalkan",
        "batal" -> TicketUiStatus.CANCELED

        "refund_process",
        "refund diproses",
        "refund" -> TicketUiStatus.REFUND_PROCESS

        "reschedule_process",
        "reschedule diproses",
        "reschedule" -> TicketUiStatus.RESCHEDULE_PROCESS

        else -> TicketUiStatus.ACTIVE
    }
}

@Composable
fun TicketStatusBadge(
    status: TicketUiStatus,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = status.backgroundColor()
    val contentColor = status.contentColor()

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(50.dp),
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = status.icon(),
                contentDescription = null,
                tint = contentColor,
            )

            Text(
                text = status.label(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = contentColor,
            )
        }
    }
}

@Composable
fun TicketStatusInfoCard(
    status: TicketUiStatus,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = status.backgroundColor(),
        ),
        border = BorderStroke(
            width = 1.dp,
            color = status.borderColor(),
        ),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = status.icon(),
                contentDescription = null,
                tint = status.contentColor(),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = status.label(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Neutral700,
                )

                Text(
                    text = status.description(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                )
            }
        }
    }
}

@Composable
fun TicketActionByStatusCard(
    status: TicketUiStatus,
    onPayNowClick: () -> Unit,
    onRefundClick: () -> Unit,
    onRescheduleClick: () -> Unit,
    onSeeTicketClick: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Aksi Tiket",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Neutral700,
            )

            when (status) {
                TicketUiStatus.WAITING_PAYMENT -> {
                    StatusActionButton(
                        text = "Bayar Sekarang",
                        onClick = onPayNowClick,
                    )
                }

                TicketUiStatus.ACTIVE -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        StatusActionButton(
                            text = "Refund",
                            onClick = onRefundClick,
                            modifier = Modifier.weight(1f),
                        )

                        StatusActionButton(
                            text = "Reschedule",
                            onClick = onRescheduleClick,
                            modifier = Modifier.weight(1f),
                        )
                    }

                    StatusActionButton(
                        text = "Lihat E-Ticket",
                        onClick = onSeeTicketClick,
                    )
                }

                TicketUiStatus.RESCHEDULE_PROCESS -> {
                    StatusActionButton(
                        text = "Lihat Tiket",
                        onClick = onSeeTicketClick,
                    )
                }

                TicketUiStatus.COMPLETED -> {
                    StatusActionButton(
                        text = "Pesan Lagi",
                        onClick = onSeeTicketClick,
                    )
                }

                TicketUiStatus.CANCELED,
                TicketUiStatus.REFUND_PROCESS -> {
                    Text(
                        text = "Tidak ada aksi lanjutan untuk status tiket ini.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral500,
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Primary2,
        ),
        border = BorderStroke(1.dp, Primary2),
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private fun TicketUiStatus.label(): String {
    return when (this) {
        TicketUiStatus.WAITING_PAYMENT -> "Menunggu Pembayaran"
        TicketUiStatus.ACTIVE -> "Aktif"
        TicketUiStatus.COMPLETED -> "Selesai"
        TicketUiStatus.CANCELED -> "Dibatalkan"
        TicketUiStatus.REFUND_PROCESS -> "Refund Diproses"
        TicketUiStatus.RESCHEDULE_PROCESS -> "Reschedule Diproses"
    }
}

private fun TicketUiStatus.description(): String {
    return when (this) {
        TicketUiStatus.WAITING_PAYMENT -> "Selesaikan pembayaran agar e-ticket dapat diterbitkan."
        TicketUiStatus.ACTIVE -> "Tiket aktif dan dapat digunakan sesuai jadwal keberangkatan."
        TicketUiStatus.COMPLETED -> "Perjalanan telah selesai."
        TicketUiStatus.CANCELED -> "Pesanan tiket telah dibatalkan."
        TicketUiStatus.REFUND_PROCESS -> "Pengajuan refund sedang diproses."
        TicketUiStatus.RESCHEDULE_PROCESS -> "Pengajuan perubahan jadwal sedang diproses."
    }
}

private fun TicketUiStatus.icon(): ImageVector {
    return when (this) {
        TicketUiStatus.WAITING_PAYMENT -> Icons.Outlined.Payments
        TicketUiStatus.ACTIVE -> Icons.Outlined.CheckCircle
        TicketUiStatus.COMPLETED -> Icons.Outlined.CheckCircle
        TicketUiStatus.CANCELED -> Icons.Outlined.Cancel
        TicketUiStatus.REFUND_PROCESS -> Icons.Outlined.Info
        TicketUiStatus.RESCHEDULE_PROCESS -> Icons.Outlined.SyncAlt
    }
}

private fun TicketUiStatus.backgroundColor(): Color {
    return when (this) {
        TicketUiStatus.WAITING_PAYMENT -> Color(0xFFFFF7E6)
        TicketUiStatus.ACTIVE -> Color(0xFFEAF7EE)
        TicketUiStatus.COMPLETED -> Color(0xFFEAF4FF)
        TicketUiStatus.CANCELED -> Color(0xFFFFEBEE)
        TicketUiStatus.REFUND_PROCESS -> Color(0xFFFFF7E6)
        TicketUiStatus.RESCHEDULE_PROCESS -> Primary3
    }
}

private fun TicketUiStatus.contentColor(): Color {
    return when (this) {
        TicketUiStatus.WAITING_PAYMENT -> Color(0xFFF57C00)
        TicketUiStatus.ACTIVE -> Color(0xFF2E7D32)
        TicketUiStatus.COMPLETED -> Primary2
        TicketUiStatus.CANCELED -> Color(0xFFC62828)
        TicketUiStatus.REFUND_PROCESS -> Color(0xFFF57C00)
        TicketUiStatus.RESCHEDULE_PROCESS -> Primary2
    }
}

private fun TicketUiStatus.borderColor(): Color {
    return when (this) {
        TicketUiStatus.WAITING_PAYMENT -> Color(0xFFFFD699)
        TicketUiStatus.ACTIVE -> Color(0xFFC8E6C9)
        TicketUiStatus.COMPLETED -> Color(0xFFD7E9FF)
        TicketUiStatus.CANCELED -> Color(0xFFFFCDD2)
        TicketUiStatus.REFUND_PROCESS -> Color(0xFFFFD699)
        TicketUiStatus.RESCHEDULE_PROCESS -> Color(0xFFD7E9FF)
    }
}