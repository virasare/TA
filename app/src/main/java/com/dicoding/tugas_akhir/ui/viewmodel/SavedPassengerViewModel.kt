package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugas_akhir.data.repository.SavedPassengerRepository
import com.dicoding.tugas_akhir.domain.model.SavedPassenger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class SavedPassengerViewModel(
    private val repository: SavedPassengerRepository,
) : ViewModel() {

    private val _passengers = MutableStateFlow<List<SavedPassenger>>(emptyList())
    val passengers: StateFlow<List<SavedPassenger>> = _passengers.asStateFlow()

    private val _formState = MutableStateFlow(SavedPassengerFormState())
    val formState: StateFlow<SavedPassengerFormState> = _formState.asStateFlow()

    init {
        loadPassengers()
    }

    private fun loadPassengers() {
        viewModelScope.launch {
            repository.getSavedPassengers().collect { passengers ->
                _passengers.value = passengers
            }
        }
    }

    fun loadPassengerForEdit(id: String) {
        viewModelScope.launch {
            val passenger = repository.getSavedPassengerById(id) ?: return@launch

            _formState.value = SavedPassengerFormState(
                id = passenger.id,
                fullName = passenger.fullName,
                nik = passenger.nik,
                phoneNumber = passenger.phoneNumber,
                gender = passenger.gender,
            )
        }
    }

    fun updateFullName(value: String) {
        _formState.value = _formState.value.copy(fullName = value)
    }

    fun updateNik(value: String) {
        _formState.value = _formState.value.copy(
            nik = value.filter { it.isDigit() }.take(16)
        )
    }

    fun updatePhone(value: String) {
        _formState.value = _formState.value.copy(
            phoneNumber = value.filter { it.isDigit() }
        )
    }

    fun updateGender(value: String) {
        _formState.value = _formState.value.copy(gender = value)
    }

    fun savePassenger(onSaved: () -> Unit) {
        val form = _formState.value

        if (!form.isValid) return

        viewModelScope.launch {
            repository.savePassenger(
                SavedPassenger(
                    id = form.id.ifBlank {
                        "SP-${UUID.randomUUID().toString().take(8).uppercase()}"
                    },
                    fullName = form.fullName,
                    nik = form.nik,
                    phoneNumber = form.phoneNumber,
                    gender = form.gender,
                )
            )

            resetForm()
            onSaved()
        }
    }

    fun deletePassenger(passenger: SavedPassenger) {
        viewModelScope.launch {
            repository.deletePassenger(passenger)
        }
    }

    fun resetForm() {
        _formState.value = SavedPassengerFormState()
    }
}

data class SavedPassengerFormState(
    val id: String = "",
    val fullName: String = "",
    val nik: String = "",
    val phoneNumber: String = "",
    val gender: String = "Perempuan",
) {
    val isValid: Boolean
        get() = fullName.isNotBlank() &&
                nik.length == 16 &&
                phoneNumber.length >= 10 &&
                gender.isNotBlank()
}