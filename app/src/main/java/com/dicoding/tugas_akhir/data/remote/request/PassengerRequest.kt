package com.dicoding.tugas_akhir.data.remote.request

data class PassengerRequest(
    val fullName: String,
    val nik: String,
    val phoneNumber: String,
    val birthDate: String,
    val gender: String,
)