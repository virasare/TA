package com.dicoding.tugas_akhir.ui.screens.ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.ticket.ManageTicketInfoCard
import com.dicoding.tugas_akhir.ui.components.ticket.RefundReasonCard
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.components.dialog.ConfirmActionDialog

@Composable
fun RefundScreen(
    bookingId: String,
    onSubmitClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedReason by remember {
        mutableStateOf("")
    }

    var customReason by remember {
        mutableStateOf("")
    }

    var showRefundConfirm by remember {
        mutableStateOf(false)
    }

    val isValid = selectedReason.isNotBlank() &&
            (selectedReason != "Alasan lain" || customReason.isNotBlank())

    if (showRefundConfirm) {
        ConfirmActionDialog(
            title = "Ajukan refund?",
            message = "Pengajuan refund akan mengubah status tiket menjadi Refund Diproses.",
            confirmText = "Ya, ajukan",
            onConfirm = {
                showRefundConfirm = false
                onSubmitClick(bookingId)
            },
            onDismiss = {
                showRefundConfirm = false
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                ManageTicketInfoCard(
                    title = "Pengajuan Refund",
                    description = "Refund dapat diajukan sesuai ketentuan tiket. Proses verifikasi membutuhkan waktu dan statusnya akan muncul pada halaman Pesanan Saya.",
                )
            }

            item {
                RefundReasonCard(
                    selectedReason = selectedReason,
                    customReason = customReason,
                    onReasonSelected = {
                        selectedReason = it
                    },
                    onCustomReasonChange = {
                        customReason = it
                    },
                )
            }
        }

        PrimaryButton(
            text = "Ajukan Refund",
            onClick = {
                showRefundConfirm = true
            },
            enabled = isValid,
            modifier = Modifier
                .navigationBarsPadding()
        )
    }
}