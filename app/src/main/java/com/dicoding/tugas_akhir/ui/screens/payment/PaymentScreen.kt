package com.dicoding.tugas_akhir.ui.screens.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.PassengerData
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.data.dummy.TicketClassOption
import com.dicoding.tugas_akhir.data.dummy.toRupiah
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.theme.*

private enum class PaymentMethod(
    val title: String,
    val description: String,
    val icon: ImageVector
) {
    VirtualAccount(
        title = "Transfer Bank",
        description = "Bayar melalui virtual account bank",
        icon = Icons.Outlined.AccountBalance
    ),
    Qris(
        title = "QRIS",
        description = "Bayar menggunakan e-wallet atau mobile banking",
        icon = Icons.Outlined.CreditCard
    )
}

@Composable
fun PaymentScreen(
    schedule: ShipSchedule?,
    selectedTicket: TicketClassOption?,
    passengerList: List<PassengerData>,
    onBackClick: () -> Unit,
    onPayNowClick: () -> Unit
) {
    if (schedule == null || selectedTicket == null) {
        PaymentEmptyState()
        return
    }

    var selectedMethod by remember {
        mutableStateOf(PaymentMethod.VirtualAccount)
    }

    val adminFee = 5000
    val ticketPrice = selectedTicket.price * selectedTicket.passengerCount
    val totalPrice = ticketPrice + adminFee
    val firstPassenger = passengerList.firstOrNull()

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
                PaymentPassengerCard(
                    passenger = firstPassenger,
                    passengerCount = selectedTicket.passengerCount
                )
            }

            item {
                PaymentSummaryCard(
                    ticketPrice = ticketPrice,
                    adminFee = adminFee,
                    totalPrice = totalPrice
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PaymentMethod.entries.forEach { method ->
                        PaymentMethodItem(
                            method = method,
                            selected = selectedMethod == method,
                            onClick = {
                                selectedMethod = method
                            }
                        )
                    }
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Background
        ) {
            PrimaryButton(
                text = "Bayar Sekarang",
                onClick = onPayNowClick,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun PaymentPassengerCard(
    passenger: PassengerData?,
    passengerCount: Int
) {
    PaymentCard {
        SectionHeader(
            icon = Icons.Outlined.Person,
            title = "Data Penumpang"
        )

        Text(
            text = "Periksa kembali data penumpang",
            color = Neutral500,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        )

        PaymentRow(
            label = "Nama Lengkap",
            value = passenger?.fullName?.ifBlank { "Vira Sare" } ?: "Vira Sare"
        )

        PaymentRow(
            label = "Jumlah Penumpang",
            value = "$passengerCount orang"
        )

        PaymentRow(
            label = "Nomor Telepon",
            value = passenger?.phoneNumber?.ifBlank { "0812xxxxxxxx" } ?: "0812xxxxxxxx"
        )
    }
}

@Composable
private fun PaymentSummaryCard(
    ticketPrice: Int,
    adminFee: Int,
    totalPrice: Int
) {
    PaymentCard {
        Text(
            text = "Ringkasan Pembayaran",
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PaymentRow(
                label = "Harga Tiket",
                value = ticketPrice.toRupiah()
            )

            PaymentRow(
                label = "Biaya Admin",
                value = adminFee.toRupiah()
            )

            HorizontalDivider(color = Neutral200)

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
private fun PaymentMethodItem(
    method: PaymentMethod,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) Primary2 else Neutral200

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (selected) Primary3 else Neutral200,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = method.icon,
                    contentDescription = null,
                    tint = if (selected) Primary2 else Neutral500,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(20.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = method.title,
                    color = Neutral700,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = method.description,
                    color = Neutral500,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            RadioButton(
                selected = selected,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun PaymentCard(
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
private fun PaymentRow(
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
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun PaymentEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Data pembayaran belum tersedia",
            color = Neutral500,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}