package com.dicoding.tugas_akhir.ui.state

import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.domain.model.ETicket

enum class TicketFilter(
    val label: String,
) {
    All("Semua"),
    WaitingPayment("Menunggu"),
    Active("Aktif"),
    Finished("Selesai"),
}

sealed interface MyTicketUiState {
    data object Loading : MyTicketUiState
    data class Success(val tickets: List<Booking>) : MyTicketUiState
    data class Empty(val message: String) : MyTicketUiState
    data class Error(val message: String) : MyTicketUiState
}

sealed interface ETicketUiState {
    data object Loading : ETicketUiState
    data class Success(val ticket: ETicket) : ETicketUiState
    data class Error(val message: String) : ETicketUiState
}