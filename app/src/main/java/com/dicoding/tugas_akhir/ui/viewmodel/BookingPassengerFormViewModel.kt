package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugas_akhir.data.repository.SavedPassengerRepository
import com.dicoding.tugas_akhir.domain.model.BookingPassengerInput
import com.dicoding.tugas_akhir.domain.model.SavedPassenger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class BookingPassengerFormViewModel(
    private val savedPassengerRepository: SavedPassengerRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingPassengerFormUiState())
    val uiState: StateFlow<BookingPassengerFormUiState> = _uiState.asStateFlow()

    init {
        observeSavedPassengers()
    }

    private fun observeSavedPassengers() {
        viewModelScope.launch {
            savedPassengerRepository.getSavedPassengers().collect { passengers ->
                _uiState.value = _uiState.value.copy(
                    savedPassengers = passengers,
                )
            }
        }
    }

    fun setPassengerCount(count: Int) {
        val safeCount = count.coerceAtLeast(1)
        val currentPassengers = _uiState.value.passengers.toMutableList()

        while (currentPassengers.size < safeCount) {
            currentPassengers.add(BookingPassengerInput())
        }

        while (currentPassengers.size > safeCount) {
            currentPassengers.removeAt(currentPassengers.lastIndex)
        }

        _uiState.value = _uiState.value.copy(
            passengerCount = safeCount,
            passengers = currentPassengers,
        )
    }

    fun updateFullName(index: Int, value: String) {
        updatePassenger(index) {
            it.copy(fullName = value)
        }
    }

    fun updateNik(index: Int, value: String) {
        updatePassenger(index) {
            it.copy(
                nik = value.filter { char -> char.isDigit() }.take(16)
            )
        }
    }

    fun updatePhoneNumber(index: Int, value: String) {
        updatePassenger(index) {
            it.copy(
                phoneNumber = value.filter { char -> char.isDigit() }
            )
        }
    }

    fun updateGender(index: Int, value: String) {
        updatePassenger(index) {
            it.copy(gender = value)
        }
    }

    fun updateSaveToPassengerData(index: Int, checked: Boolean) {
        updatePassenger(index) {
            it.copy(saveToPassengerData = checked)
        }
    }

    fun openSavedPassengerSheet(index: Int) {
        _uiState.value = _uiState.value.copy(
            selectedPassengerIndex = index,
            isSavedPassengerSheetVisible = true,
        )
    }

    fun closeSavedPassengerSheet() {
        _uiState.value = _uiState.value.copy(
            selectedPassengerIndex = null,
            isSavedPassengerSheetVisible = false,
        )
    }

    fun applySavedPassenger(passenger: SavedPassenger) {
        val index = _uiState.value.selectedPassengerIndex ?: return

        updatePassenger(index) {
            it.copy(
                id = passenger.id,
                fullName = passenger.fullName,
                nik = passenger.nik,
                phoneNumber = passenger.phoneNumber,
                gender = passenger.gender,
                saveToPassengerData = false,
            )
        }

        closeSavedPassengerSheet()
    }

    fun continueBooking(
        onSuccess: (List<BookingPassengerInput>) -> Unit,
    ) {
        val passengers = _uiState.value.passengers

        if (!passengers.all { it.isValid }) return

        viewModelScope.launch {
            passengers
                .filter { it.saveToPassengerData }
                .forEach { passenger ->
                    savedPassengerRepository.savePassenger(
                        SavedPassenger(
                            id = passenger.id.ifBlank {
                                "SP-${UUID.randomUUID().toString().take(8).uppercase()}"
                            },
                            fullName = passenger.fullName,
                            nik = passenger.nik,
                            phoneNumber = passenger.phoneNumber,
                            gender = passenger.gender,
                        )
                    )
                }

            onSuccess(passengers)
        }
    }

    private fun updatePassenger(
        index: Int,
        transform: (BookingPassengerInput) -> BookingPassengerInput,
    ) {
        val currentPassengers = _uiState.value.passengers.toMutableList()

        if (index !in currentPassengers.indices) return

        currentPassengers[index] = transform(currentPassengers[index])

        _uiState.value = _uiState.value.copy(
            passengers = currentPassengers,
        )
    }

    fun savePassengerFromBooking(
        fullName: String,
        nik: String,
        phoneNumber: String,
        gender: String,
    ) {
        if (
            fullName.isBlank() ||
            nik.length != 16 ||
            phoneNumber.length < 10 ||
            gender.isBlank()
        ) {
            return
        }

        viewModelScope.launch {
            savedPassengerRepository.savePassenger(
                SavedPassenger(
                    id = "SP-${UUID.randomUUID().toString().take(8).uppercase()}",
                    fullName = fullName,
                    nik = nik,
                    phoneNumber = phoneNumber,
                    gender = gender,
                )
            )
        }
    }
}

data class BookingPassengerFormUiState(
    val passengerCount: Int = 1,
    val passengers: List<BookingPassengerInput> = listOf(
        BookingPassengerInput()
    ),
    val savedPassengers: List<SavedPassenger> = emptyList(),
    val selectedPassengerIndex: Int? = null,
    val isSavedPassengerSheetVisible: Boolean = false,
) {
    val isFormValid: Boolean
        get() = passengers.isNotEmpty() && passengers.all { it.isValid }
}