package com.dicoding.tugas_akhir.ui.screens.payment

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.ErrorOutline
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.OutlinedButtonDefaults
import com.dicoding.tugas_akhir.core.utils.PriceFormatter
import com.dicoding.tugas_akhir.domain.model.Payment
import com.dicoding.tugas_akhir.ui.components.loading.PaymentWaitingPlaceholder
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView
import com.dicoding.tugas_akhir.ui.state.PaymentActionUiState
import com.dicoding.tugas_akhir.ui.state.PaymentDetailUiState
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White
import com.dicoding.tugas_akhir.ui.viewmodel.PaymentViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

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
                    .background(Background),
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
                onPaymentFailedClick = {
                    onPaymentFailed(paymentId)
                },
                modifier = modifier,
            )
        }

        is PaymentDetailUiState.Error -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Background),
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
    onPaymentFailedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
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
                PaymentWaitingHeader(
                    onBackClick = onBackClick,
                )
            }

            item {
                PaymentCodeCard(payment = payment)
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
            onPaymentFailedClick = onPaymentFailedClick,
        )
    }
}

@Composable
private fun PaymentWaitingHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 3.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Primary3,
                border = BorderStroke(1.dp, Neutral200),
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = Primary2,
                        modifier = Modifier.size(22.dp),
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Selesaikan pembayaran sebelum batas waktu agar tiket dapat diterbitkan.",
                        color = Neutral700,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentCodeCard(
    payment: Payment,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
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
                    color = Primary3,
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Payments,
                            contentDescription = null,
                            tint = Primary2,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = payment.paymentMethodName,
                        color = Neutral700,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Text(
                        text = "Status: ${payment.status}",
                        color = Primary2,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            HorizontalDivider(color = Neutral200)

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Total Pembayaran",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = PriceFormatter.formatToRupiah(payment.totalPrice),
                    color = Primary2,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Primary3,
                border = BorderStroke(1.dp, Neutral200),
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = "Kode Pembayaran",
                            color = Neutral500,
                            style = MaterialTheme.typography.bodySmall,
                        )

                        Text(
                            text = payment.paymentCode,
                            color = Neutral700,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }

                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = null,
                        tint = Primary2,
                    )
                }
            }

            Text(
                text = "Batas pembayaran: ${payment.expiredIn}",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodySmall,
            )
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
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Cara Pembayaran",
                color = Neutral700,
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
            color = Primary3,
            border = BorderStroke(1.dp, Neutral200),
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = number.toString(),
                    color = Primary2,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = instruction,
            modifier = Modifier.weight(1f),
            color = Neutral700,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun PaymentWaitingBottomBar(
    paymentActionUiState: PaymentActionUiState,
    onCheckPaymentClick: () -> Unit,
    onPaymentFailedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
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
                    containerColor = Primary2,
                    contentColor = White,
                    disabledContainerColor = Neutral200,
                    disabledContentColor = Neutral500,
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (paymentActionUiState is PaymentActionUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = White,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Saya Sudah Bayar / Cek Status")
                }
            }

            OutlinedButton(
                onClick = onPaymentFailedClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                    containerColor = White,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Pembayaran Bermasalah")
            }
        }
    }
}