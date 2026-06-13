package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugas_akhir.core.common.Resource
import com.dicoding.tugas_akhir.data.repository.MyTicketRepository
import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.ui.state.ETicketUiState
import com.dicoding.tugas_akhir.ui.state.MyTicketUiState
import com.dicoding.tugas_akhir.ui.state.TicketFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyTicketViewModel(
    private val myTicketRepository: MyTicketRepository,
) : ViewModel() {

    private var allTickets: List<Booking> = emptyList()

    private val _selectedFilter = MutableStateFlow(TicketFilter.All)
    val selectedFilter: StateFlow<TicketFilter> = _selectedFilter.asStateFlow()

    private val _myTicketUiState = MutableStateFlow<MyTicketUiState>(
        MyTicketUiState.Loading
    )
    val myTicketUiState: StateFlow<MyTicketUiState> =
        _myTicketUiState.asStateFlow()

    private val _eTicketUiState = MutableStateFlow<ETicketUiState>(
        ETicketUiState.Loading
    )
    val eTicketUiState: StateFlow<ETicketUiState> =
        _eTicketUiState.asStateFlow()

    fun loadMyTickets() {
        viewModelScope.launch {
            myTicketRepository.getMyTickets().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _myTicketUiState.value = MyTicketUiState.Loading
                    }

                    is Resource.Success -> {
                        allTickets = result.data
                        applyFilter()
                    }

                    is Resource.Empty -> {
                        allTickets = emptyList()
                        _myTicketUiState.value = MyTicketUiState.Empty(
                            "Belum ada tiket atau pesanan"
                        )
                    }

                    is Resource.Error -> {
                        _myTicketUiState.value = MyTicketUiState.Error(result.message)
                    }
                }
            }
        }
    }

    fun changeFilter(filter: TicketFilter) {
        _selectedFilter.value = filter
        applyFilter()
    }

    private fun applyFilter() {
        val filteredTickets = when (_selectedFilter.value) {
            TicketFilter.All -> allTickets

            TicketFilter.WaitingPayment -> allTickets.filter { ticket ->
                ticket.status.equals("Menunggu Pembayaran", ignoreCase = true)
            }

            TicketFilter.Active -> allTickets.filter { ticket ->
                ticket.status.equals("Aktif", ignoreCase = true)
            }

            TicketFilter.Finished -> allTickets.filter { ticket ->
                ticket.status.equals("Selesai", ignoreCase = true)
            }
        }

        _myTicketUiState.value = if (filteredTickets.isEmpty()) {
            MyTicketUiState.Empty("Tidak ada tiket pada kategori ini")
        } else {
            MyTicketUiState.Success(filteredTickets)
        }
    }

    fun loadETicketByBookingId(bookingId: String) {
        viewModelScope.launch {
            myTicketRepository.getETicketByBookingId(bookingId).collect { result ->
                _eTicketUiState.value = when (result) {
                    is Resource.Loading -> ETicketUiState.Loading
                    is Resource.Success -> ETicketUiState.Success(result.data)
                    is Resource.Empty -> ETicketUiState.Error("E-ticket tidak ditemukan")
                    is Resource.Error -> ETicketUiState.Error(result.message)
                }
            }
        }
    }

    fun loadETicketByPaymentId(paymentId: String) {
        viewModelScope.launch {
            myTicketRepository.getETicketByPaymentId(paymentId).collect { result ->
                _eTicketUiState.value = when (result) {
                    is Resource.Loading -> ETicketUiState.Loading
                    is Resource.Success -> ETicketUiState.Success(result.data)
                    is Resource.Empty -> ETicketUiState.Error("E-ticket tidak ditemukan")
                    is Resource.Error -> ETicketUiState.Error(result.message)
                }
            }
        }
    }
}