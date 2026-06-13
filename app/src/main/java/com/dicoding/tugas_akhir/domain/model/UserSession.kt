package com.dicoding.tugas_akhir.domain.model

data class UserSession(
    val uid: String,
    val name: String?,
    val email: String?,
    val photoUrl: String?,
)