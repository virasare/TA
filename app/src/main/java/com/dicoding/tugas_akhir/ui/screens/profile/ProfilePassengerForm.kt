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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.ButtonDefaults
import com.dicoding.tugas_akhir.ui.components.loading.PassengerFormPlaceholder

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

    val isFormLoading by viewModel.isFormLoading.collectAsStateWithLifecycle()

    LaunchedEffect(passengerId) {
        if (passengerId.isNullOrBlank()) {
            viewModel.resetForm()
        } else {
            viewModel.loadPassengerForEdit(passengerId)
        }
    }

    if (!passengerId.isNullOrBlank() && isFormLoading) {
        PassengerFormPlaceholder(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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

                    ProfileBirthDatePickerField(
                        value = formState.birthDate,
                        onDateSelected = viewModel::updateBirthDate,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileBirthDatePickerField(
    value: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = value.toDateMillis()
    )

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Tanggal Lahir") },
            placeholder = { Text("Pilih tanggal lahir") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            readOnly = true,
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
            ),
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    showDatePicker = true
                }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateSelected(it.toDateString())
                        }
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Text("Pilih")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Text("Batal")
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                    todayContentColor = MaterialTheme.colorScheme.primary,
                    todayDateBorderColor = MaterialTheme.colorScheme.primary,
                    navigationContentColor = MaterialTheme.colorScheme.primary,
                    selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                    selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        }
    }
}

private fun Long.toDateString(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(this))
}

private fun String.toDateMillis(): Long? {
    if (isBlank()) return null

    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.parse(this)?.time
    } catch (e: Exception) {
        null
    }
}