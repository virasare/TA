package com.dicoding.tugas_akhir.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.DirectionsBoat
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Sailing
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

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
    status: ShipScheduleStatus,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val statusText = when (status) {
        ShipScheduleStatus.Available -> "Tersedia"
        ShipScheduleStatus.Limited -> "Terbatas"
        ShipScheduleStatus.Unavailable -> "Habis"
    }

    val statusTextColor = when (status) {
        ShipScheduleStatus.Available -> Color(0xFF1B8A4B)
        ShipScheduleStatus.Limited -> Color(0xFFC47A00)
        ShipScheduleStatus.Unavailable -> Color(0xFFD32F2F)
    }
    val statusContainerColor = statusTextColor.copy(alpha = 0.14f)

    val isUnavailable = status == ShipScheduleStatus.Unavailable

    val cardContainerColor = colors.surface

    val cardBorderColor = colors.outlineVariant

    val cardShadowElevation = if (isUnavailable) {
        0.dp
    } else {
        2.dp
    }

    val iconContainerColor = if (isUnavailable) {
        colors.errorContainer.copy(alpha = 0.58f)
    } else {
        colors.primaryContainer.copy(alpha = 0.58f)
    }

    val iconTintColor = if (isUnavailable) {
        colors.error
    } else {
        colors.primary
    }

    val titleColor = if (isUnavailable) {
        colors.onSurfaceVariant
    } else {
        colors.onSurface
    }

    val bodyColor = colors.onSurfaceVariant

    var showSoldOutDialog by remember {
        mutableStateOf(false)
    }

    if (showSoldOutDialog) {
        AlertDialog(
            onDismissRequest = {
                showSoldOutDialog = false
            },
            containerColor = colors.surface,
            titleContentColor = colors.onSurface,
            textContentColor = colors.onSurfaceVariant,
            title = {
                Text("Tiket sudah habis")
            },
            text = {
                Text("Kuota jadwal ini sudah habis. Silakan pilih jadwal lain atau ubah pencarian untuk melihat alternatif keberangkatan.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSoldOutDialog = false
                    },
                ) {
                    Text(
                        text = "Mengerti",
                        color = colors.primary,
                    )
                }
            },
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (isUnavailable) {
                    showSoldOutDialog = true
                } else {
                    onClick()
                }
            }
            .alpha(if (isUnavailable) 0.92f else 1f),
        shape = RoundedCornerShape(18.dp),
        color = cardContainerColor,
        border = BorderStroke(1.dp, cardBorderColor),
        shadowElevation = cardShadowElevation
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = iconContainerColor
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DirectionsBoat,
                            contentDescription = null,
                            tint = iconTintColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = shipName,
                        color = titleColor,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = route,
                        color = bodyColor,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                StatusPill(
                    text = statusText,
                    containerColor = statusContainerColor,
                    textColor = statusTextColor
                )
            }

            Divider(color = colors.outlineVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScheduleMiniInfo(
                    icon = Icons.Outlined.AccessTime,
                    label = "Berangkat",
                    value = "$departureDate, $departureTime",
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                ScheduleMiniInfo(
                    icon = Icons.Outlined.Sailing,
                    label = "Tiba",
                    value = arrivalTime,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.EventSeat,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = if (isUnavailable) "Kuota habis" else "$quota tersedia",
                        color = bodyColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = duration,
                        color = colors.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Payments,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(17.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = price,
                            color = colors.primary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleMiniInfo(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = CircleShape,
            color = colors.primaryContainer.copy(alpha = 0.58f)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                text = value,
                color = colors.onSurface,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StatusPill(
    text: String,
    containerColor: Color,
    textColor: Color
) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = containerColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
