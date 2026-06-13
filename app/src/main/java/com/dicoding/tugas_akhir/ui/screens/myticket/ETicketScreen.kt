package com.dicoding.tugas_akhir.ui.screens.myticket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.core.utils.DateFormatter
import com.dicoding.tugas_akhir.domain.model.ETicket
import com.dicoding.tugas_akhir.ui.components.loading.BookingSummaryPlaceholder
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView
import com.dicoding.tugas_akhir.ui.components.ticket.FakeQrCode
import com.dicoding.tugas_akhir.ui.state.ETicketUiState
import com.dicoding.tugas_akhir.ui.viewmodel.MyTicketViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF111827)
private val Background = Color(0xFFF6F8FB)
private val Primary1 = Color(0xFF0B1F3A)
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
fun ETicketScreen(
    bookingId: String? = null,
    paymentId: String? = null,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyTicketViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val eTicketUiState by viewModel.eTicketUiState.collectAsStateWithLifecycle()

    LaunchedEffect(bookingId, paymentId) {
        when {
            paymentId != null -> viewModel.loadETicketByPaymentId(paymentId)
            bookingId != null -> viewModel.loadETicketByBookingId(bookingId)
        }
    }

    when (val state = eTicketUiState) {
        is ETicketUiState.Loading -> {
            BookingSummaryPlaceholder(
                modifier = modifier
                    .fillMaxSize()
                    .background(Background)
            )
        }

        is ETicketUiState.Success -> {
            ETicketContent(
                ticket = state.ticket,
                modifier = modifier,
            )
        }

        is ETicketUiState.Error -> {
            LottieStateView(
                animationFile = "lottie/error_connection.json",
                title = "E-Ticket Tidak Ditemukan",
                message = state.message,
                modifier = modifier
                    .fillMaxSize()
                    .background(Background),
            )
        }
    }
}

@Composable
private fun ETicketContent(
    ticket: ETicket,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("e_ticket_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ETicketMainCard(ticket = ticket)

        PassengerListCard(ticket = ticket)

        BoardingInfoCard(ticket = ticket)

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun ETicketMainCard(
    ticket: ETicket,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
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
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = "E-Ticket Kapal",
                        color = Black,
                    )

                    Text(
                        text = ticket.bookingCode,
                        color = Neutral500,
                    )
                }

                ETicketStatusPill(status = ticket.status)
            }

            RouteSection(ticket = ticket)

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = White,
                border = BorderStroke(
                    width = 1.dp,
                    color = Neutral200,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    FakeQrCode(
                        value = ticket.qrCode,
                    )

                    Text(
                        text = "Tunjukkan QR ini saat check-in di pelabuhan",
                        color = Neutral500,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            SoftDivider()

            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    TicketInfoItem(
                        title = "Kapal",
                        value = ticket.shipName,
                        modifier = Modifier.weight(1f),
                    )

                    TicketInfoItem(
                        title = "Kelas",
                        value = ticket.ticketClassName,
                        modifier = Modifier.weight(1f),
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    TicketInfoItem(
                        title = "Tanggal",
                        value = DateFormatter.formatDate(ticket.departureDate),
                        modifier = Modifier.weight(1f),
                    )

                    TicketInfoItem(
                        title = "Jam Berangkat",
                        value = ticket.departureTime,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun RouteSection(
    ticket: ETicket,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Primary3,
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFD7EAFE),
        ),
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = ticket.origin,
                    color = Primary2,
                )

                Text(
                    text = "Pelabuhan asal",
                    color = Neutral500,
                )
            }

            Surface(
                shape = CircleShape,
                color = White,
                border = BorderStroke(
                    width = 1.dp,
                    color = Neutral200,
                ),
            ) {
                Text(
                    text = "→",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    color = Primary2,
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = ticket.destination,
                    color = Primary2,
                )

                Text(
                    text = "Pelabuhan tujuan",
                    color = Neutral500,
                )
            }
        }
    }
}

@Composable
private fun TicketInfoItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Neutral100,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
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
}

@Composable
private fun PassengerListCard(
    ticket: ETicket,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Data Penumpang",
                color = Black,
            )

            ticket.passengers.forEachIndexed { index, passenger ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Neutral100,
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "${index + 1}. ${passenger.fullName}",
                            color = Black,
                        )

                        Text(
                            text = "NIK: ${passenger.nik}",
                            color = Neutral500,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BoardingInfoCard(
    ticket: ETicket,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "Informasi Keberangkatan",
                color = Black,
            )

            BoardingInfoRow(
                title = "Terminal",
                value = ticket.terminal,
            )

            BoardingInfoRow(
                title = "Gate",
                value = ticket.gate,
            )

            BoardingInfoRow(
                title = "Diterbitkan",
                value = ticket.issuedAt,
            )

            SoftDivider()

            Text(
                text = ticket.note,
                color = Neutral500,
            )
        }
    }
}

@Composable
private fun BoardingInfoRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            color = Neutral500,
        )

        Text(
            text = value,
            color = Black,
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun SoftDivider(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 2.dp),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Neutral200,
        ) {}
    }
}

@Composable
private fun ETicketStatusPill(
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