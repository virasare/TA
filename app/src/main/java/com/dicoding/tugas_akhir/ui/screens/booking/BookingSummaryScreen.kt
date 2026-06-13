package com.dicoding.tugas_akhir.ui.screens.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.DirectionsBoat
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.core.utils.DateFormatter
import com.dicoding.tugas_akhir.core.utils.PriceFormatter
import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.loading.BookingSummaryPlaceholder
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView
import com.dicoding.tugas_akhir.ui.state.BookingDetailUiState
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White
import com.dicoding.tugas_akhir.ui.viewmodel.BookingViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

@Composable
fun BookingSummaryScreen(
    bookingId: String,
    onBackClick: () -> Unit,
    onPaymentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val bookingDetailUiState by viewModel.bookingDetailUiState.collectAsStateWithLifecycle()

    LaunchedEffect(bookingId) {
        viewModel.getBookingDetail(bookingId)
    }

    when (val state = bookingDetailUiState) {
        is BookingDetailUiState.Loading -> {
            BookingSummaryPlaceholder(
                modifier = modifier.fillMaxSize()
            )
        }

        is BookingDetailUiState.Success -> {
            BookingSummaryContent(
                booking = state.booking,
                onPaymentClick = {
                    onPaymentClick(state.booking.id)
                },
                modifier = modifier
            )
        }

        is BookingDetailUiState.Error -> {
            LottieStateView(
                animationFile = "lottie/error_connection.json",
                title = "Pesanan tidak ditemukan",
                message = state.message,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun BookingSummaryContent(
    booking: Booking,
    onPaymentClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .testTag("booking_summary_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                BookingSummaryHeader(booking = booking)
            }

            item {
                BookingStepCard()
            }

            item {
                SectionTitle(
                    title = "Cek Ringkasan Pesanan",
                    description = "Pastikan detail perjalanan, data penumpang, dan total pembayaran sudah benar sebelum memilih metode pembayaran."
                )
            }

            item {
                TravelSummaryCard(booking = booking)
            }

            item {
                PassengerSummaryCard(booking = booking)
            }

            item {
                PaymentSummaryCard(booking = booking)
            }

            item {
                PaymentNoteCard()
            }
        }

        BookingSummaryBottomBar(
            booking = booking,
            onPaymentClick = onPaymentClick
        )
    }
}

@Composable
private fun BookingSummaryHeader(
    booking: Booking,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                shape = RoundedCornerShape(15.dp),
                color = Primary3
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ConfirmationNumber,
                        contentDescription = null,
                        tint = Primary2,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Ringkasan Pesanan",
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${booking.passengerCount} penumpang • ${booking.ticketClassName}",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            SummaryChip(text = "Siap dicek")
        }
    }
}

@Composable
private fun BookingStepCard(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Proses Pemesanan",
                color = Neutral700,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepItem(
                    number = "1",
                    title = "Tiket",
                    active = false,
                    done = true,
                    modifier = Modifier.weight(1f)
                )

                StepLine(modifier = Modifier.weight(0.35f))

                StepItem(
                    number = "2",
                    title = "Data",
                    active = false,
                    done = true,
                    modifier = Modifier.weight(1f)
                )

                StepLine(modifier = Modifier.weight(0.35f))

                StepItem(
                    number = "3",
                    title = "Ringkasan",
                    active = true,
                    done = false,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StepItem(
    number: String,
    title: String,
    active: Boolean,
    done: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Surface(
            modifier = Modifier.size(30.dp),
            shape = CircleShape,
            color = if (active || done) Primary2 else Background,
            border = BorderStroke(
                width = 1.dp,
                color = if (active || done) Primary2 else Neutral200
            )
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (done) {
                    Icon(
                        imageVector = Icons.Outlined.TaskAlt,
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(17.dp)
                    )
                } else {
                    Text(
                        text = number,
                        color = if (active) White else Neutral500,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        Text(
            text = title,
            color = if (active) Primary2 else Neutral500,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StepLine(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier.padding(horizontal = 4.dp),
        color = Neutral200
    )
}

@Composable
private fun SectionTitle(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = description,
            color = Neutral500,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun TravelSummaryCard(
    booking: Booking,
    modifier: Modifier = Modifier
) {
    SummaryCard(
        title = "Detail Perjalanan",
        icon = Icons.Outlined.DirectionsBoat,
        modifier = modifier
    ) {
        Text(
            text = booking.shipName,
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        SummaryInfoRow(
            icon = Icons.Outlined.LocationOn,
            label = "Rute",
            value = "${booking.origin} → ${booking.destination}"
        )

        SummaryInfoRow(
            icon = Icons.Outlined.CalendarMonth,
            label = "Keberangkatan",
            value = "${DateFormatter.formatDate(booking.departureDate)}, ${booking.departureTime}"
        )

        SummaryInfoRow(
            icon = Icons.Outlined.EventSeat,
            label = "Kelas",
            value = booking.ticketClassName
        )
    }
}

@Composable
private fun PassengerSummaryCard(
    booking: Booking,
    modifier: Modifier = Modifier
) {
    SummaryCard(
        title = "Data Penumpang",
        icon = Icons.Outlined.Person,
        modifier = modifier
    ) {
        booking.passengers.forEachIndexed { index, passenger ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Background,
                border = BorderStroke(1.dp, Neutral200)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        modifier = Modifier.size(34.dp),
                        shape = CircleShape,
                        color = Primary3
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = Primary2,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = passenger.fullName,
                            color = Neutral700,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "NIK: ${passenger.nik}",
                            color = Neutral500,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "No. HP: ${passenger.phoneNumber}",
                            color = Neutral500,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentSummaryCard(
    booking: Booking,
    modifier: Modifier = Modifier
) {
    SummaryCard(
        title = "Estimasi Pembayaran",
        icon = Icons.Outlined.Payments,
        modifier = modifier
    ) {
        PriceRow(
            title = "${booking.ticketClassName} x ${booking.passengerCount}",
            value = PriceFormatter.formatToRupiah(
                booking.ticketPrice * booking.passengerCount
            )
        )

        PriceRow(
            title = "Biaya Admin",
            value = PriceFormatter.formatToRupiah(booking.adminFee)
        )

        HorizontalDivider(color = Neutral200)

        PriceRow(
            title = "Total",
            value = PriceFormatter.formatToRupiah(booking.totalPrice),
            isTotal = true
        )

        Text(
            text = "Metode pembayaran belum dipilih.",
            color = Neutral500,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun PaymentNoteCard(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Primary3,
        border = BorderStroke(1.dp, Neutral200)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = Primary2,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = "Pesanan belum masuk status Menunggu Pembayaran. Status tersebut akan muncul setelah kamu memilih metode pembayaran dan menekan tombol Bayar.",
                color = Neutral700,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(38.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Primary3
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Primary2,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = title,
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            HorizontalDivider(color = Neutral200)

            content()
        }
    }
}

@Composable
private fun SummaryInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = Neutral500,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = value,
            modifier = Modifier.weight(1.4f),
            color = Neutral700,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun PriceRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    isTotal: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = if (isTotal) Neutral700 else Neutral500,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            style = if (isTotal) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.bodyMedium
            }
        )

        Text(
            text = value,
            color = if (isTotal) Primary2 else Neutral700,
            fontWeight = FontWeight.Bold,
            style = if (isTotal) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.bodyMedium
            }
        )
    }
}

@Composable
private fun SummaryChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        color = Primary3,
        border = BorderStroke(1.dp, Neutral200)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            color = Primary2,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun BookingSummaryBottomBar(
    booking: Booking,
    onPaymentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Total Pembayaran",
                        color = Neutral500,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = PriceFormatter.formatToRupiah(booking.totalPrice),
                        color = Neutral700,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Primary3
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Wallet,
                            contentDescription = null,
                            tint = Primary2,
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.size(5.dp))

                        Text(
                            text = "Step 3/3",
                            color = Primary2,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            PrimaryButton(
                text = "Pilih Metode Pembayaran",
                onClick = onPaymentClick,
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}