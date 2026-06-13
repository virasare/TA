package com.dicoding.tugas_akhir.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey
    val id: String,
    val scheduleId: String,
    val shipName: String,
    val origin: String,
    val destination: String,
    val departureDate: String,
    val departureTime: String,
    val ticketClassName: String,
    val ticketPrice: Int,
    val passengerCount: Int,
    val adminFee: Int,
    val totalPrice: Int,
    val status: String,
    val createdAt: String,
    val paymentDeadline: String,
    val createdAtMillis: Long,
)