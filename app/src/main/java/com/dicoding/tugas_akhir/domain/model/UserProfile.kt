package com.dicoding.tugas_akhir.domain.model

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val photoUri: String = "",
)