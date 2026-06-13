package com.dicoding.tugas_akhir.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
private val Success = Color(0xFF16A34A)
private val SuccessLight = Color(0xFFDCFCE7)
private val Warning = Color(0xFFF59E0B)
private val WarningLight = Color(0xFFFEF3C7)
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
            .clickable {
                if (!isWaitingPayment) {
                    onTicketClick()
                }
            },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = White,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Neutral200,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = ticket.shipName,
                        color = Black,
                    )

                    Text(
                        text = ticket.id,
                        color = Neutral500,
                    )
                }

                TicketStatusPill(status = ticket.status)
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "${ticket.origin} → ${ticket.destination}",
                    color = Primary2,
                )

                Text(
                    text = "${DateFormatter.formatDate(ticket.departureDate)}, ${ticket.departureTime}",
                    color = Neutral500,
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Neutral100,
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TicketSmallInfo(
                        title = "Kelas",
                        value = ticket.ticketClassName,
                        modifier = Modifier.weight(1f),
                    )

                    TicketSmallInfo(
                        title = "Penumpang",
                        value = "${ticket.passengerCount} orang",
                        modifier = Modifier.weight(1f),
                    )

                    TicketSmallInfo(
                        title = "Total",
                        value = PriceFormatter.formatToRupiah(ticket.totalPrice),
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            if (isWaitingPayment) {
                Button(
                    onClick = onPayNowClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary2,
                        contentColor = White,
                    ),
                ) {
                    Text("Bayar Sekarang")
                }
            } else {
                OutlinedButton(
                    onClick = onTicketClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Primary2,
                    ),
                    border = BorderStroke(1.dp, Primary2),
                ) {
                    Text("Lihat E-Ticket")
                }
            }
        }
    }
}

@Composable
private fun TicketSmallInfo(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            text = title,
            color = Neutral500,
        )

        Text(
            text = value,
            color = Black,
        )
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
        )
    }
}