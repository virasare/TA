package com.dicoding.tugas_akhir.domain.model

data class BookingPassengerInput(
    val id: String = "",
    val fullName: String = "",
    val nik: String = "",
    val phoneNumber: String = "",
    val gender: String = "Perempuan",
    val saveToPassengerData: Boolean = false,
) {
    val isValid: Boolean
        get() = fullName.isNotBlank() &&
                nik.length == 16 &&
                phoneNumber.length >= 10 &&
                gender.isNotBlank()
}