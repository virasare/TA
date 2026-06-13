package com.dicoding.tugas_akhir.ui.state

import com.dicoding.tugas_akhir.core.utils.ValidationUtils
import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.domain.model.TicketClassOption

data class PassengerFormState(
    val fullName: String = "",
    val nik: String = "",
    val phoneNumber: String = "",
    val birthDate: String = "",
    val gender: String = "",
) {
    val isValid: Boolean
        get() = ValidationUtils.isRequired(fullName) &&
                ValidationUtils.isNikValid(nik) &&
                ValidationUtils.isPhoneValid(phoneNumber) &&
                ValidationUtils.isRequired(birthDate) &&
                ValidationUtils.isRequired(gender)
}

sealed interface TicketClassUiState {
    data object Loading : TicketClassUiState
    data class Success(val ticketClasses: List<TicketClassOption>) : TicketClassUiState
    data class Empty(val message: String) : TicketClassUiState
    data class Error(val message: String) : TicketClassUiState
}

sealed interface CreateBookingUiState {
    data object Idle : CreateBookingUiState
    data object Loading : CreateBookingUiState
    data class Success(val booking: Booking) : CreateBookingUiState
    data class Error(val message: String) : CreateBookingUiState
}

sealed interface BookingDetailUiState {
    data object Loading : BookingDetailUiState
    data class Success(val booking: Booking) : BookingDetailUiState
    data class Error(val message: String) : BookingDetailUiState
}