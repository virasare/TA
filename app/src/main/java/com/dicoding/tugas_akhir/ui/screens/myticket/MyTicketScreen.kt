package com.dicoding.tugas_akhir.ui.screens.myticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.ui.components.cards.MyTicketCard
import com.dicoding.tugas_akhir.ui.components.loading.ScheduleListPlaceholder
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView
import com.dicoding.tugas_akhir.ui.state.MyTicketUiState
import com.dicoding.tugas_akhir.ui.state.TicketFilter
import com.dicoding.tugas_akhir.ui.viewmodel.MyTicketViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

@Composable
fun MyTicketScreen(
    onTicketClick: (String) -> Unit,
    onPayNowClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyTicketViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val myTicketUiState by viewModel.myTicketUiState.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMyTickets()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("my_ticket_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            TicketFilterSection(
                selectedFilter = selectedFilter,
                onFilterClick = viewModel::changeFilter,
            )
        }

        when (val state = myTicketUiState) {
            is MyTicketUiState.Loading -> {
                item {
                    ScheduleListPlaceholder(itemCount = 3)
                }
            }

            is MyTicketUiState.Success -> {
                items(
                    items = state.tickets,
                    key = { ticket -> ticket.id },
                ) { ticket ->
                    MyTicketCard(
                        ticket = ticket,
                        onTicketClick = {
                            onTicketClick(ticket.id)
                        },
                        onPayNowClick = {
                            onPayNowClick(ticket.id)
                        },
                    )
                }
            }

            is MyTicketUiState.Empty -> {
                item {
                    LottieStateView(
                        animationFile = "empty.json",
                        title = "Belum Ada Tiket",
                        message = state.message,
                    )
                }
            }

            is MyTicketUiState.Error -> {
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
}

@Composable
private fun TicketFilterSection(
    selectedFilter: TicketFilter,
    onFilterClick: (TicketFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(enumValues<TicketFilter>().toList()) { filter ->
            val selected = selectedFilter == filter

            FilterChip(
                selected = selected,
                onClick = {
                    onFilterClick(filter)
                },
                label = {
                    Text(filter.label)
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = colors.surface,
                    labelColor = colors.onSurfaceVariant,
                    selectedContainerColor = colors.primaryContainer,
                    selectedLabelColor = colors.onPrimaryContainer,
                    disabledContainerColor = colors.surfaceVariant,
                    disabledLabelColor = colors.onSurfaceVariant,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected,
                    borderColor = colors.outlineVariant,
                    selectedBorderColor = colors.primary,
                ),
            )
        }
    }
}
