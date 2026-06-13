package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugas_akhir.core.common.Resource
import com.dicoding.tugas_akhir.data.remote.request.CreateBookingRequest
import com.dicoding.tugas_akhir.data.remote.request.PassengerRequest
import com.dicoding.tugas_akhir.data.repository.BookingRepository
import com.dicoding.tugas_akhir.domain.model.TicketClassOption
import com.dicoding.tugas_akhir.ui.state.BookingDetailUiState
import com.dicoding.tugas_akhir.ui.state.CreateBookingUiState
import com.dicoding.tugas_akhir.ui.state.PassengerFormState
import com.dicoding.tugas_akhir.ui.state.TicketClassUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingViewModel(
    private val bookingRepository: BookingRepository,
) : ViewModel() {

    private val _ticketClassUiState = MutableStateFlow<TicketClassUiState>(
        TicketClassUiState.Loading
    )
    val ticketClassUiState: StateFlow<TicketClassUiState> =
        _ticketClassUiState.asStateFlow()

    private val _selectedTicketClass = MutableStateFlow<TicketClassOption?>(null)
    val selectedTicketClass: StateFlow<TicketClassOption?> =
        _selectedTicketClass.asStateFlow()

    private val _passengerCount = MutableStateFlow(1)
    val passengerCount: StateFlow<Int> = _passengerCount.asStateFlow()

    private val _passengerForms = MutableStateFlow(listOf(PassengerFormState()))
    val passengerForms: StateFlow<List<PassengerFormState>> =
        _passengerForms.asStateFlow()

    private val _createBookingUiState = MutableStateFlow<CreateBookingUiState>(
        CreateBookingUiState.Idle
    )
    val createBookingUiState: StateFlow<CreateBookingUiState> =
        _createBookingUiState.asStateFlow()

    private val _bookingDetailUiState = MutableStateFlow<BookingDetailUiState>(
        BookingDetailUiState.Loading
    )
    val bookingDetailUiState: StateFlow<BookingDetailUiState> =
        _bookingDetailUiState.asStateFlow()

    fun loadTicketClassOptions(scheduleId: String) {
        viewModelScope.launch {
            bookingRepository.getTicketClassOptions(scheduleId).collect { result ->
                _ticketClassUiState.value = when (result) {
                    is Resource.Loading -> TicketClassUiState.Loading
                    is Resource.Success -> TicketClassUiState.Success(result.data)
                    is Resource.Empty -> TicketClassUiState.Empty("Kelas tiket tidak tersedia")
                    is Resource.Error -> TicketClassUiState.Error(result.message)
                }
            }
        }
    }

    fun selectTicketClass(ticketClass: TicketClassOption) {
        _selectedTicketClass.value = ticketClass
    }

    fun increasePassenger() {
        val currentCount = _passengerCount.value
        if (currentCount < 5) {
            _passengerCount.value = currentCount + 1
        }
    }

    fun decreasePassenger() {
        val currentCount = _passengerCount.value
        if (currentCount > 1) {
            _passengerCount.value = currentCount - 1
        }
    }

    fun preparePassengerForms(count: Int) {
        val safeCount = count.coerceIn(1, 5)
        val currentForms = _passengerForms.value

        _passengerForms.value = List(safeCount) { index ->
            currentForms.getOrNull(index) ?: PassengerFormState()
        }
    }

    fun updatePassengerFullName(index: Int, value: String) {
        updatePassengerForm(index) {
            it.copy(fullName = value)
        }
    }

    fun updatePassengerNik(index: Int, value: String) {
        updatePassengerForm(index) {
            it.copy(nik = value.filter { char -> char.isDigit() }.take(16))
        }
    }

    fun updatePassengerPhoneNumber(index: Int, value: String) {
        updatePassengerForm(index) {
            it.copy(phoneNumber = value.filter { char -> char.isDigit() })
        }
    }

    fun updatePassengerBirthDate(index: Int, value: String) {
        updatePassengerForm(index) {
            it.copy(birthDate = value)
        }
    }

    fun updatePassengerGender(index: Int, value: String) {
        updatePassengerForm(index) {
            it.copy(gender = value)
        }
    }

    private fun updatePassengerForm(
        index: Int,
        transform: (PassengerFormState) -> PassengerFormState,
    ) {
        _passengerForms.value = _passengerForms.value.mapIndexed { formIndex, form ->
            if (formIndex == index) transform(form) else form
        }
    }

    fun createBooking(
        scheduleId: String,
        ticketClassId: String,
    ) {
        val forms = _passengerForms.value

        if (forms.any { !it.isValid }) {
            _createBookingUiState.value = CreateBookingUiState.Error(
                "Lengkapi semua data penumpang dengan benar"
            )
            return
        }

        val request = CreateBookingRequest(
            scheduleId = scheduleId,
            ticketClassId = ticketClassId,
            passengers = forms.map { form ->
                PassengerRequest(
                    fullName = form.fullName,
                    nik = form.nik,
                    phoneNumber = form.phoneNumber,
                    birthDate = form.birthDate,
                    gender = form.gender,
                )
            },
        )

        viewModelScope.launch {
            bookingRepository.createBooking(request).collect { result ->
                _createBookingUiState.value = when (result) {
                    is Resource.Loading -> CreateBookingUiState.Loading
                    is Resource.Success -> CreateBookingUiState.Success(result.data)
                    is Resource.Empty -> CreateBookingUiState.Error("Pesanan gagal dibuat")
                    is Resource.Error -> CreateBookingUiState.Error(result.message)
                }
            }
        }
    }

    fun getBookingDetail(bookingId: String) {
        viewModelScope.launch {
            bookingRepository.getBookingDetail(bookingId).collect { result ->
                _bookingDetailUiState.value = when (result) {
                    is Resource.Loading -> BookingDetailUiState.Loading
                    is Resource.Success -> BookingDetailUiState.Success(result.data)
                    is Resource.Empty -> BookingDetailUiState.Error("Pesanan tidak ditemukan")
                    is Resource.Error -> BookingDetailUiState.Error(result.message)
                }
            }
        }
    }

    fun resetCreateBookingState() {
        _createBookingUiState.value = CreateBookingUiState.Idle
    }
}