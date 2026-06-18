package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.domain.model.SavedPassenger
import com.dicoding.tugas_akhir.ui.components.dialog.ConfirmActionDialog
import com.dicoding.tugas_akhir.ui.components.profile.AddPassengerCard
import com.dicoding.tugas_akhir.ui.components.profile.InfoNote
import com.dicoding.tugas_akhir.ui.components.profile.PassengerDataCard
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView
import com.dicoding.tugas_akhir.ui.viewmodel.SavedPassengerViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

@Composable
fun PassengerDataScreen(
    onAddPassengerClick: () -> Unit,
    onEditPassengerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavedPassengerViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val passengers by viewModel.passengers.collectAsStateWithLifecycle()
    var selectedDeletePassenger by remember { mutableStateOf<SavedPassenger?>(null) }

    if (selectedDeletePassenger != null) {
        ConfirmActionDialog(
            title = "Hapus data penumpang?",
            message = "Data penumpang yang dihapus tidak akan tampil lagi saat pemesanan.",
            confirmText = "Hapus",
            dismissText = "Batal",
            onConfirm = {
                selectedDeletePassenger?.let { passenger ->
                    viewModel.deletePassenger(passenger)
                }
                selectedDeletePassenger = null
            },
            onDismiss = {
                selectedDeletePassenger = null
            },
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAFC))
            .navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            InfoNote(
                text = "Data penumpang tersimpan dapat digunakan kembali saat melakukan pemesanan tiket.",
            )
        }

        item {
            AddPassengerCard(
                onClick = onAddPassengerClick,
            )
        }

        if (passengers.isEmpty()) {
            item {
                LottieStateView(
                    animationFile = "empty.json",
                    title = "Belum Ada Data Penumpang",
                    message = "Tambahkan data penumpang agar proses booking lebih cepat.",
                )
            }
        } else {
            items(
                items = passengers,
                key = { it.id },
            ) { passenger ->
                PassengerDataCard(
                    title = passenger.fullName,
                    nik = passenger.nik,
                    phoneNumber = passenger.phoneNumber,
                    birthDate = passenger.birthDate,
                    gender = passenger.gender,
                    onEditClick = {
                        onEditPassengerClick(passenger.id)
                    },
                    onDeleteClick = {
                        selectedDeletePassenger = passenger
                    },
                )
            }
        }
    }
}