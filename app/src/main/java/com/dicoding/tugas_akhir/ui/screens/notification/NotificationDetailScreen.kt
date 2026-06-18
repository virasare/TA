package com.dicoding.tugas_akhir.ui.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.domain.model.AppNotification
import com.dicoding.tugas_akhir.domain.model.NotificationType
import com.dicoding.tugas_akhir.ui.components.profile.InfoNote
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.viewmodel.NotificationViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.height
import com.dicoding.tugas_akhir.ui.components.loading.shimmerEffect
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView

@Composable
fun NotificationDetailScreen(
    notificationId: String,
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val notification = notifications.find {
        it.id == notificationId
    }

    LaunchedEffect(notificationId) {
        viewModel.markAsRead(notificationId)
    }

    when {
        isLoading -> {
            NotificationDetailPlaceholder(
                modifier = modifier,
            )
        }

        notification == null -> {
            NotificationNotFound(
                modifier = modifier,
            )
        }

        else -> {
            NotificationDetailContent(
                notification = notification,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun NotificationDetailContent(
    notification: AppNotification,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val icon = notification.getIcon()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.surface,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                color = colors.surfaceVariant,
                                shape = CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Primary2,
                            modifier = Modifier.size(36.dp),
                        )
                    }

                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface,
                    )

                    Text(
                        text = notification.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant,
                    )

                    HorizontalDivider(
                        color = colors.outlineVariant,
                    )

                    NotificationDetailRow(
                        title = "Jenis Notifikasi",
                        value = notification.type.toDisplayType(),
                    )

                    NotificationDetailRow(
                        title = "Status",
                        value = if (notification.isRead) "Sudah dibaca" else "Belum dibaca",
                    )

                    NotificationDetailRow(
                        title = "Waktu",
                        value = notification.createdAt.toReadableDate(),
                    )
                }
            }
        }

        item {
            InfoNote(
                title = "Informasi",
                text = "Notifikasi ini tersimpan di aplikasi dan digunakan sebagai riwayat informasi untuk pengguna.",
            )
        }
    }
}

@Composable
private fun NotificationDetailPlaceholder(
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(26.dp),
                    )
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .shimmerEffect(cornerRadius = 36),
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .height(22.dp)
                        .shimmerEffect(),
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .shimmerEffect(),
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.86f)
                        .height(14.dp)
                        .shimmerEffect(),
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                )

                repeat(3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.38f)
                                .height(14.dp)
                                .shimmerEffect(),
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.42f)
                                .height(14.dp)
                                .shimmerEffect(),
                        )
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(18.dp),
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.42f)
                        .height(16.dp)
                        .shimmerEffect(),
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(13.dp)
                        .shimmerEffect(),
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.78f)
                        .height(13.dp)
                        .shimmerEffect(),
                )
            }
        }
    }
}

@Composable
private fun NotificationDetailRow(
    title: String,
    value: String,
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = colors.onSurface,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun NotificationNotFound(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        LottieStateView(
            animationFile = "empty.json",
            title = "Notifikasi tidak ditemukan",
            message = "Data notifikasi mungkin sudah dihapus atau belum tersedia.",
        )
    }
}

private fun AppNotification.getIcon(): ImageVector {
    return when (type) {
        NotificationType.PAYMENT -> Icons.Outlined.Payments
        NotificationType.TICKET -> Icons.Outlined.ConfirmationNumber
        NotificationType.SCHEDULE -> Icons.Outlined.Schedule
        NotificationType.RESCHEDULE -> Icons.Outlined.SyncAlt
        NotificationType.REFUND -> Icons.Outlined.SyncAlt
        NotificationType.INFO -> Icons.Outlined.Info
    }
}

private fun NotificationType.toDisplayType(): String {
    return when (this) {
        NotificationType.PAYMENT -> "Pembayaran"
        NotificationType.TICKET -> "Tiket"
        NotificationType.SCHEDULE -> "Jadwal"
        NotificationType.RESCHEDULE -> "Reschedule"
        NotificationType.REFUND -> "Refund"
        NotificationType.INFO -> "Informasi"
    }
}

private fun Long.toReadableDate(): String {
    val formatter = SimpleDateFormat(
        "dd MMM yyyy, HH:mm",
        Locale.forLanguageTag("id-ID"),
    )

    return formatter.format(Date(this))
}