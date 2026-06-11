package com.dicoding.tugas_akhir.ui.screens.notification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.AppNotification
import com.dicoding.tugas_akhir.data.dummy.NotificationType
import com.dicoding.tugas_akhir.data.dummy.dummyNotifications
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun NotificationScreen(
    onNotificationClick: (Int) -> Unit
) {
    val notifications = dummyNotifications

    if (notifications.isEmpty()) {
        EmptyNotificationState()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .navigationBarsPadding(),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 20.dp,
                bottom = 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(notifications) { notification ->
                NotificationItem(
                    notification = notification,
                    onClick = {
                        onNotificationClick(notification.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: AppNotification,
    onClick: () -> Unit
) {
    val icon = notification.type.toIcon()
    val iconTint = if (notification.isRead) Neutral500 else Primary2
    val iconBackground = if (notification.isRead) Neutral200 else Primary3

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        color = if (notification.isRead) White else Primary3.copy(alpha = 0.45f),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                color = iconBackground,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        color = Neutral700,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = notification.time,
                        color = if (notification.isRead) Neutral500 else Primary2,
                        style = MaterialTheme.typography.bodySmall
                    )

                    if (!notification.isRead) {
                        Surface(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(7.dp),
                            color = Primary2,
                            shape = RoundedCornerShape(50.dp),
                            content = {}
                        )
                    }
                }

                Text(
                    text = notification.message,
                    color = Neutral500,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun EmptyNotificationState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = White,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, Neutral200),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    color = Primary3,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        tint = Primary2,
                        modifier = Modifier
                            .padding(18.dp)
                            .size(34.dp)
                    )
                }

                Text(
                    text = "Belum Ada Notifikasi",
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Notifikasi terkait pemesanan, pembayaran, perubahan jadwal, dan pengajuan refund akan muncul di sini.",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun NotificationType.toIcon(): ImageVector {
    return when (this) {
        NotificationType.PaymentSuccess -> Icons.Outlined.CheckCircle
        NotificationType.DepartureReminder -> Icons.Outlined.Event
        NotificationType.ScheduleChanged -> Icons.Outlined.Info
        NotificationType.RefundProcess -> Icons.Outlined.Sync
    }
}