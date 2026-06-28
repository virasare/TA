package com.dicoding.tugas_akhir.ui.screens.booking

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.core.utils.PriceFormatter
import com.dicoding.tugas_akhir.domain.model.SavedPassenger
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.forms.PassengerInputForm
import com.dicoding.tugas_akhir.ui.state.CreateBookingUiState
import com.dicoding.tugas_akhir.ui.state.PassengerFormState
import com.dicoding.tugas_akhir.ui.viewmodel.BookingViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.dicoding.tugas_akhir.ui.components.booking.SavedPassengerBookingActionCard
import com.dicoding.tugas_akhir.ui.components.booking.SavedPassengerPickerSheet
import com.dicoding.tugas_akhir.ui.viewmodel.BookingPassengerFormViewModel
import com.dicoding.tugas_akhir.ui.components.dialog.ConfirmActionDialog
import com.dicoding.tugas_akhir.ui.components.booking.SavePassengerDataCheckboxCard

@Composable
fun PassengerFormScreen(
    scheduleId: String,
    ticketClassId: String,
    ticketPrice: Int,
    passengerCount: Int,
    onBackClick: () -> Unit,
    onBookingCreated: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
    savedPassengerViewModel: BookingPassengerFormViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val passengerForms by viewModel.passengerForms.collectAsStateWithLifecycle()
    val createBookingUiState by viewModel.createBookingUiState.collectAsStateWithLifecycle()

    val savedPassengerUiState by savedPassengerViewModel.uiState.collectAsStateWithLifecycle()

    var showSavedPassengerSheet by rememberSaveable {
        mutableStateOf(false)
    }

    var showCreateBookingConfirm by remember {
        mutableStateOf(false)
    }

    val savePassengerCheckedMap = remember {
        mutableStateMapOf<Int, Boolean>()
    }

    var selectedPassengerIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val isLoading = createBookingUiState is CreateBookingUiState.Loading
    val isAllFormValid = passengerForms.all { it.isValid }
    val currentForm = passengerForms.getOrNull(selectedPassengerIndex) ?: PassengerFormState()
    val isCurrentFormValid = currentForm.isValid
    val isLastPassenger = selectedPassengerIndex == passengerForms.lastIndex
    val totalPrice = ticketPrice * passengerCount
    val isCurrentPassengerAlreadySaved = remember(
        currentForm,
        savedPassengerUiState.savedPassengers,
    ) {
        savedPassengerUiState.savedPassengers.any { savedPassenger ->
            savedPassenger.hasSameData(currentForm)
        }
    }

    LaunchedEffect(passengerCount) {
        viewModel.preparePassengerForms(passengerCount)
        selectedPassengerIndex = 0
    }

    LaunchedEffect(passengerForms.size) {
        if (passengerForms.isNotEmpty() && selectedPassengerIndex > passengerForms.lastIndex) {
            selectedPassengerIndex = passengerForms.lastIndex
        }
    }

    LaunchedEffect(createBookingUiState) {
        val state = createBookingUiState
        if (state is CreateBookingUiState.Success) {
            onBookingCreated(state.booking.id)
            viewModel.resetCreateBookingState()
        }
    }

    LaunchedEffect(selectedPassengerIndex, isCurrentPassengerAlreadySaved) {
        if (isCurrentPassengerAlreadySaved) {
            savePassengerCheckedMap[selectedPassengerIndex] = false
        }
    }

    if (showSavedPassengerSheet) {
        SavedPassengerPickerSheet(
            passengers = savedPassengerUiState.savedPassengers,
            isLoading = savedPassengerUiState.isSavedPassengerLoading,
            onPassengerClick = { passenger ->
                viewModel.updatePassengerFullName(selectedPassengerIndex, passenger.fullName)
                viewModel.updatePassengerNik(selectedPassengerIndex, passenger.nik)
                viewModel.updatePassengerPhoneNumber(selectedPassengerIndex, passenger.phoneNumber)
                viewModel.updatePassengerBirthDate(selectedPassengerIndex, passenger.birthDate)
                viewModel.updatePassengerGender(selectedPassengerIndex, passenger.gender)

                showSavedPassengerSheet = false
            },
            onDismiss = {
                showSavedPassengerSheet = false
            },
        )
    }

    if (showCreateBookingConfirm) {
        ConfirmActionDialog(
            title = "Buat pesanan?",
            message = "Pastikan semua data penumpang sudah benar sebelum melanjutkan ke ringkasan booking.",
            confirmText = "Ya, buat pesanan",
            onConfirm = {
                showCreateBookingConfirm = false

                val shouldSaveCurrentPassenger =
                    savePassengerCheckedMap[selectedPassengerIndex] ?: false

                if (
                    shouldSaveCurrentPassenger &&
                    currentForm.isValid &&
                    !isCurrentPassengerAlreadySaved
                ) {
                    savedPassengerViewModel.savePassengerFromBooking(
                        fullName = currentForm.fullName,
                        nik = currentForm.nik,
                        phoneNumber = currentForm.phoneNumber,
                        birthDate = currentForm.birthDate,
                        gender = currentForm.gender,
                    )
                }

                viewModel.createBooking(
                    scheduleId = scheduleId,
                    ticketClassId = ticketClassId,
                )
            },
            onDismiss = {
                showCreateBookingConfirm = false
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("passenger_form_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                PassengerFormHeader(
                    passengerCount = passengerCount,
                    totalPrice = totalPrice
                )
            }

            item {
                BookingStepCard()
            }

            item {
                SectionTitle(
                    title = "Lengkapi Data Penumpang",
                    description = "Pilih penumpang lalu isi datanya satu per satu agar lebih mudah dicek."
                )
            }

            item {
                PassengerTabSection(
                    passengerForms = passengerForms,
                    selectedIndex = selectedPassengerIndex,
                    onPassengerClick = { index ->
                        selectedPassengerIndex = index
                    }
                )
            }

            item {
                SavedPassengerBookingActionCard(
                    savedPassengerCount = savedPassengerUiState.savedPassengers.size,
                    isLoading = savedPassengerUiState.isSavedPassengerLoading,
                    onPickSavedPassengerClick = {
                        showSavedPassengerSheet = true
                    },
                )
            }

            item {
                PassengerInputForm(
                    passengerNumber = selectedPassengerIndex + 1,
                    formState = currentForm,
                    onFullNameChange = { value ->
                        viewModel.updatePassengerFullName(selectedPassengerIndex, value)
                    },
                    onNikChange = { value ->
                        viewModel.updatePassengerNik(selectedPassengerIndex, value)
                    },
                    onPhoneNumberChange = { value ->
                        viewModel.updatePassengerPhoneNumber(selectedPassengerIndex, value)
                    },
                    onBirthDateChange = { value ->
                        viewModel.updatePassengerBirthDate(selectedPassengerIndex, value)
                    },
                    onGenderChange = { value ->
                        viewModel.updatePassengerGender(selectedPassengerIndex, value)
                    }
                )
            }

            if (!isCurrentPassengerAlreadySaved) {
                item {
                    SavePassengerDataCheckboxCard(
                        saveToPassengerData = savePassengerCheckedMap[selectedPassengerIndex] ?: false,
                        onSaveCheckedChange = { checked ->
                            savePassengerCheckedMap[selectedPassengerIndex] = checked
                        },
                    )
                }
            }

            if (createBookingUiState is CreateBookingUiState.Error) {
                item {
                    ErrorInfoCard(
                        message = (createBookingUiState as CreateBookingUiState.Error).message
                    )
                }
            }

            item {
                PassengerNoteCard()
            }
        }

        PassengerFormBottomBar(
            totalPrice = totalPrice,
            passengerCount = passengerCount,
            selectedPassengerIndex = selectedPassengerIndex,
            totalPassenger = passengerForms.size,
            isLastPassenger = isLastPassenger,
            isLoading = isLoading,
            enabled = if (isLastPassenger) {
                isAllFormValid && !isLoading
            } else {
                isCurrentFormValid && !isLoading
            },
            onContinueClick = {
                if (!isLastPassenger) {
                    val shouldSaveCurrentPassenger =
                        savePassengerCheckedMap[selectedPassengerIndex] ?: false

                    if (
                        shouldSaveCurrentPassenger &&
                        currentForm.isValid &&
                        !isCurrentPassengerAlreadySaved
                    ) {
                        savedPassengerViewModel.savePassengerFromBooking(
                            fullName = currentForm.fullName,
                            nik = currentForm.nik,
                            phoneNumber = currentForm.phoneNumber,
                            birthDate = currentForm.birthDate,
                            gender = currentForm.gender,
                        )
                    }

                    selectedPassengerIndex += 1
                } else {
                    showCreateBookingConfirm = true
                }
            }
        )
    }
}

@Composable
private fun PassengerFormHeader(
    passengerCount: Int,
    totalPrice: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                shape = RoundedCornerShape(15.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.58f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Data Penumpang",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "$passengerCount penumpang • ${PriceFormatter.formatToRupiah(totalPrice)}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun BookingStepCard(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Proses Pemesanan",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepItem(
                    number = "1",
                    title = "Tiket",
                    active = false,
                    done = true,
                    modifier = Modifier.weight(1f)
                )

                StepLine(modifier = Modifier.weight(0.35f))

                StepItem(
                    number = "2",
                    title = "Data",
                    active = true,
                    done = false,
                    modifier = Modifier.weight(1f)
                )

                StepLine(modifier = Modifier.weight(0.35f))

                StepItem(
                    number = "3",
                    title = "Ringkasan",
                    active = false,
                    done = false,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PassengerTabSection(
    passengerForms: List<PassengerFormState>,
    selectedIndex: Int,
    onPassengerClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Pilih Penumpang",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "Tanda centang berarti data sudah lengkap.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(passengerForms) { index, form ->
                    PassengerTabChip(
                        passengerNumber = index + 1,
                        selected = selectedIndex == index,
                        completed = form.isValid,
                        hasInput = form.hasInput(),
                        onClick = {
                            onPassengerClick(index)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PassengerTabChip(
    passengerNumber: Int,
    selected: Boolean,
    completed: Boolean,
    hasInput: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected || completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when {
                    completed -> Icons.Outlined.TaskAlt
                    hasInput -> Icons.Outlined.EditNote
                    else -> Icons.Outlined.Person
                },
                contentDescription = null,
                tint = if (selected) MaterialTheme.colorScheme.onPrimary else if (completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.size(6.dp))

            Text(
                text = "P$passengerNumber",
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

private fun PassengerFormState.hasInput(): Boolean {
    return fullName.isNotBlank() ||
            nik.isNotBlank() ||
            phoneNumber.isNotBlank() ||
            birthDate.isNotBlank() ||
            gender.isNotBlank()
}

@Composable
private fun StepItem(
    number: String,
    title: String,
    active: Boolean,
    done: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Surface(
            modifier = Modifier.size(30.dp),
            shape = CircleShape,
            color = if (active || done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            border = BorderStroke(
                width = 1.dp,
                color = if (active || done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
            )
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (done) {
                    Icon(
                        imageVector = Icons.Outlined.TaskAlt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(17.dp)
                    )
                } else {
                    Text(
                        text = number,
                        color = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        Text(
            text = title,
            color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StepLine(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier.padding(horizontal = 4.dp),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Composable
private fun SectionTitle(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun PassengerNoteCard(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.58f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.Badge,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = "Data penumpang akan digunakan untuk penerbitan e-ticket. Gunakan chip penumpang di atas untuk mengecek data satu per satu.",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ErrorInfoCard(
    message: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun PassengerFormBottomBar(
    totalPrice: Int,
    passengerCount: Int,
    selectedPassengerIndex: Int,
    totalPassenger: Int,
    isLastPassenger: Boolean,
    isLoading: Boolean,
    enabled: Boolean,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonText = when {
        isLoading -> "Memproses Pesanan..."
        isLastPassenger -> "Lanjut ke Ringkasan"
        else -> "Simpan & Lanjut P${selectedPassengerIndex + 2}"
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "$passengerCount penumpang",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = PriceFormatter.formatToRupiah(totalPrice),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.58f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.EventSeat,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.size(5.dp))

                        Text(
                            text = "P${selectedPassengerIndex + 1}/$totalPassenger",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            PrimaryButton(
                text = buttonText,
                onClick = onContinueClick,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth()
            )

            if (isLoading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

private fun SavedPassenger.hasSameData(
    formState: PassengerFormState,
): Boolean {
    return fullName.trim().equals(formState.fullName.trim(), ignoreCase = true) &&
            nik.trim() == formState.nik.trim() &&
            phoneNumber.trim() == formState.phoneNumber.trim() &&
            birthDate.trim() == formState.birthDate.trim() &&
            gender.trim().equals(formState.gender.trim(), ignoreCase = true)
}
