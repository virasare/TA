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
import com.dicoding.tugas_akhir.ui.components.ticket.RescheduleOptionCard
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.components.dialog.ConfirmActionDialog

@Composable
fun RescheduleScreen(
    bookingId: String,
    onSubmitClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedScheduleId by remember {
        mutableStateOf("")
    }

    var showRescheduleConfirm by remember {
        mutableStateOf(false)
    }

    var isSubmitting by remember {
        mutableStateOf(false)
    }

    if (showRescheduleConfirm) {
        ConfirmActionDialog(
            title = "Ajukan reschedule?",
            message = "Pengajuan reschedule akan mengubah status tiket menjadi Reschedule Diproses.",
            confirmText = "Ya, ajukan",
            onConfirm = {
                showRescheduleConfirm = false
                isSubmitting = true
                onSubmitClick(bookingId)
            },
            onDismiss = {
                showRescheduleConfirm = false
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
                    title = "Reschedule Tiket",
                    description = "Pilih jadwal baru yang tersedia. Perubahan jadwal akan diproses dan tiket baru akan diterbitkan setelah berhasil.",
                )
            }

            item {
                RescheduleOptionCard(
                    selectedScheduleId = selectedScheduleId,
                    onScheduleSelected = {
                        selectedScheduleId = it
                    },
                )
            }
        }

        PrimaryButton(
            text = if (isSubmitting) "Mengajukan Reschedule..." else "Ajukan Reschedule",
            onClick = {
                showRescheduleConfirm = true
            },
            enabled = selectedScheduleId.isNotBlank() && !isSubmitting,
            isLoading = isSubmitting,
            modifier = Modifier.navigationBarsPadding()
        )
    }
}