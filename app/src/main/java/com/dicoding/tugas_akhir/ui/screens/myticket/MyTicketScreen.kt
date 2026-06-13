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

private val White = Color(0xFFFFFFFF)
private val Background = Color(0xFFF6F8FB)
private val Primary2 = Color(0xFF1976D2)
private val Primary3 = Color(0xFFE8F2FD)
private val Neutral500 = Color(0xFF6B7280)
private val Neutral200 = Color(0xFFE5E7EB)
private val Primary1 = Color(0xFF0B1F3A)

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
            .background(Background)
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
                        animationFile = "lottie/empty_schedule.json",
                        title = "Belum Ada Tiket",
                        message = state.message,
                    )
                }
            }

            is MyTicketUiState.Error -> {
                item {
                    LottieStateView(
                        animationFile = "lottie/error_connection.json",
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
                    containerColor = White,
                    labelColor = Neutral500,
                    selectedContainerColor = Primary3,
                    selectedLabelColor = Primary1,
                    disabledContainerColor = Neutral200,
                    disabledLabelColor = Neutral500,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected,
                    borderColor = Neutral200,
                    selectedBorderColor = Primary2,
                ),
            )
        }
    }
}