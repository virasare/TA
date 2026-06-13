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
fun PaymentScreen(
    bookingId: String,
    onBackClick: () -> Unit,
    onPaymentCreated: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val paymentMethodUiState by viewModel.paymentMethodUiState.collectAsStateWithLifecycle()
    val selectedPaymentMethod by viewModel.selectedPaymentMethod.collectAsStateWithLifecycle()
    val createPaymentUiState by viewModel.createPaymentUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadPaymentMethods()
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
            .background(Background)
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
                    color = Neutral700,
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
                            animationFile = "empty_schedule.json",
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
            createPaymentUiState = createPaymentUiState,
            onContinueClick = {
                viewModel.createPayment(bookingId)
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
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 3.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(
                            Primary3,
                            White,
                            White,
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
                            tint = Neutral700,
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Pembayaran",
                        color = Neutral700,
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
                        color = Primary3,
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Payments,
                                contentDescription = null,
                                tint = Primary2,
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
                            color = Neutral700,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                        )

                        Text(
                            text = "Selesaikan pembayaran untuk menerbitkan e-ticket kamu.",
                            color = Neutral500,
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
        color = Primary3,
        border = BorderStroke(1.dp, Neutral200),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = Primary2,
                modifier = Modifier.size(22.dp),
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Pembayaran diproses secara aman. Pastikan nominal dan metode pembayaran sudah sesuai.",
                color = Neutral700,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun PaymentBottomActionBar(
    selectedMethodName: String?,
    createPaymentUiState: CreatePaymentUiState,
    onContinueClick: () -> Unit,
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
            if (selectedMethodName != null) {
                Text(
                    text = "Dipilih: $selectedMethodName",
                    color = Neutral700,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                Text(
                    text = "Pilih metode pembayaran terlebih dahulu.",
                    color = Neutral500,
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
                    containerColor = Primary2,
                    contentColor = White,
                    disabledContainerColor = Neutral200,
                    disabledContentColor = Neutral500,
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (createPaymentUiState is CreatePaymentUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = White,
                    )
                } else {
                    Text("Lanjut ke Instruksi Pembayaran")
                }
            }
        }
    }
}