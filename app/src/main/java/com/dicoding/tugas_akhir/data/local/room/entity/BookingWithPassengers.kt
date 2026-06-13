package com.dicoding.tugas_akhir.data.local.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BookingWithPassengers(
    @Embedded
    val booking: BookingEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "bookingId",
    )
    val passengers: List<PassengerEntity>,
)