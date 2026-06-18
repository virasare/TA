package com.dicoding.tugas_akhir.ui.components.notification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.domain.model.AppNotification
import com.dicoding.tugas_akhir.domain.model.NotificationType
import com.dicoding.tugas_akhir.ui.theme.Primary2
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.TextButton

@Composable
fun NotificationSummaryCard(
    unreadCount: Int,
    onMarkAllAsRead: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEAF4FF)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFD7E9FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = unreadCount.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = "Notifikasi Belum Dibaca",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF102A43),
                )

                Text(
                    text = if (unreadCount > 0) {
                        "Ada informasi baru yang perlu kamu cek."
                    } else {
                        "Semua notifikasi sudah dibaca."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF627D98),
                )
            }

            Text(
                text = "Tandai semua",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF1976D2),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    onMarkAllAsRead()
                },
            )
        }
    }
}

@Composable
fun NotificationFilterSection(
    selectedFilter: NotificationFilter,
    onFilterSelected: (NotificationFilter) -> Unit,
    onMarkAllAsRead: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NotificationFilter.entries.forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { onFilterSelected(filter) },
                    label = { Text(filter.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = colors.surface,
                        labelColor = colors.onSurfaceVariant,
                        selectedContainerColor = colors.primaryContainer,
                        selectedLabelColor = colors.onPrimaryContainer,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedFilter == filter,
                        borderColor = colors.outlineVariant,
                        selectedBorderColor = colors.primary,
                    ),
                )
            }
        }

        TextButton(
            onClick = onMarkAllAsRead,
        ) {
            Text("Tandai semua")
        }
    }
}

@Composable
fun AppNotificationCard(
    notification: AppNotification,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon = notification.type.toIcon()
    val accentColor = notification.type.toColor()
    val colors = MaterialTheme.colorScheme

    val rowBackground = if (notification.isRead) {
        Color.Transparent
    } else {
        colors.primaryContainer.copy(alpha = 0.32f)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = rowBackground,
                shape = RoundedCornerShape(18.dp),
            )
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = accentColor.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(14.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onSurface,
                        modifier = Modifier.weight(1f),
                    )

                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = colors.primary,
                                    shape = CircleShape,
                                ),
                        )
                    }
                }

                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                )

                Text(
                    text = formatNotificationTime(notification.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.onSurfaceVariant.copy(alpha = 0.78f),
                )
            }

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(34.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = "Hapus notifikasi",
                    tint = colors.error,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        HorizontalDivider(
            color = colors.outlineVariant.copy(alpha = 0.72f),
        )
    }
}

enum class NotificationFilter(
    val label: String,
) {
    ALL("Semua"),
    UNREAD("Belum Dibaca"),
}

private fun NotificationType.toIcon(): ImageVector {
    return when (this) {
        NotificationType.PAYMENT -> Icons.Outlined.Payments
        NotificationType.TICKET -> Icons.Outlined.ConfirmationNumber
        NotificationType.SCHEDULE -> Icons.Outlined.Schedule
        NotificationType.REFUND -> Icons.Outlined.Info
        NotificationType.RESCHEDULE -> Icons.Outlined.SyncAlt
        NotificationType.INFO -> Icons.Outlined.Info
    }
}

private fun NotificationType.toColor(): Color {
    return when (this) {
        NotificationType.PAYMENT -> Color(0xFFF57C00)
        NotificationType.TICKET -> Color(0xFF2E7D32)
        NotificationType.SCHEDULE -> Color(0xFF1976D2)
        NotificationType.REFUND -> Color(0xFFD32F2F)
        NotificationType.RESCHEDULE -> Color(0xFF0288D1)
        NotificationType.INFO -> Primary2
    }
}

private fun formatNotificationTime(createdAt: Long): String {
    val now = System.currentTimeMillis()
    val diffMillis = now - createdAt

    val minute = 60 * 1000L
    val hour = 60 * minute
    val day = 24 * hour

    return when {
        diffMillis < minute -> "Baru saja"
        diffMillis < hour -> "${diffMillis / minute} menit lalu"
        diffMillis < day -> "${diffMillis / hour} jam lalu"
        else -> "${diffMillis / day} hari lalu"
    }
}