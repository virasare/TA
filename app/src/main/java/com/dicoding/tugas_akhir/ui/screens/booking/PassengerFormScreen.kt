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
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.forms.PassengerInputForm
import com.dicoding.tugas_akhir.ui.state.CreateBookingUiState
import com.dicoding.tugas_akhir.ui.state.PassengerFormState
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White
import com.dicoding.tugas_akhir.ui.viewmodel.BookingViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

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
) {
    val passengerForms by viewModel.passengerForms.collectAsStateWithLifecycle()
    val createBookingUiState by viewModel.createBookingUiState.collectAsStateWithLifecycle()

    var selectedPassengerIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val isLoading = createBookingUiState is CreateBookingUiState.Loading
    val isAllFormValid = passengerForms.all { it.isValid }
    val currentForm = passengerForms.getOrNull(selectedPassengerIndex) ?: PassengerFormState()
    val isCurrentFormValid = currentForm.isValid
    val isLastPassenger = selectedPassengerIndex == passengerForms.lastIndex
    val totalPrice = ticketPrice * passengerCount

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
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
                    selectedPassengerIndex += 1
                } else {
                    viewModel.createBooking(
                        scheduleId = scheduleId,
                        ticketClassId = ticketClassId
                    )
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
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                shape = RoundedCornerShape(15.dp),
                color = Primary3
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Primary2,
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
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "$passengerCount penumpang • ${PriceFormatter.formatToRupiah(totalPrice)}",
                    color = Neutral500,
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
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Proses Pemesanan",
                color = Neutral700,
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
        color = White,
        border = BorderStroke(1.dp, Neutral200),
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
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "Tanda centang berarti data sudah lengkap.",
                    color = Neutral500,
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
        color = if (selected) Primary2 else White,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected || completed) Primary2 else Neutral200
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
                tint = if (selected) White else if (completed) Primary2 else Neutral500,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.size(6.dp))

            Text(
                text = "P$passengerNumber",
                color = if (selected) White else Neutral700,
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
            color = if (active || done) Primary2 else Background,
            border = BorderStroke(
                width = 1.dp,
                color = if (active || done) Primary2 else Neutral200
            )
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (done) {
                    Icon(
                        imageVector = Icons.Outlined.TaskAlt,
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(17.dp)
                    )
                } else {
                    Text(
                        text = number,
                        color = if (active) White else Neutral500,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        Text(
            text = title,
            color = if (active) Primary2 else Neutral500,
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
        color = Neutral200
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
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = description,
            color = Neutral500,
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
        color = Primary3,
        border = BorderStroke(1.dp, Neutral200)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.Badge,
                contentDescription = null,
                tint = Primary2,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = "Data penumpang akan digunakan untuk penerbitan e-ticket. Gunakan chip penumpang di atas untuk mengecek data satu per satu.",
                color = Neutral700,
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
        color = White,
        border = BorderStroke(1.dp, Neutral200),
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
                        color = Neutral500,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = PriceFormatter.formatToRupiah(totalPrice),
                        color = Neutral700,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Primary3
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.EventSeat,
                            contentDescription = null,
                            tint = Primary2,
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.size(5.dp))

                        Text(
                            text = "P${selectedPassengerIndex + 1}/$totalPassenger",
                            color = Primary2,
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
                        color = Primary2
                    )
                }
            }
        }
    }
}