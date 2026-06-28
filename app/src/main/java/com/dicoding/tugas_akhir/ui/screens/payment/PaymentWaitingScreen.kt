package com.dicoding.tugas_akhir.ui.screens.payment

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.core.utils.PriceFormatter
import com.dicoding.tugas_akhir.domain.model.Payment
import com.dicoding.tugas_akhir.ui.components.loading.PaymentWaitingPlaceholder
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView
import com.dicoding.tugas_akhir.ui.components.dialog.ConfirmActionDialog
import com.dicoding.tugas_akhir.ui.state.PaymentActionUiState
import com.dicoding.tugas_akhir.ui.state.PaymentDetailUiState
import com.dicoding.tugas_akhir.ui.viewmodel.PaymentViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun PaymentWaitingScreen(
    paymentId: String,
    onBackClick: () -> Unit,
    onPaymentSuccess: (String) -> Unit,
    onPaymentFailed: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val paymentDetailUiState by viewModel.paymentDetailUiState.collectAsStateWithLifecycle()
    val paymentActionUiState by viewModel.paymentActionUiState.collectAsStateWithLifecycle()

    LaunchedEffect(paymentId) {
        viewModel.getPaymentDetail(paymentId)
    }

    LaunchedEffect(paymentActionUiState) {
        val state = paymentActionUiState
        if (state is PaymentActionUiState.Success) {
            onPaymentSuccess(state.payment.id)
            viewModel.resetPaymentActionState()
        }
    }

    when (val state = paymentDetailUiState) {
        is PaymentDetailUiState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                PaymentWaitingPlaceholder(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        is PaymentDetailUiState.Success -> {
            PaymentWaitingContent(
                payment = state.payment,
                paymentActionUiState = paymentActionUiState,
                onBackClick = onBackClick,
                onCheckPaymentClick = {
                    viewModel.simulatePaymentSuccess(paymentId)
                },
                modifier = modifier,
            )
        }

        is PaymentDetailUiState.Error -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                LottieStateView(
                    animationFile = "no_internet.json",
                    title = "Pembayaran tidak ditemukan",
                    message = state.message,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun PaymentWaitingContent(
    payment: Payment,
    paymentActionUiState: PaymentActionUiState,
    onBackClick: () -> Unit,
    onCheckPaymentClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showChangeMethodConfirm by remember {
        mutableStateOf(false)
    }

    var remainingSeconds by remember(payment.id) {
        mutableStateOf(30 * 60)
    }

    LaunchedEffect(payment.id) {
        remainingSeconds = 30 * 60
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds -= 1
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("payment_waiting_screen"),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 14.dp,
                bottom = 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                PaymentWaitingHeader()
            }

            item {
                PaymentCodeCard(
                    payment = payment,
                    remainingSeconds = remainingSeconds,
                )
            }

            item {
                PaymentInstructionCard(payment = payment)
            }

            if (paymentActionUiState is PaymentActionUiState.Error) {
                item {
                    Text(
                        text = paymentActionUiState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        PaymentWaitingBottomBar(
            paymentActionUiState = paymentActionUiState,
            onCheckPaymentClick = onCheckPaymentClick,
            onChangeMethodClick = {
                showChangeMethodConfirm = true
            },
        )
    }

    if (showChangeMethodConfirm) {
        ConfirmActionDialog(
            title = "Ganti metode pembayaran?",
            message = "Instruksi pembayaran saat ini akan direset dan kamu akan kembali ke halaman pilih metode.",
            confirmText = "Ya, ganti metode",
            onConfirm = {
                showChangeMethodConfirm = false
                onBackClick()
            },
            onDismiss = {
                showChangeMethodConfirm = false
            },
        )
    }
}

@Composable
private fun PaymentWaitingHeader(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.58f),
                shape = RoundedCornerShape(16.dp),
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.AccessTime,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp),
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "Selesaikan pembayaran sebelum batas waktu agar tiket dapat diterbitkan.",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun PaymentCodeCard(
    payment: Payment,
    remainingSeconds: Int,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val isQris = payment.paymentMethodId.equals("qris", ignoreCase = true) ||
            payment.paymentMethodName.contains("qris", ignoreCase = true)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 3.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.58f),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Payments,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = payment.paymentMethodName,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Text(
                        text = "Status: ${payment.status}",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Total Pembayaran",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = PriceFormatter.formatToRupiah(payment.totalPrice),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(
                    text = "Sisa waktu: ${remainingSeconds.toCountdownText()}",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.58f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            ) {
                if (isQris) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        ) {
                            DummyQrisCode(
                                value = payment.paymentCode,
                                modifier = Modifier
                                    .padding(14.dp)
                                    .size(190.dp),
                            )
                        }

                        Text(
                            text = "QRIS simulasi untuk kebutuhan pengujian.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall,
                        )

                        OutlinedButton(
                            onClick = {
                                saveDummyQrisImage(
                                    context = context,
                                    value = payment.paymentCode,
                                )
                            },
                        ) {
                            Text("Simpan QRIS")
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(
                                    text = payment.copyTitle(),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodySmall,
                                )

                                val destinationInfo = payment.destinationInfo()
                                if (destinationInfo.isNotBlank()) {
                                    Text(
                                        text = destinationInfo,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }

                                Text(
                                    text = payment.paymentCode,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }

                            IconButton(
                                onClick = {
                                    clipboardManager.setText(
                                        AnnotatedString(payment.paymentCode)
                                    )
                                    Toast.makeText(
                                        context,
                                        "${payment.copyActionText()} disalin.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ContentCopy,
                                    contentDescription = payment.copyActionText(),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun DummyQrisCode(
    value: String,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val cells = 29
        val cellSize = size.minDimension / cells
        val background = Color.White
        val foreground = Color(0xFF111827)

        drawRect(background)

        fun drawFinder(startX: Int, startY: Int) {
            for (x in 0 until 7) {
                for (y in 0 until 7) {
                    val isBorder = x == 0 || y == 0 || x == 6 || y == 6
                    val isCenter = x in 2..4 && y in 2..4

                    if (isBorder || isCenter) {
                        drawRect(
                            color = foreground,
                            topLeft = androidx.compose.ui.geometry.Offset(
                                (startX + x) * cellSize,
                                (startY + y) * cellSize,
                            ),
                            size = Size(cellSize, cellSize),
                        )
                    }
                }
            }
        }

        drawFinder(1, 1)
        drawFinder(21, 1)
        drawFinder(1, 21)

        val seed = value.fold(0) { acc, char -> acc + char.code }
        for (x in 0 until cells) {
            for (y in 0 until cells) {
                val inFinder = (x in 1..7 && y in 1..7) ||
                        (x in 21..27 && y in 1..7) ||
                        (x in 1..7 && y in 21..27)

                val shouldDraw = !inFinder &&
                        ((x * 31 + y * 17 + seed) % 5 == 0 ||
                                (x + y + seed) % 11 == 0)

                if (shouldDraw) {
                    drawRect(
                        color = foreground,
                        topLeft = androidx.compose.ui.geometry.Offset(
                            x * cellSize,
                            y * cellSize,
                        ),
                        size = Size(cellSize, cellSize),
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentInstructionCard(
    payment: Payment,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Cara Pembayaran",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )

            payment.instructions.forEachIndexed { index, instruction ->
                InstructionStepItem(
                    number = index + 1,
                    instruction = instruction,
                )
            }
        }
    }
}

@Composable
private fun InstructionStepItem(
    number: Int,
    instruction: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.58f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = number.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = instruction,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private fun Payment.copyTitle(): String {
    return when {
        paymentMethodId.equals("virtual_account", ignoreCase = true) -> "Kode Virtual Account"
        paymentMethodId.equals("bank_transfer", ignoreCase = true) -> "Nomor Rekening Tujuan"
        else -> "Kode Pembayaran"
    }
}

private fun Payment.copyActionText(): String {
    return when {
        paymentMethodId.equals("virtual_account", ignoreCase = true) -> "Salin Kode VA"
        paymentMethodId.equals("bank_transfer", ignoreCase = true) -> "Salin Rekening"
        else -> "Salin Kode"
    }
}

private fun Payment.destinationInfo(): String {
    return when {
        paymentMethodId.equals("virtual_account", ignoreCase = true) -> {
            "Bank penerbit: BNI Virtual Account"
        }

        paymentMethodId.equals("bank_transfer", ignoreCase = true) -> {
            "Bank tujuan: BCA a.n. PT NusaKapal Indonesia"
        }

        else -> ""
    }
}

private fun Int.toCountdownText(): String {
    val minutes = this / 60
    val seconds = this % 60

    return "%02d:%02d".format(minutes, seconds)
}

private fun saveDummyQrisImage(
    context: Context,
    value: String,
) {
    val bitmap = createDummyQrisBitmap(value = value)
    val fileName = "qris-simulasi-${System.currentTimeMillis()}.png"

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/NusaKapal",
                )
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values,
            )

            if (uri != null) {
                context.contentResolver.openOutputStream(uri)?.use { output ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
                }
            }
        } else {
            val file = java.io.File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                fileName,
            )

            java.io.FileOutputStream(file).use { output ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
            }
        }

        Toast.makeText(context, "QRIS simulasi berhasil disimpan.", Toast.LENGTH_SHORT).show()
    } catch (exception: Exception) {
        Toast.makeText(context, "Gagal menyimpan QRIS simulasi.", Toast.LENGTH_SHORT).show()
    }
}

private fun createDummyQrisBitmap(
    value: String,
    sizePx: Int = 720,
): Bitmap {
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    val backgroundPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        style = android.graphics.Paint.Style.FILL
    }
    val foregroundPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.rgb(17, 24, 39)
        style = android.graphics.Paint.Style.FILL
    }

    canvas.drawRect(0f, 0f, sizePx.toFloat(), sizePx.toFloat(), backgroundPaint)

    val cells = 29
    val cellSize = sizePx / cells.toFloat()
    val seed = value.fold(0) { acc, char -> acc + char.code }

    fun drawCell(x: Int, y: Int) {
        canvas.drawRect(
            x * cellSize,
            y * cellSize,
            (x + 1) * cellSize,
            (y + 1) * cellSize,
            foregroundPaint,
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

    drawFinder(1, 1)
    drawFinder(21, 1)
    drawFinder(1, 21)

    for (x in 0 until cells) {
        for (y in 0 until cells) {
            val inFinder = (x in 1..7 && y in 1..7) ||
                    (x in 21..27 && y in 1..7) ||
                    (x in 1..7 && y in 21..27)

            val shouldDraw = !inFinder &&
                    ((x * 31 + y * 17 + seed) % 5 == 0 ||
                            (x + y + seed) % 11 == 0)

            if (shouldDraw) {
                drawCell(x, y)
            }
        }
    }

    return bitmap
}

@Composable
private fun PaymentWaitingBottomBar(
    paymentActionUiState: PaymentActionUiState,
    onCheckPaymentClick: () -> Unit,
    onChangeMethodClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Button(
                onClick = onCheckPaymentClick,
                enabled = paymentActionUiState !is PaymentActionUiState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (paymentActionUiState is PaymentActionUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Cek Status Pembayaran")
                }
            }

            OutlinedButton(
                onClick = onChangeMethodClick,
                enabled = paymentActionUiState !is PaymentActionUiState.Loading,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Payments,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Ganti Metode Pembayaran")
            }
        }
    }
}
