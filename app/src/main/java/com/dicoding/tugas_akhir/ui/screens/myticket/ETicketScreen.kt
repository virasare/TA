package com.dicoding.tugas_akhir.ui.screens.ticket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.ETicketData
import com.dicoding.tugas_akhir.data.dummy.PassengerData
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun ETicketScreen(
    ticketData: ETicketData?,
    onBackClick: () -> Unit
) {
    if (ticketData == null) {
        EmptyETicketState()
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
                top = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                ETicketMainCard(
                    ticketData = ticketData
                )
            }

            item {
                PortInfoCard(
                    originPort = ticketData.originPort,
                    destinationPort = ticketData.destinationPort
                )
            }

            item {
                RefundInfoCard()
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Background
        ) {
            PrimaryButton(
                text = "Kembali ke Pesanan",
                onClick = onBackClick,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun ETicketMainCard(
    ticketData: ETicketData
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ETicketHeader(
                status = ticketData.status
            )

            FakeQrCode(
                modifier = Modifier
                    .padding(top = 22.dp)
                    .size(150.dp)
            )

            Text(
                text = "Tunjukkan QR Code ini saat proses keberangkatan",
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = "Kode Booking",
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 18.dp)
            )

            Text(
                text = ticketData.bookingCode,
                color = Primary2,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            HorizontalDivider(
                color = Neutral200,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            TicketTripDetailSection(
                ticketData = ticketData
            )

            HorizontalDivider(
                color = Neutral200,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            PassengerDetailSection(
                passengers = ticketData.passengers
            )
        }
    }
}

@Composable
private fun ETicketHeader(
    status: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "E-Ticket Kapal",
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )

        TicketStatusBadge(status = status)
    }
}

@Composable
private fun TicketStatusBadge(
    status: String
) {
    val backgroundColor = when (status.lowercase()) {
        "aktif" -> Color(0xFFE8F8EF)
        "selesai" -> Color(0xFFE8F2FF)
        "menunggu bayar", "menunggu pembayaran" -> Color(0xFFFFF4D8)
        "dibatalkan" -> Color(0xFFF1F2F4)
        "refund diproses" -> Color(0xFFFFF4D8)
        "reschedule" -> Color(0xFFE8F2FF)
        else -> Color(0xFFE8F8EF)
    }

    val textColor = when (status.lowercase()) {
        "aktif" -> Color(0xFF1B9A59)
        "selesai" -> Primary2
        "menunggu bayar", "menunggu pembayaran" -> Color(0xFFD98A00)
        "dibatalkan" -> Neutral500
        "refund diproses" -> Color(0xFFD98A00)
        "reschedule" -> Primary2
        else -> Color(0xFF1B9A59)
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun TicketTripDetailSection(
    ticketData: ETicketData
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        SectionTitle(
            icon = Icons.Outlined.ConfirmationNumber,
            title = "Detail Perjalanan"
        )

        TicketRow(
            label = "Nama Kapal",
            value = ticketData.shipName
        )

        TicketRow(
            label = "Rute",
            value = ticketData.route
        )

        TicketRow(
            label = "Kelas Tiket",
            value = ticketData.ticketClass
        )

        TicketRow(
            label = "Jumlah Penumpang",
            value = "${ticketData.passengerCount} orang"
        )

        TicketRow(
            label = "Tanggal Keberangkatan",
            value = ticketData.departureDate
        )

        TicketRow(
            label = "Jam Keberangkatan",
            value = ticketData.departureTime
        )

        TicketRow(
            label = "Estimasi Tiba",
            value = "${ticketData.arrivalDate}, ${ticketData.arrivalTime}"
        )

        TicketRow(
            label = "Durasi Perjalanan",
            value = ticketData.duration
        )
    }
}

@Composable
private fun PassengerDetailSection(
    passengers: List<PassengerData>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(
            icon = Icons.Outlined.Person,
            title = "Data Penumpang"
        )

        if (passengers.isEmpty()) {
            TicketRow(
                label = "Penumpang",
                value = "-"
            )
        } else {
            passengers.forEachIndexed { index, passenger ->
                PassengerItem(
                    index = index,
                    passenger = passenger
                )
            }
        }
    }
}

@Composable
private fun PassengerItem(
    index: Int,
    passenger: PassengerData
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Primary3.copy(alpha = 0.45f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                text = "Penumpang ${index + 1}",
                color = Neutral700,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            TicketRow(
                label = "Nama",
                value = passenger.fullName.ifBlank { "-" }
            )

            TicketRow(
                label = "NIK",
                value = passenger.nik.ifBlank { "-" }
            )

            TicketRow(
                label = "Nomor Telepon",
                value = passenger.phoneNumber.ifBlank { "-" }
            )

            TicketRow(
                label = "Jenis Kelamin",
                value = passenger.gender.ifBlank { "-" }
            )
        }
    }
}

@Composable
private fun SectionTitle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 4.dp)
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
                    .size(18.dp)
            )
        }

        Text(
            text = title,
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
private fun TicketRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            color = Neutral700,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PortInfoCard(
    originPort: String,
    destinationPort: String
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Informasi Pelabuhan",
                color = Neutral700,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            PortRow(
                title = "Pelabuhan Asal",
                value = originPort
            )

            PortRow(
                title = "Pelabuhan Tujuan",
                value = destinationPort
            )
        }
    }
}

@Composable
private fun PortRow(
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
            modifier = Modifier.padding(start = 12.dp)
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
private fun RefundInfoCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Primary3,
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(
            text = "Refund dan reschedule tersedia sesuai ketentuan tiket.",
            color = Neutral700,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(14.dp)
        )
    }
}

@Composable
private fun FakeQrCode(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cellCount = 21
        val cellSize = size.minDimension / cellCount

        fun drawCell(row: Int, col: Int) {
            drawRect(
                color = Color.Black,
                topLeft = Offset(col * cellSize, row * cellSize),
                size = Size(cellSize, cellSize)
            )
        }

        fun finder(startRow: Int, startCol: Int) {
            for (r in 0..6) {
                for (c in 0..6) {
                    if (
                        r == 0 ||
                        r == 6 ||
                        c == 0 ||
                        c == 6 ||
                        (r in 2..4 && c in 2..4)
                    ) {
                        drawCell(startRow + r, startCol + c)
                    }
                }
            }
        }

        finder(0, 0)
        finder(0, 14)
        finder(14, 0)

        for (row in 0 until cellCount) {
            for (col in 0 until cellCount) {
                val isFinderArea =
                    row < 7 && col < 7 ||
                            row < 7 && col >= 14 ||
                            row >= 14 && col < 7

                if (!isFinderArea && ((row * col + row + col) % 3 == 0)) {
                    drawCell(row, col)
                }
            }
        }
    }
}

@Composable
private fun EmptyETicketState() {
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = Primary3,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ConfirmationNumber,
                        contentDescription = null,
                        tint = Primary2,
                        modifier = Modifier
                            .padding(18.dp)
                            .size(34.dp)
                    )
                }

                Text(
                    text = "Data E-Ticket belum tersedia",
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "E-Ticket akan muncul setelah data pesanan dipilih atau pembayaran berhasil.",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}