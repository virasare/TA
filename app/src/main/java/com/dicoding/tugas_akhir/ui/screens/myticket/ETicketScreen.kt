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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.io.FileOutputStream

private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF111827)
private val Background = Color(0xFFF6F8FB)
private val Primary1 = Color(0xFF0B1F3A)
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
fun ETicketScreen(
    bookingId: String? = null,
    paymentId: String? = null,
    downloadRequest: Int = 0,
    onBackClick: () -> Unit,
    onRefundClick: (String) -> Unit = {},
    onRescheduleClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: MyTicketViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val eTicketUiState by viewModel.eTicketUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var handledDownloadRequest by rememberSaveable(bookingId, paymentId) {
        mutableStateOf(downloadRequest)
    }

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
                    .background(MaterialTheme.colorScheme.background)
            )
        }

        is ETicketUiState.Success -> {
            LaunchedEffect(downloadRequest, state.ticket.bookingId) {
                if (downloadRequest > handledDownloadRequest) {
                    handledDownloadRequest = downloadRequest
                    downloadETicketPdf(
                        context = context,
                        ticket = state.ticket,
                    )
                }
            }
            ETicketContent(
                ticket = state.ticket,
                onRefundClick = onRefundClick,
                onRescheduleClick = onRescheduleClick,
                modifier = modifier,
            )
        }

        is ETicketUiState.Error -> {
            LottieStateView(
                animationFile = "no_internet.json",
                title = "E-Ticket Tidak Ditemukan",
                message = state.message,
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            )
        }
    }
}

@Composable
private fun ETicketContent(
    ticket: ETicket,
    onRefundClick: (String) -> Unit,
    onRescheduleClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("e_ticket_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ETicketBoardingPassCard(ticket = ticket)

        PassengerListCard(ticket = ticket)

        BoardingInfoCard(ticket = ticket)

        ImportantInfoCard()

        if (ticket.status.equals("Aktif", ignoreCase = true)) {
            ManageTicketCard(
                bookingId = ticket.bookingId,
                onRefundClick = onRefundClick,
                onRescheduleClick = onRescheduleClick,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun ETicketBoardingPassCard(
    ticket: ETicket,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = colors.outlineVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Primary2,
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            Text(
                                text = "E-Ticket Kapal",
                                color = White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                            )

                            Text(
                                text = "Kode Booking: ${ticket.bookingCode}",
                                color = White.copy(alpha = 0.82f),
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        ETicketStatusPill(status = ticket.status)
                    }

                    RouteHeroSection(ticket = ticket)
                }
            }

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                QrTicketSection(ticket = ticket)

                TicketPerforationDivider()

                TicketDetailGrid(ticket = ticket)
            }
        }
    }
}

@Composable
private fun RouteHeroSection(
    ticket: ETicket,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = White.copy(alpha = 0.14f),
        border = BorderStroke(
            width = 1.dp,
            color = White.copy(alpha = 0.18f),
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = ticket.origin,
                    color = White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = "Pelabuhan asal",
                    color = White.copy(alpha = 0.76f),
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            Surface(
                shape = CircleShape,
                color = White,
            ) {
                Text(
                    text = "→",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    color = Primary2,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = ticket.destination,
                    color = White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = "Pelabuhan tujuan",
                    color = White.copy(alpha = 0.76f),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
private fun QrTicketSection(
    ticket: ETicket,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = colors.surfaceVariant,
            border = BorderStroke(
                width = 1.dp,
                color = colors.outlineVariant,
            ),
        ) {
            Box(
                modifier = Modifier.padding(18.dp),
                contentAlignment = Alignment.Center,
            ) {
                FakeQrCode(
                    value = ticket.qrCode,
                    qrSize = 220.dp,
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text = "Scan QR saat check-in",
                color = colors.onSurface,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Text(
                text = "Tunjukkan kode ini kepada petugas pelabuhan. Pastikan layar cukup terang.",
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun TicketDetailGrid(
    ticket: ETicket,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
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
                title = "Jam",
                value = ticket.departureTime,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun TicketInfoItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = colors.surfaceVariant,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
            )

            Text(
                text = value,
                color = colors.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PassengerListCard(
    ticket: ETicket,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = colors.outlineVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SectionHeader(
                title = "Data Penumpang",
                description = "${ticket.passengers.size} penumpang terdaftar pada tiket ini.",
            )

            ticket.passengers.forEachIndexed { index, passenger ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = colors.surfaceVariant,
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = colors.primaryContainer,
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = colors.primary,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = passenger.fullName,
                                color = colors.onSurface,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )

                            Text(
                                text = "NIK: ${passenger.nik}",
                                color = colors.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
                            )

                            Text(
                                text = "No. HP: ${passenger.phoneNumber}",
                                color = colors.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
                            )

                            Text(
                                text = "Tanggal Lahir: ${passenger.birthDate.ifBlank { "-" }}",
                                color = colors.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
                            )

                            Text(
                                text = "Jenis Kelamin: ${passenger.gender.ifBlank { "-" }}",
                                color = colors.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ManageTicketCard(
    bookingId: String,
    onRefundClick: (String) -> Unit,
    onRescheduleClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = colors.outlineVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SectionHeader(
                title = "Kelola Tiket",
                description = "Gunakan menu ini untuk mengajukan refund atau mengubah jadwal keberangkatan.",
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = {
                        onRefundClick(bookingId)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Danger,
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Danger,
                    ),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(
                        text = "Refund",
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Button(
                    onClick = {
                        onRescheduleClick(bookingId)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary2,
                        contentColor = White,
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                ) {
                    Text(
                        text = "Reschedule",
                        fontWeight = FontWeight.SemiBold,
                    )
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
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = colors.outlineVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SectionHeader(
                title = "Informasi Keberangkatan",
                description = "Periksa terminal dan gate sebelum berangkat.",
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
                title = "Transit",
                value = ticket.transitInfo,
            )

            BoardingInfoRow(
                title = "Diterbitkan",
                value = ticket.issuedAt,
            )

            TicketPerforationDivider()

            Text(
                text = ticket.note,
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun ImportantInfoCard(
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = colors.surfaceVariant,
        border = BorderStroke(
            width = 1.dp,
            color = colors.outlineVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "Informasi Penting",
                color = colors.onSurface,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "Datang lebih awal ke pelabuhan dan siapkan identitas sesuai data penumpang. E-ticket ini berlaku sebagai bukti pemesanan yang sah.",
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            color = colors.onSurface,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = description,
            color = colors.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun BoardingInfoRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = title,
            color = colors.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = value,
            color = colors.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun TicketPerforationDivider(
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = Modifier.size(10.dp),
            shape = CircleShape,
            color = colors.background,
        ) {}

        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .padding(horizontal = 6.dp),
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colors.outlineVariant,
            ) {}
        }

        Surface(
            modifier = Modifier.size(10.dp),
            shape = CircleShape,
            color = colors.background,
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
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
        )
    }
}

private fun drawPdfLabelValue(
    canvas: android.graphics.Canvas,
    label: String,
    value: String,
    x: Float,
    y: Float,
    labelPaint: Paint,
    valuePaint: Paint,
) {
    canvas.drawText(label, x, y, labelPaint)
    canvas.drawText(value.take(28), x, y + 20f, valuePaint)
}

private fun drawDummyTicketQr(
    canvas: android.graphics.Canvas,
    value: String,
    left: Float,
    top: Float,
    size: Float,
) {
    val whitePaint = Paint().apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.FILL
    }
    val blackPaint = Paint().apply {
        color = android.graphics.Color.BLACK
        style = Paint.Style.FILL
    }
    val borderPaint = Paint().apply {
        color = android.graphics.Color.rgb(229, 231, 235)
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    canvas.drawRect(left, top, left + size, top + size, whitePaint)
    canvas.drawRect(left, top, left + size, top + size, borderPaint)

    val cells = 21
    val cellSize = size / cells
    val seed = value.hashCode().let { if (it < 0) -it else it }

    fun drawCell(x: Int, y: Int) {
        canvas.drawRect(
            left + x * cellSize,
            top + y * cellSize,
            left + (x + 1) * cellSize,
            top + (y + 1) * cellSize,
            blackPaint,
        )
    }

    fun drawFinder(startX: Int, startY: Int) {
        for (x in 0 until 7) {
            for (y in 0 until 7) {
                val isBorder = x == 0 || y == 0 || x == 6 || y == 6
                val isCenter = x in 2..4 && y in 2..4
                if (isBorder || isCenter) {
                    drawCell(startX + x, startY + y)
                }
            }
        }
    }

    drawFinder(0, 0)
    drawFinder(14, 0)
    drawFinder(0, 14)

    for (x in 0 until cells) {
        for (y in 0 until cells) {
            val inFinder = (x in 0..6 && y in 0..6) ||
                    (x in 14..20 && y in 0..6) ||
                    (x in 0..6 && y in 14..20)

            val shouldDraw = !inFinder && ((x * 31 + y * 17 + seed) % 5) < 2
            if (shouldDraw) {
                drawCell(x, y)
            }
        }
    }
}

private fun downloadETicketPdf(
    context: Context,
    ticket: ETicket,
) {
    val fileName = "e-ticket-${ticket.bookingCode}.pdf"
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    val bluePaint = Paint().apply {
        color = android.graphics.Color.rgb(25, 118, 210)
        style = Paint.Style.FILL
    }

    val lightPaint = Paint().apply {
        color = android.graphics.Color.rgb(232, 242, 253)
        style = Paint.Style.FILL
    }

    val titlePaint = Paint().apply {
        textSize = 24f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = android.graphics.Color.WHITE
    }

    val whiteBodyPaint = Paint().apply {
        textSize = 13f
        color = android.graphics.Color.WHITE
    }

    val sectionPaint = Paint().apply {
        textSize = 18f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = android.graphics.Color.rgb(17, 24, 39)
    }

    val labelPaint = Paint().apply {
        textSize = 11f
        color = android.graphics.Color.rgb(107, 114, 128)
    }

    val bodyPaint = Paint().apply {
        textSize = 14f
        color = android.graphics.Color.rgb(17, 24, 39)
    }

    val boldPaint = Paint(bodyPaint).apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    canvas.drawRect(36f, 36f, 559f, 230f, bluePaint)
    canvas.drawText("E-Ticket Kapal", 56f, 74f, titlePaint)
    canvas.drawText("Kode Booking: ${ticket.bookingCode}", 56f, 100f, whiteBodyPaint)
    canvas.drawText("Status: ${ticket.status}", 56f, 122f, whiteBodyPaint)

    canvas.drawText(ticket.origin, 56f, 174f, titlePaint)
    canvas.drawText("Pelabuhan asal", 56f, 196f, whiteBodyPaint)
    canvas.drawText("→", 272f, 174f, titlePaint)
    canvas.drawText(ticket.destination, 338f, 174f, titlePaint)
    canvas.drawText("Pelabuhan tujuan", 338f, 196f, whiteBodyPaint)

    canvas.drawRect(36f, 248f, 559f, 396f, lightPaint)
    canvas.drawText("Detail Perjalanan", 56f, 284f, sectionPaint)
    drawPdfLabelValue(canvas, "Kapal", ticket.shipName, 56f, 318f, labelPaint, boldPaint)
    drawPdfLabelValue(canvas, "Tanggal", DateFormatter.formatDate(ticket.departureDate), 56f, 358f, labelPaint, boldPaint)
    drawPdfLabelValue(canvas, "Jam Berangkat", ticket.departureTime, 300f, 318f, labelPaint, boldPaint)
    drawPdfLabelValue(canvas, "Kelas", ticket.ticketClassName, 300f, 358f, labelPaint, boldPaint)

    canvas.drawText("QR Check-in", 56f, 440f, sectionPaint)
    drawDummyTicketQr(
        canvas = canvas,
        value = ticket.qrCode,
        left = 56f,
        top = 458f,
        size = 190f,
    )

    canvas.drawText("Data Penumpang", 286f, 440f, sectionPaint)
    var passengerY = 472f
    ticket.passengers.forEachIndexed { index, passenger ->
        canvas.drawText("${index + 1}. ${passenger.fullName}", 286f, passengerY, boldPaint)
        passengerY += 18f
        canvas.drawText("NIK: ${passenger.nik}", 286f, passengerY, bodyPaint)
        passengerY += 18f
        canvas.drawText("HP: ${passenger.phoneNumber}", 286f, passengerY, bodyPaint)
        passengerY += 18f
        canvas.drawText("Lahir: ${passenger.birthDate.ifBlank { "-" }}", 286f, passengerY, bodyPaint)
        passengerY += 18f
        canvas.drawText("Gender: ${passenger.gender.ifBlank { "-" }}", 286f, passengerY, bodyPaint)
        passengerY += 24f
    }

    canvas.drawText("Informasi Boarding", 56f, 700f, sectionPaint)
    canvas.drawText("Terminal: ${ticket.terminal}", 56f, 730f, bodyPaint)
    canvas.drawText("Gate: ${ticket.gate}", 56f, 754f, bodyPaint)
    canvas.drawText(ticket.note.take(90), 56f, 786f, bodyPaint)

    pdfDocument.finishPage(page)

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                values,
            )

            if (uri != null) {
                context.contentResolver.openOutputStream(uri)?.use { output ->
                    pdfDocument.writeTo(output)
                }
            }
        } else {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                fileName,
            )

            FileOutputStream(file).use { output ->
                pdfDocument.writeTo(output)
            }
        }

        Toast.makeText(context, "E-ticket berhasil diunduh.", Toast.LENGTH_SHORT).show()
    } catch (exception: Exception) {
        Toast.makeText(context, "Gagal mengunduh e-ticket.", Toast.LENGTH_SHORT).show()
    } finally {
        pdfDocument.close()
    }
}
