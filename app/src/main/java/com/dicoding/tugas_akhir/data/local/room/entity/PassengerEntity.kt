package com.dicoding.tugas_akhir.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passengers")
data class PassengerEntity(
    @PrimaryKey
    val id: String,
    val bookingId: String,
    val passengerOrder: Int,
    val fullName: String,
    val nik: String,
    val phoneNumber: String,
    val birthDate: String,
    val gender: String,
)