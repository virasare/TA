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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Sailing
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.core.utils.PriceFormatter
import com.dicoding.tugas_akhir.domain.model.TicketClassOption
import com.dicoding.tugas_akhir.ui.components.cards.TicketClassOptionCard
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.loading.ScheduleListPlaceholder
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView
import com.dicoding.tugas_akhir.ui.state.TicketClassUiState
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
fun SelectTicketScreen(
    scheduleId: String,
    onBackClick: () -> Unit,
    onContinueClick: (
        scheduleId: String,
        ticketClassId: String,
        ticketPrice: Int,
        passengerCount: Int
    ) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    )
) {
    val ticketClassUiState by viewModel.ticketClassUiState.collectAsStateWithLifecycle()
    val selectedTicketClass by viewModel.selectedTicketClass.collectAsStateWithLifecycle()
    val passengerCount by viewModel.passengerCount.collectAsStateWithLifecycle()

    LaunchedEffect(scheduleId) {
        viewModel.loadTicketClassOptions(scheduleId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .testTag("select_ticket_screen")
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
                SelectTicketHeader()
            }

            item {
                BookingStepCard()
            }

            item {
                SectionTitle(
                    title = "Pilih Kelas Tiket",
                    description = "Pilih kelas tiket dan jumlah penumpang sebelum mengisi data penumpang."
                )
            }

            when (val state = ticketClassUiState) {
                is TicketClassUiState.Loading -> {
                    item {
                        ScheduleListPlaceholder(itemCount = 3)
                    }
                }

                is TicketClassUiState.Success -> {
                    items(
                        count = state.ticketClasses.size,
                        key = { index -> state.ticketClasses[index].id }
                    ) { index ->
                        val ticketClass = state.ticketClasses[index]

                        TicketClassOptionCard(
                            ticketClass = ticketClass,
                            selected = selectedTicketClass?.id == ticketClass.id,
                            onClick = {
                                viewModel.selectTicketClass(ticketClass)
                            }
                        )
                    }
                }

                is TicketClassUiState.Empty -> {
                    item {
                        LottieStateView(
                            animationFile = "lottie/empty_schedule.json",
                            title = "Kelas tiket tidak tersedia",
                            message = state.message
                        )
                    }
                }

                is TicketClassUiState.Error -> {
                    item {
                        LottieStateView(
                            animationFile = "lottie/error_connection.json",
                            title = "Terjadi Kesalahan",
                            message = state.message
                        )
                    }
                }
            }

            item {
                PassengerCounterCard(
                    passengerCount = passengerCount,
                    onDecreaseClick = viewModel::decreasePassenger,
                    onIncreaseClick = viewModel::increasePassenger
                )
            }

            item {
                SelectTicketNoteCard()
            }
        }

        SelectTicketBottomBar(
            selectedTicketClass = selectedTicketClass,
            passengerCount = passengerCount,
            onContinueClick = {
                val selected = selectedTicketClass ?: return@SelectTicketBottomBar

                onContinueClick(
                    scheduleId,
                    selected.id,
                    selected.price,
                    passengerCount
                )
            }
        )
    }
}

@Composable
private fun SelectTicketHeader(
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
                        imageVector = Icons.Outlined.Sailing,
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
                    text = "Atur Tiket Perjalanan",
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Tentukan kelas tiket dan jumlah penumpang.",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Primary3,
                border = BorderStroke(1.dp, Neutral200)
            ) {
                Text(
                    text = "Step 1/3",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    color = Primary2,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelSmall
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
                    active = true,
                    done = false,
                    modifier = Modifier.weight(1f)
                )

                StepLine(modifier = Modifier.weight(0.35f))

                StepItem(
                    number = "2",
                    title = "Data",
                    active = false,
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
private fun PassengerCounterCard(
    passengerCount: Int,
    onDecreaseClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(13.dp),
                    color = Primary3,
                    border = BorderStroke(1.dp, Neutral200)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.EventSeat,
                            contentDescription = null,
                            tint = Primary2,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Jumlah Penumpang",
                        color = Neutral700,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Maksimal 5 penumpang per pesanan",
                        color = Neutral500,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            HorizontalDivider(color = Neutral200)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$passengerCount penumpang",
                    color = Neutral700,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CounterButton(
                        icon = Icons.Outlined.Remove,
                        enabled = passengerCount > 1,
                        onClick = onDecreaseClick
                    )

                    Text(
                        text = passengerCount.toString(),
                        modifier = Modifier.padding(horizontal = 18.dp),
                        color = Neutral700,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )

                    CounterButton(
                        icon = Icons.Outlined.Add,
                        enabled = passengerCount < 5,
                        onClick = onIncreaseClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CounterButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.size(36.dp),
        shape = CircleShape,
        color = if (enabled) Primary3 else Background,
        border = BorderStroke(1.dp, Neutral200)
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (enabled) Primary2 else Neutral500,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun SelectTicketNoteCard(
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
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = Primary2,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = "Jumlah penumpang yang dipilih akan menentukan jumlah data penumpang yang perlu diisi pada langkah berikutnya.",
                color = Neutral700,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SelectTicketBottomBar(
    selectedTicketClass: TicketClassOption?,
    passengerCount: Int,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalPrice = selectedTicketClass?.let {
        it.price * passengerCount
    } ?: 0

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
                        text = selectedTicketClass?.name ?: "Belum pilih kelas",
                        color = Neutral500,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = if (selectedTicketClass != null) {
                            PriceFormatter.formatToRupiah(totalPrice)
                        } else {
                            "Rp0"
                        },
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
                            imageVector = if (selectedTicketClass != null) {
                                Icons.Outlined.TaskAlt
                            } else {
                                Icons.Outlined.ConfirmationNumber
                            },
                            contentDescription = null,
                            tint = Primary2,
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.size(5.dp))

                        Text(
                            text = "$passengerCount pax",
                            color = Primary2,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            PrimaryButton(
                text = "Lanjut Isi Data Penumpang",
                onClick = onContinueClick,
                enabled = selectedTicketClass != null,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}