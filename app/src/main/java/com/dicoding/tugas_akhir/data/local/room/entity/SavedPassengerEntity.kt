package com.dicoding.tugas_akhir.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_passengers")
data class SavedPassengerEntity(
    @PrimaryKey
    val id: String,
    val fullName: String,
    val nik: String,
    val phoneNumber: String,
    val gender: String,
    val createdAtMillis: Long,
)