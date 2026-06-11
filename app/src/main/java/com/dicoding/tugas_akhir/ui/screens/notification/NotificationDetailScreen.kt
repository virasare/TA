package com.dicoding.tugas_akhir.ui.screens.notification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsBoat
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.AppNotification
import com.dicoding.tugas_akhir.ui.components.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun NotificationDetailScreen(
    notification: AppNotification?,
    onSeeTicketClick: () -> Unit
) {
    if (notification == null) {
        NotificationNotFoundState()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 20.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                NotificationMessageSection(notification = notification)
            }

            item {
                NotificationTripDetailCard(notification = notification)
            }
        }

        PrimaryButton(
            text = "Lihat E-Ticket",
            onClick = onSeeTicketClick,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun NotificationMessageSection(
    notification: AppNotification
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = notification.title,
                color = Neutral700,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = notification.time,
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = notification.message.toDetailMessage(),
            color = Neutral500,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun NotificationTripDetailCard(
    notification: AppNotification
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = White,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Detail Perjalanan",
                color = Neutral700,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            DetailTextItem(
                label = "Kode Booking",
                value = notification.bookingCode ?: "-"
            )

            DetailTextItem(
                label = "Nama Kapal",
                value = notification.shipName ?: "-"
            )

            Text(
                text = "Rute",
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall
            )

            RoutePointItem(
                title = "Pelabuhan Asal",
                value = notification.originPort ?: "-"
            )

            RoutePointItem(
                title = "Pelabuhan Tujuan",
                value = notification.destinationPort ?: "-"
            )

            DetailTextItem(
                label = "Jadwal Keberangkatan",
                value = notification.departureSchedule ?: "-"
            )

            DetailTextItem(
                label = "Status",
                value = notification.status ?: "-",
                valueColor = Primary2
            )
        }
    }
}

@Composable
private fun DetailTextItem(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = Neutral700
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            color = Neutral500,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = value,
            color = valueColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun RoutePointItem(
    title: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Primary3,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = Primary2,
                modifier = Modifier
                    .padding(10.dp)
                    .size(20.dp)
            )
        }

        Column(
            modifier = Modifier.padding(start = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = value,
                color = Neutral700,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun NotificationNotFoundState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = White,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, Neutral200)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = Primary2,
                    modifier = Modifier.size(40.dp)
                )

                Text(
                    text = "Notifikasi tidak ditemukan",
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Detail notifikasi tidak tersedia.",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun String.toDetailMessage(): String {
    return when {
        contains("Pembayaran", ignoreCase = true) -> {
            "Pembayaran tiket Anda telah berhasil dikonfirmasi. Tiket elektronik sudah diterbitkan dan dapat dilihat pada halaman Tiket Saya."
        }

        contains("berangkat", ignoreCase = true) -> {
            "Kapal Anda akan segera berangkat. Pastikan data tiket, waktu keberangkatan, dan pelabuhan keberangkatan sudah sesuai."
        }

        contains("perubahan", ignoreCase = true) -> {
            "Terdapat perubahan pada jadwal keberangkatan. Silakan periksa kembali detail perjalanan Anda."
        }

        contains("refund", ignoreCase = true) -> {
            "Pengajuan refund Anda sedang diproses. Status pengajuan dapat berubah sesuai proses verifikasi."
        }

        else -> this
    }
}