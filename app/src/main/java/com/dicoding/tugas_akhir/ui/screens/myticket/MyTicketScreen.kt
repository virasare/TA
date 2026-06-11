package com.dicoding.tugas_akhir.ui.screens.myticket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.BookingOrder
import com.dicoding.tugas_akhir.data.dummy.BookingStatus
import com.dicoding.tugas_akhir.data.dummy.dummyOrders
import com.dicoding.tugas_akhir.data.dummy.toRupiah
import com.dicoding.tugas_akhir.ui.components.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

private enum class OrderFilter(
    val label: String
) {
    All("Semua"),
    WaitingPayment("Menunggu Bayar"),
    Active("Aktif"),
    Completed("Selesai"),
    Cancelled("Dibatalkan")
}

@Composable
fun MyTicketScreen(
    onTicketClick: (BookingOrder) -> Unit
) {
    var selectedFilter by remember {
        mutableStateOf(OrderFilter.All)
    }

    val orders = remember(selectedFilter) {
        dummyOrders.filterByOrderFilter(selectedFilter)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding()
    ) {
        OrderFilterSection(
            selectedFilter = selectedFilter,
            onFilterSelected = {
                selectedFilter = it
            }
        )

        if (orders.isEmpty()) {
            EmptyOrderState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = 16.dp,
                    bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders) { order ->
                    OrderCard(
                        order = order,
                        onDetailClick = {
                            onTicketClick(order)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderFilterSection(
    selectedFilter: OrderFilter,
    onFilterSelected: (OrderFilter) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            top = 18.dp,
            bottom = 4.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(OrderFilter.entries.toList()) { filter ->
            OrderFilterChip(
                text = filter.label,
                selected = selectedFilter == filter,
                onClick = {
                    onFilterSelected(filter)
                }
            )
        }
    }
}

@Composable
private fun OrderFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (selected) Primary2 else White
    val contentColor = if (selected) White else Neutral700
    val borderColor = if (selected) Primary2 else Neutral200

    Surface(
        modifier = Modifier.clickable { onClick() },
        color = containerColor,
        shape = RoundedCornerShape(50.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun OrderCard(
    order: BookingOrder,
    onDetailClick: () -> Unit
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
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Kode Booking",
                        color = Neutral500,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = order.bookingCode,
                        color = Neutral700,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                OrderStatusBadge(status = order.status)
            }

            Text(
                text = order.shipName,
                color = Neutral700,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            OrderInfoRow(
                icon = Icons.Outlined.LocationOn,
                text = order.route
            )

            OrderInfoRow(
                icon = Icons.Outlined.CalendarMonth,
                text = "${order.departureDate}, ${order.departureTime}"
            )

            OrderInfoRow(
                icon = Icons.Outlined.ConfirmationNumber,
                text = "${order.ticketClass} • ${order.passengerCount} penumpang • ${order.totalPrice.toRupiah()}"
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Neutral200
            ) {
                Spacer(modifier = Modifier.size(1.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.status.toDescription(),
                    color = Neutral500,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )

                PrimaryButton(
                    text = if (order.status == BookingStatus.WaitingPayment) {
                        "Bayar"
                    } else {
                        "Detail"
                    },
                    onClick = onDetailClick
                )
            }
        }
    }
}

@Composable
private fun OrderInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
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
                    .size(18.dp)
            )
        }

        Text(
            text = text,
            color = Neutral500,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
private fun OrderStatusBadge(
    status: BookingStatus
) {
    val backgroundColor = when (status) {
        BookingStatus.Active -> ColorStatus.GreenBackground
        BookingStatus.WaitingPayment -> ColorStatus.YellowBackground
        BookingStatus.Completed -> ColorStatus.BlueBackground
        BookingStatus.Cancelled -> ColorStatus.GrayBackground
        BookingStatus.RefundProcess -> ColorStatus.YellowBackground
        BookingStatus.Rescheduled -> ColorStatus.BlueBackground
    }

    val textColor = when (status) {
        BookingStatus.Active -> ColorStatus.GreenText
        BookingStatus.WaitingPayment -> ColorStatus.YellowText
        BookingStatus.Completed -> Primary2
        BookingStatus.Cancelled -> Neutral500
        BookingStatus.RefundProcess -> ColorStatus.YellowText
        BookingStatus.Rescheduled -> Primary2
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = status.label,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun EmptyOrderState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Belum Ada Pesanan",
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Pesanan tiket kapal yang Anda buat akan muncul di sini.",
            color = Neutral500,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

private fun List<BookingOrder>.filterByOrderFilter(
    filter: OrderFilter
): List<BookingOrder> {
    return when (filter) {
        OrderFilter.All -> this
        OrderFilter.WaitingPayment -> filter {
            it.status == BookingStatus.WaitingPayment
        }
        OrderFilter.Active -> filter {
            it.status == BookingStatus.Active
        }
        OrderFilter.Completed -> filter {
            it.status == BookingStatus.Completed
        }
        OrderFilter.Cancelled -> filter {
            it.status == BookingStatus.Cancelled
        }
    }
}

private fun BookingStatus.toDescription(): String {
    return when (this) {
        BookingStatus.Active -> "Tiket siap digunakan"
        BookingStatus.WaitingPayment -> "Selesaikan pembayaran"
        BookingStatus.Completed -> "Perjalanan selesai"
        BookingStatus.Cancelled -> "Pesanan dibatalkan"
        BookingStatus.RefundProcess -> "Refund sedang diproses"
        BookingStatus.Rescheduled -> "Jadwal berhasil diubah"
    }
}

private object ColorStatus {
    val GreenBackground = androidx.compose.ui.graphics.Color(0xFFE8F8EF)
    val GreenText = androidx.compose.ui.graphics.Color(0xFF1B9A59)
    val YellowBackground = androidx.compose.ui.graphics.Color(0xFFFFF4D8)
    val YellowText = androidx.compose.ui.graphics.Color(0xFFD98A00)
    val BlueBackground = androidx.compose.ui.graphics.Color(0xFFE8F2FF)
    val GrayBackground = androidx.compose.ui.graphics.Color(0xFFF1F2F4)
}