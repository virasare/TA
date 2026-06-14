package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.ui.components.profile.BottomActionButton
import com.dicoding.tugas_akhir.ui.components.profile.ChoiceSection
import com.dicoding.tugas_akhir.ui.components.profile.InfoNote
import com.dicoding.tugas_akhir.ui.components.profile.ProfileFormCard
import com.dicoding.tugas_akhir.ui.components.profile.ProfileTextField
import com.dicoding.tugas_akhir.ui.viewmodel.SavedPassengerViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

@Composable
fun PassengerProfileFormScreen(
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    passengerId: String? = null,
    viewModel: SavedPassengerViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    LaunchedEffect(passengerId) {
        if (passengerId.isNullOrBlank()) {
            viewModel.resetForm()
        } else {
            viewModel.loadPassengerForEdit(passengerId)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAFC))
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                ProfileFormCard(
                    title = if (passengerId.isNullOrBlank()) {
                        "Tambah Data Penumpang"
                    } else {
                        "Edit Data Penumpang"
                    },
                ) {
                    ProfileTextField(
                        label = "Nama Lengkap",
                        value = formState.fullName,
                        onValueChange = viewModel::updateFullName,
                        placeholder = "Masukkan nama lengkap",
                    )

                    ProfileTextField(
                        label = "Nomor Induk Kependudukan",
                        value = formState.nik,
                        onValueChange = viewModel::updateNik,
                        placeholder = "Masukkan 16 digit NIK",
                        keyboardType = KeyboardType.Number,
                        isError = formState.nik.isNotEmpty() && formState.nik.length < 16,
                        supportingText = if (formState.nik.isNotEmpty() && formState.nik.length < 16) {
                            "NIK harus 16 digit"
                        } else {
                            null
                        },
                    )

                    ProfileTextField(
                        label = "Nomor Telepon",
                        value = formState.phoneNumber,
                        onValueChange = viewModel::updatePhone,
                        placeholder = "Masukkan nomor telepon",
                        keyboardType = KeyboardType.Phone,
                    )

                    ChoiceSection(
                        title = "Jenis Kelamin",
                        options = listOf("Perempuan", "Laki-laki"),
                        selectedOption = formState.gender,
                        onOptionSelected = viewModel::updateGender,
                    )

                    InfoNote(
                        text = "Pastikan data penumpang sesuai kartu identitas untuk menghindari kendala saat keberangkatan.",
                    )
                }
            }
        }

        BottomActionButton(
            text = if (passengerId.isNullOrBlank()) {
                "Simpan Penumpang"
            } else {
                "Simpan Perubahan"
            },
            onClick = {
                viewModel.savePassenger(
                    onSaved = onSaveClick,
                )
            },
            enabled = formState.isValid,
            modifier = Modifier.navigationBarsPadding(),
        )
    }
}