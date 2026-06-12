package com.dicoding.tugas_akhir.ui.screens.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DirectionsBoat
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.PassengerData
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.data.dummy.TicketClassOption
import com.dicoding.tugas_akhir.data.dummy.toRupiah
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White
import androidx.compose.foundation.layout.ColumnScope

@Composable
fun BookingSummaryScreen(
    schedule: ShipSchedule?,
    selectedTicket: TicketClassOption?,
    passengerData: PassengerData,
    onBackClick: () -> Unit,
    onPaymentClick: () -> Unit
) {
    if (schedule == null || selectedTicket == null) {
        BookingSummaryEmptyState()
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
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PassengerSummaryCard(
                    passengerData = passengerData
                )
            }

            item {
                TripSummaryCard(
                    schedule = schedule
                )
            }

            item {
                PaymentSummaryCard(
                    ticketClassName = selectedTicket.name,
                    passengerCount = selectedTicket.passengerCount,
                    ticketPrice = ticketPrice,
                    adminFee = adminFee,
                    totalPrice = totalPrice
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Background
        ) {
            PrimaryButton(
                text = "Lanjut Bayar",
                onClick = onPaymentClick,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun PassengerSummaryCard(
    passengerData: PassengerData
) {
    SummaryCard {
        SectionHeader(
            icon = Icons.Outlined.Person,
            title = "Data Penumpang"
        )

        Text(
            text = "Periksa kembali data penumpang",
            color = Neutral500,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp, bottom = 14.dp)
        )

        SummaryTextRow(
            label = "Nama Lengkap",
            value = passengerData.fullName.ifBlank { "Vira Sare" }
        )

        SummaryTextRow(
            label = "Nomor Induk Keluarga (NIK)",
            value = passengerData.nik.ifBlank { "5308xxxxxxxxxxxx" }
        )

        SummaryTextRow(
            label = "Nomor Telepon",
            value = passengerData.phoneNumber.ifBlank { "0812xxxxxxxx" }
        )

        SummaryTextRow(
            label = "Jenis Kelamin",
            value = passengerData.gender.ifBlank { "Perempuan" }
        )
    }
}

@Composable
private fun TripSummaryCard(
    schedule: ShipSchedule
) {
    SummaryCard {
        SectionHeader(
            icon = Icons.Outlined.DirectionsBoat,
            title = "Detail Perjalanan"
        )

        Column(
            modifier = Modifier.padding(top = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TripInfoRow(
                icon = Icons.Outlined.LocationOn,
                label = "Rute",
                value = schedule.route
            )

            TripInfoRow(
                icon = Icons.Outlined.CalendarMonth,
                label = "Tanggal Keberangkatan",
                value = schedule.departureDate
            )

            TripInfoRow(
                icon = Icons.Outlined.AccessTime,
                label = "Jam Keberangkatan",
                value = schedule.departureTime
            )

            TripInfoRow(
                icon = Icons.Outlined.AccessTime,
                label = "Estimasi Tiba",
                value = "${schedule.arrivalDate}, ${schedule.arrivalTime}"
            )

            TripInfoRow(
                icon = Icons.Outlined.AccessTime,
                label = "Durasi Perjalanan",
                value = schedule.duration
            )
        }
    }
}

@Composable
private fun PaymentSummaryCard(
    ticketClassName: String,
    passengerCount: Int,
    ticketPrice: Int,
    adminFee: Int,
    totalPrice: Int
) {
    SummaryCard {
        Text(
            text = "Ringkasan Pembayaran",
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            modifier = Modifier.padding(top = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryTextRow(
                label = "Kelas Tiket",
                value = ticketClassName
            )

            SummaryTextRow(
                label = "Jumlah Penumpang",
                value = "$passengerCount orang"
            )

            SummaryTextRow(
                label = "Harga Tiket",
                value = ticketPrice.toRupiah()
            )

            SummaryTextRow(
                label = "Biaya Admin",
                value = adminFee.toRupiah()
            )

            HorizontalDivider(
                color = Neutral200,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Primary3,
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total",
                        color = Neutral700,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = totalPrice.toRupiah(),
                        color = Primary2,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    content: @Composable ColumnScope.() -> Unit
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
            content = content
        )
    }
}

@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Primary3,
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Primary2,
                modifier = Modifier
                    .padding(8.dp)
                    .size(22.dp)
            )
        }

        Text(
            text = title,
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
private fun SummaryTextRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = Neutral700
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.Top
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
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun TripInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary2,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = label,
            color = Neutral500,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        )

        Text(
            text = value,
            color = Neutral700,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun BookingSummaryEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ringkasan pesanan belum tersedia",
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Pilih jadwal dan tiket terlebih dahulu sebelum melanjutkan ke pembayaran.",
            color = Neutral500,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}