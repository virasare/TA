package com.dicoding.tugas_akhir.ui.screens.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.data.dummy.TicketClassOption
import com.dicoding.tugas_akhir.data.dummy.toRupiah
import com.dicoding.tugas_akhir.ui.components.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.buttons.SecondaryButton
import com.dicoding.tugas_akhir.ui.theme.*

@Composable
fun PaymentWaitingScreen(
    schedule: ShipSchedule?,
    selectedTicket: TicketClassOption?,
    onCancelClick: () -> Unit,
    onCheckStatusClick: () -> Unit
) {
    if (schedule == null || selectedTicket == null) {
        Text(text = "Data pembayaran tidak ditemukan")
        return
    }

    val adminFee = 5000
    val ticketPrice = selectedTicket.price * selectedTicket.passengerCount
    val totalPrice = ticketPrice + adminFee

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                WaitingScheduleCard(schedule = schedule)
            }

            item {
                WaitingPaymentSummaryCard(
                    ticketPrice = ticketPrice,
                    adminFee = adminFee,
                    totalPrice = totalPrice
                )
            }

            item {
                PaymentStepsCard()
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ColorWarningBackground
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = ColorWarningText,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = "Selesaikan pembayaran sebelum batas waktu berakhir.",
                    color = Neutral700,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                text = "Batalkan Pesanan",
                onClick = onCancelClick,
                modifier = Modifier.weight(1f)
            )

            PrimaryButton(
                text = "Cek Status Bayar",
                onClick = onCheckStatusClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun WaitingScheduleCard(
    schedule: ShipSchedule
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = White,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = schedule.shipName,
                color = Neutral700,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = Primary2,
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    text = schedule.route,
                    color = Neutral500,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(
                text = "${schedule.departureDate}, ${schedule.departureTime}",
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun WaitingPaymentSummaryCard(
    ticketPrice: Int,
    adminFee: Int,
    totalPrice: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = White,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Ringkasan Pembayaran",
                color = Neutral700,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            SummaryRow("Harga Tiket", ticketPrice.toRupiah())
            SummaryRow("Biaya Admin", adminFee.toRupiah())

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Primary3,
                shape = RoundedCornerShape(10.dp)
            ) {
                SummaryRow(
                    label = "Total",
                    value = totalPrice.toRupiah(),
                    valueColor = Primary2,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun PaymentStepsCard() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Langkah Pembayaran",
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        listOf(
            "Buka aplikasi bank/e-wallet.",
            "Pilih menu pembayaran.",
            "Masukkan nomor pembayaran.",
            "Konfirmasi pembayaran."
        ).forEachIndexed { index, step ->
            Text(
                text = "${index + 1}. $step",
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = Neutral700,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Neutral500,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            color = valueColor,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private val ColorWarningBackground = androidx.compose.ui.graphics.Color(0xFFFFF4D8)
private val ColorWarningText = androidx.compose.ui.graphics.Color(0xFFD98A00)