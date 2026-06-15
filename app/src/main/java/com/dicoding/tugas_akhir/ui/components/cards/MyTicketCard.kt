package com.dicoding.tugas_akhir.ui.components.cards

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Sailing
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.core.utils.DateFormatter
import com.dicoding.tugas_akhir.core.utils.PriceFormatter
import com.dicoding.tugas_akhir.domain.model.Booking

private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF111827)
private val Primary2 = Color(0xFF1976D2)
private val Primary3 = Color(0xFFE8F2FD)
private val Neutral100 = Color(0xFFF3F4F6)
private val Neutral200 = Color(0xFFE5E7EB)
private val Neutral500 = Color(0xFF6B7280)
private val Neutral700 = Color(0xFF374151)
private val Success = Color(0xFF16A34A)
private val SuccessLight = Color(0xFFDCFCE7)
private val Warning = Color(0xFFF59E0B)
private val WarningLight = Color(0xFFFEF3C7)
private val Danger = Color(0xFFDC2626)
private val DangerLight = Color(0xFFFEE2E2)
private val Info = Color(0xFF2563EB)
private val InfoLight = Color(0xFFDBEAFE)

@Composable
fun MyTicketCard(
    ticket: Booking,
    onTicketClick: () -> Unit,
    onPayNowClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isWaitingPayment = ticket.status.equals(
        other = "Menunggu Pembayaran",
        ignoreCase = true,
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = !isWaitingPayment,
                onClick = onTicketClick,
            ),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = White,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Neutral200,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            TicketHeaderSection(ticket = ticket)

            RouteSection(ticket = ticket)

            TicketInfoSection(ticket = ticket)

            TicketActionSection(
                isWaitingPayment = isWaitingPayment,
                onTicketClick = onTicketClick,
                onPayNowClick = onPayNowClick,
            )
        }
    }
}

@Composable
private fun TicketHeaderSection(
    ticket: Booking,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Surface(
            modifier = Modifier.size(46.dp),
            shape = RoundedCornerShape(16.dp),
            color = Primary3,
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Sailing,
                    contentDescription = null,
                    tint = Primary2,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text = ticket.shipName,
                color = Black,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Surface(
                shape = RoundedCornerShape(50),
                color = Neutral100,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ConfirmationNumber,
                        contentDescription = null,
                        tint = Neutral500,
                        modifier = Modifier.size(14.dp),
                    )

                    Text(
                        text = ticket.id,
                        color = Neutral500,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        TicketStatusPill(status = ticket.status)
    }
}

@Composable
private fun RouteSection(
    ticket: Booking,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Primary3,
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFD7EAFE),
        ),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Text(
                        text = ticket.origin,
                        color = Primary2,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = "Pelabuhan asal",
                        color = Neutral500,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = White,
                    border = BorderStroke(1.dp, Neutral200),
                ) {
                    Text(
                        text = "→",
                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                        color = Primary2,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Text(
                        text = ticket.destination,
                        color = Primary2,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = "Pelabuhan tujuan",
                        color = Neutral500,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = White,
            ) {
                Text(
                    text = "${DateFormatter.formatDate(ticket.departureDate)}, ${ticket.departureTime}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                    color = Neutral700,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun TicketInfoSection(
    ticket: Booking,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Neutral100,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            TicketSmallInfo(
                icon = Icons.Outlined.EventSeat,
                title = "Kelas",
                value = ticket.ticketClassName,
                modifier = Modifier.weight(1f),
            )

            TicketSmallInfo(
                icon = Icons.Outlined.People,
                title = "Penumpang",
                value = "${ticket.passengerCount} orang",
                modifier = Modifier.weight(1f),
            )

            TicketSmallInfo(
                icon = Icons.Outlined.Payments,
                title = "Total",
                value = PriceFormatter.formatToRupiah(ticket.totalPrice),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun TicketSmallInfo(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary2,
            modifier = Modifier.size(18.dp),
        )

        Text(
            text = title,
            color = Neutral500,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
        )

        Text(
            text = value,
            color = Black,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun TicketActionSection(
    isWaitingPayment: Boolean,
    onTicketClick: () -> Unit,
    onPayNowClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isWaitingPayment) {
        Button(
            onClick = onPayNowClick,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary2,
                contentColor = White,
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        ) {
            Text(
                text = "Bayar Sekarang",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    } else {
        OutlinedButton(
            onClick = onTicketClick,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Primary2,
            ),
            border = BorderStroke(1.dp, Primary2),
        ) {
            Text(
                text = "Lihat E-Ticket",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun TicketStatusPill(
    status: String,
    modifier: Modifier = Modifier,
) {
    val backgroundColor: Color
    val contentColor: Color

    when {
        status.equals("Aktif", ignoreCase = true) -> {
            backgroundColor = SuccessLight
            contentColor = Success
        }

        status.equals("Menunggu Pembayaran", ignoreCase = true) -> {
            backgroundColor = WarningLight
            contentColor = Warning
        }

        status.equals("Dibatalkan", ignoreCase = true) -> {
            backgroundColor = DangerLight
            contentColor = Danger
        }

        status.equals("Selesai", ignoreCase = true) -> {
            backgroundColor = Neutral200
            contentColor = Neutral500
        }

        else -> {
            backgroundColor = InfoLight
            contentColor = Info
        }
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = backgroundColor,
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            color = contentColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
    }
}