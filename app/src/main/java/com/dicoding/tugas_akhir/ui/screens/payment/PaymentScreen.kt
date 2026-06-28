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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.ui.components.cards.PaymentMethodCard
import com.dicoding.tugas_akhir.ui.components.loading.PaymentMethodListPlaceholder
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView
import com.dicoding.tugas_akhir.ui.state.CreatePaymentUiState
import com.dicoding.tugas_akhir.ui.state.PaymentMethodUiState
import com.dicoding.tugas_akhir.ui.viewmodel.PaymentViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dicoding.tugas_akhir.core.utils.PriceFormatter
import com.dicoding.tugas_akhir.ui.components.dialog.ConfirmActionDialog
import com.dicoding.tugas_akhir.ui.state.BookingDetailUiState
import com.dicoding.tugas_akhir.ui.viewmodel.BookingViewModel

@Composable
fun PaymentScreen(
    bookingId: String,
    onBackClick: () -> Unit,
    onPaymentCreated: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
    bookingViewModel: BookingViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val paymentMethodUiState by viewModel.paymentMethodUiState.collectAsStateWithLifecycle()
    val selectedPaymentMethod by viewModel.selectedPaymentMethod.collectAsStateWithLifecycle()
    val createPaymentUiState by viewModel.createPaymentUiState.collectAsStateWithLifecycle()
    val bookingDetailUiState by bookingViewModel.bookingDetailUiState.collectAsStateWithLifecycle()
    var showConfirmPayment by remember { mutableStateOf(false) }
    val totalPaymentText = when (val state = bookingDetailUiState) {
        is BookingDetailUiState.Success -> PriceFormatter.formatToRupiah(state.booking.totalPrice)
        is BookingDetailUiState.Loading -> "Memuat total..."
        is BookingDetailUiState.Error -> null
    }

    LaunchedEffect(Unit) {
        viewModel.loadPaymentMethods()
    }

    LaunchedEffect(bookingId) {
        bookingViewModel.getBookingDetail(bookingId)
    }

    LaunchedEffect(createPaymentUiState) {
        val state = createPaymentUiState

        if (state is CreatePaymentUiState.Success) {
            onPaymentCreated(state.payment.id)
            viewModel.resetCreatePaymentState()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("payment_screen"),
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
                PaymentSecurityInfo()
            }

            item {
                Text(
                    text = "Metode Pembayaran",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            when (val state = paymentMethodUiState) {
                is PaymentMethodUiState.Loading -> {
                    item {
                        PaymentMethodListPlaceholder()
                    }
                }

                is PaymentMethodUiState.Success -> {
                    items(
                        items = state.methods,
                        key = { method -> method.id },
                    ) { method ->
                        PaymentMethodCard(
                            method = method,
                            selected = selectedPaymentMethod?.id == method.id,
                            onClick = {
                                viewModel.selectPaymentMethod(method)
                            },
                        )
                    }
                }

                is PaymentMethodUiState.Empty -> {
                    item {
                        LottieStateView(
                            animationFile = "empty.json",
                            title = "Metode pembayaran kosong",
                            message = state.message,
                        )
                    }
                }

                is PaymentMethodUiState.Error -> {
                    item {
                        LottieStateView(
                            animationFile = "no_internet.json",
                            title = "Terjadi Kesalahan",
                            message = state.message,
                        )
                    }
                }
            }
        }

        PaymentBottomActionBar(
            selectedMethodName = selectedPaymentMethod?.name,
            totalPaymentText = totalPaymentText,
            createPaymentUiState = createPaymentUiState,
            onContinueClick = {
                showConfirmPayment = true
            },
        )
    }

    if (showConfirmPayment) {
        ConfirmActionDialog(
            title = "Lanjut ke pembayaran?",
            message = "Pastikan metode pembayaran sudah benar sebelum melanjutkan.",
            onConfirm = {
                showConfirmPayment = false
                viewModel.createPayment(bookingId)
            },
            onDismiss = {
                showConfirmPayment = false
            },
        )
    }
}

@Composable
private fun PaymentHeaderCard(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 3.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background,
                        )
                    )
                )
                .padding(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Kembali",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Pembayaran",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        modifier = Modifier.size(52.dp),
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
                                modifier = Modifier.size(28.dp),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "Pilih metode pembayaran",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                        )

                        Text(
                            text = "Selesaikan pembayaran untuk menerbitkan e-ticket kamu.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentSecurityInfo(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.58f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp),
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Pembayaran diproses secara aman. Pastikan nominal dan metode pembayaran sudah sesuai.",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun PaymentBottomActionBar(
    selectedMethodName: String?,
    totalPaymentText: String?,
    createPaymentUiState: CreatePaymentUiState,
    onContinueClick: () -> Unit,
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
            if (totalPaymentText != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Total Pembayaran",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Text(
                        text = totalPaymentText,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }

            if (selectedMethodName != null) {
                Text(
                    text = "Dipilih: $selectedMethodName",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                Text(
                    text = "Pilih metode pembayaran terlebih dahulu.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            if (createPaymentUiState is CreatePaymentUiState.Error) {
                Text(
                    text = createPaymentUiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Button(
                onClick = onContinueClick,
                enabled = selectedMethodName != null &&
                        createPaymentUiState !is CreatePaymentUiState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (createPaymentUiState is CreatePaymentUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text("Lanjut ke Instruksi Pembayaran")
                }
            }
        }
    }
}
